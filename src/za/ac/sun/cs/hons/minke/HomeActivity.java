package za.ac.sun.cs.hons.minke;

import java.util.Date;

import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
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
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.markupartist.android.widget.ActionBar;

public class HomeActivity extends Activity {
	protected int selectedBranch;
	private boolean downloading;
	private long code;
	private Branch branch;
	private class InitTask extends ProgressTask {
		public InitTask() {
			super(HomeActivity.this, 6,"Loading", "Loading data, please wait...", false);
		}

		protected void retrieve(int counter) {
			if (counter == 0) {
				RPCUtils.retrieveBrands();
			} else if (counter == 1) {
				RPCUtils.retrieveProducts();
			} else if (counter == 2) {
				RPCUtils.retrieveCategories();
			} else if (counter == 3) {
				RPCUtils.retrieveLocations();
			} else if (counter == 4) {
				MapUtils.refreshLocation((LocationManager) getSystemService(LOCATION_SERVICE));
			} else if (counter == 5) {
				RPCUtils.retrieveBranches(MapUtils.getLocation().getLat(),
						MapUtils.getLocation().getLon());
			}
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
				startActivity(IntentUtils
						.getNewProductIntent(getApplicationContext()));
			}
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
			AlertDialog.Builder msg = new AlertDialog.Builder(HomeActivity.this);
			msg.setTitle("Product Updated");
			msg.setMessage("Product was successfully updated in the database.");
			msg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					startActivity(IntentUtils
							.getBrowseIntent(HomeActivity.this));
					dialog.cancel();
				}
			});
			msg.show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!EntityUtils.isLoaded()){
			InitTask task = new InitTask();
			task.execute();
			EntityUtils.setLoaded(true);
		}
		start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			scan(this.getCurrentFocus());
			return true;
		case R.id.search:
			search(this.getCurrentFocus());
			return true;
		case R.id.shop:
			shop(this.getCurrentFocus());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void start() {
		setContentView(R.layout.home);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_home);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getLocationSearchAction(this));
		actionBar.addAction(ActionUtils.getShopAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));

	}

	public void shop(View view) {
		startActivity(IntentUtils.getShopIntent(getApplicationContext()));
	}

	public void search(View view) {

		startActivity(IntentUtils
				.getLocationSearchIntent(getApplicationContext()));
	}

	public void scan(View view) {
		AlertDialog dialog;
		downloading = false;
		if ((dialog = IntentIntegrator.initiateScan(this)) != null) {
			downloading = true;
			dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							downloading = false;
							onActivityResult(RESULT_CANCELED, 0, null);
						}

					});

		}
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
							scan(null);
							dialog.cancel();
						}
					});
			failed.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			failed.show();
		}
	}

	private void confirmLocation() {
		if (EntityUtils.getBranches() != null) {
			return;
		}
		final int size = Math.min(EntityUtils.getBranches().size(), 10);
		String[] names = new String[size + 1];
		int i = 0;
		for (IsEntity b : EntityUtils.getBranches()) {
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
						if (selectedBranch == size) {
							editLocation();
						} else {
							branch = (Branch) EntityUtils.getBranches().get(
									selectedBranch);
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
							double price = (Double.parseDouble(updatePriceText
									.getText().toString()));
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

}
