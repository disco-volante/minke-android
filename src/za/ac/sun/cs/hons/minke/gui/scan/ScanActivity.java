package za.ac.sun.cs.hons.minke.gui.scan;

import java.util.Date;
import java.util.List;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;

public class ScanActivity extends Activity {

	private static final String TAG = "ScanActivity";
	private EditText nameText, priceText, sizeText;
	private AutoCompleteTextView brandText, categoryText;
	private Spinner unitSpinner;
	private LocationManager locationManager;
	private List<IsEntity> branches;
	protected int selectedBranch;
	private long code;
	protected Branch branch;
	private boolean downloading;

	class LocationTask extends AsyncTask<Double, Void, Void> {
		@Override
		protected Void doInBackground(Double... params) {
			RPCUtils.retrieveBranches(params[0], params[1]);
			System.out.println(params[0] + "  " + params[1]);
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			branches = EntityUtils.getBranches();
			scan();
		}
	}

	class FindProductTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			RPCUtils.getBranchProduct(code, branch);
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			if (EntityUtils.getScanned() != null) {
				updatePrice(EntityUtils.getScanned());
			} else {
				unknownProduct();
			}
		}
	}

	class AddProductTask extends AsyncTask<BranchProduct, Void, Void> {
		@Override
		protected Void doInBackground(BranchProduct... params) {
			RPCUtils.addBranchProduct(params[0], code, branch.getID());
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			clear();
			AlertDialog.Builder msg = new AlertDialog.Builder(ScanActivity.this);
			msg.setTitle("Product Added");
			msg.setMessage("Product was successfully added to the database.");
			msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					startActivity(IntentUtils
							.getBrowseIntent(ScanActivity.this));
					dialog.cancel();
				}
			});
			msg.show();
		}
	}

	class UpdateProductTask extends AsyncTask<BranchProduct, Void, Void> {
		@Override
		protected Void doInBackground(BranchProduct... params) {
			RPCUtils.updateBranchProduct(params[0]);
			return null;

		}

		@Override
		protected void onPostExecute(Void v) {
			AlertDialog.Builder msg = new AlertDialog.Builder(ScanActivity.this);
			msg.setTitle("Product Updated");
			msg.setMessage("Product was successfully updated in the database.");
			msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					startActivity(IntentUtils
							.getBrowseIntent(ScanActivity.this));
					dialog.cancel();
				}
			});
			msg.show();
		}
	}

	private class TextErrorWatcher implements TextWatcher {
		TextView view;
		private boolean numeric;

		public TextErrorWatcher(TextView view, boolean numeric) {
			this.view = view;
			this.numeric = numeric;
			afterTextChanged(view.getEditableText());
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().length() == 0) {
				view.setError("Input is required");
			}
			if (numeric
					&& !s.toString().matches("([1-9][0-9]*)+(\\.[0-9]{1,2}+)?")) {
				view.setError("Input must be numeric, up to 2 decimal places and greater than 0.");
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		double lat, lon;
		if (location != null) {
			Log.d(TAG, location.toString());
			lat = location.getLatitude();
			lon = location.getLongitude();

		} else {
			lat = MapUtils.getLocation().getLat();
			lon = MapUtils.getLocation().getLon();
		}
		LocationTask task = new LocationTask();
		task.execute(lat, lon);
	}

	private void initGUI() {
		setContentView(R.layout.scanned);
		nameText = (EditText) findViewById(R.id.nameText);
		nameText.addTextChangedListener(new TextErrorWatcher(nameText, false));
		brandText = (AutoCompleteTextView) findViewById(R.id.brandText);
		ArrayAdapter<IsEntity> brands = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getBrands());
		brandText.setAdapter(brands);
		brandText
				.addTextChangedListener(new TextErrorWatcher(brandText, false));
		categoryText = (AutoCompleteTextView) findViewById(R.id.categoryText);
		ArrayAdapter<IsEntity> categories = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getCategories());
		categoryText.setAdapter(categories);
		categoryText.addTextChangedListener(new TextErrorWatcher(categoryText,
				false));
		priceText = (EditText) findViewById(R.id.priceText);
		priceText.addTextChangedListener(new TextErrorWatcher(priceText, true));
		sizeText = (EditText) findViewById(R.id.sizeText);
		sizeText.addTextChangedListener(new TextErrorWatcher(sizeText, true));
		unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
		SpinnerAdapter units = new ArrayAdapter<String>(this,
				R.layout.dropdown_item, getMeasures());
		unitSpinner.setAdapter(units);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_scan);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getBrowseAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
		clear();
	}

	private String[] getMeasures() {
		return new String[] { "mg", "g", "kg", "ml", "l", "ea" };
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.scan_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getScanIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.browse:
			startActivity(IntentUtils.getBrowseIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void scan() {
		/*
		 * AlertDialog dialog; downloading = false; if ((dialog =
		 * IntentIntegrator.initiateScan(this)) != null) { downloading = true;
		 * dialog.setOnCancelListener(new OnCancelListener() {
		 * 
		 * @Override public void onCancel(DialogInterface dialog) { downloading
		 * = false; onActivityResult(RESULT_CANCELED, 0, null); }
		 * 
		 * }); dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
		 * .setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { downloading = false;
		 * onActivityResult(RESULT_CANCELED, 0, null); }
		 * 
		 * });
		 * 
		 * }
		 */

		Intent data = new Intent();
		data.putExtra("SCAN_RESULT", "2991");
		data.putExtra("SCAN_RESULT_FORMAT", "BARCODE");
		onActivityResult(IntentIntegrator.REQUEST_CODE, Activity.RESULT_OK,
				data);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentIntegrator.REQUEST_CODE
				&& resultCode != RESULT_CANCELED) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult != null) {
				try {
					code = Long.parseLong(scanResult.getContents());
					confirmLocation();
					return;
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		} else if (!downloading && resultCode == RESULT_CANCELED) {
			AlertDialog.Builder failed = new AlertDialog.Builder(this);
			failed.setTitle("Scan Error");
			failed.setMessage("Unsuccessful scan, please try again.");
			failed.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							scan();
							dialog.cancel();
						}
					});
			failed.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							startActivity(IntentUtils
									.getHomeIntent(ScanActivity.this));
							dialog.cancel();
						}
					});
			failed.show();
		}
	}

	private void confirmLocation() {
		int size = 10;
		if (branches.size() < 10) {
			size = branches.size();
		}
		String[] names = new String[size + 1];
		int i = 0;
		for (IsEntity b : branches) {
			if (i == size) {
				break;
			}
			names[i++] = b.toString();
		}
		names[size] = "Other";
		AlertDialog.Builder location = new AlertDialog.Builder(this);
		location.setTitle("Confirm Location");
		location.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				selectedBranch = position;
			}
		});
		location.setPositiveButton("Get Product",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if (selectedBranch == branches.size()) {
							editLocation();
						} else {
							branch = (Branch) branches.get(selectedBranch);
							FindProductTask task = new FindProductTask();
							task.execute();
						}
					}
				});
		location.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		location.show();

	}

	protected void editLocation() {

	}

	private void unknownProduct() {

		AlertDialog.Builder failed = new AlertDialog.Builder(this);
		failed.setTitle("Product Not Found");
		failed.setMessage("Product was not found in our database.");
		failed.setPositiveButton("Add product",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						initGUI();
						dialog.cancel();
					}
				});
		failed.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(IntentUtils
								.getHomeIntent(ScanActivity.this));
						dialog.cancel();
					}
				});
		failed.show();
	}

	private void updatePrice(final BranchProduct found) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View updateView = factory.inflate(R.layout.update_dialog, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.product_lbl);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.price_text);
		updatePriceText.addTextChangedListener(new TextErrorWatcher(
				updatePriceText, true));
		productText.setText(found.getProduct().toString());
		AlertDialog.Builder success = new AlertDialog.Builder(this);
		success.setTitle("Product Found");
		success.setView(updateView);
		success.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (updatePriceText.getError() == null) {
							double price = (Double
									.parseDouble(updatePriceText.getText()
											.toString()));
							found.setDate(new Date());
							found.setPrice(price);
							UpdateProductTask task = new UpdateProductTask();
							task.execute(found);
							dialog.cancel();
						}

					}
				});
		success.show();

	}

	public void addProduct(View view) {
		if (nameText.getError() != null || brandText.getError() != null
				|| categoryText.getError() != null
				|| sizeText.getError() != null || priceText.getError() != null) {
			return;
		}
		BranchProduct bp = new BranchProduct(nameText.getText().toString(),
				brandText.getText().toString(), null, null,
				Double.parseDouble(priceText.getText().toString()),
				Double.parseDouble(sizeText.getText().toString()), unitSpinner
						.getSelectedItem().toString(), null);
		AddProductTask task = new AddProductTask();
		task.execute(bp);
	}

	private void clear() {
		nameText.setText("");
		brandText.setText("");
		categoryText.setText("");
		priceText.setText("");
		sizeText.setText("");
	}
}
