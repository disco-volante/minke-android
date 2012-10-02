package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.LoadDataFGTask;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataFGTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class HomeActivity extends SherlockActivity {
	protected int selectedBranch;
	private boolean downloading;
	private long code;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		setTheme(R.style.Theme_Sherlock_Light);
		if (Debug.ON) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().build());
		}
		nextAction();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.settings) {
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		}
		return false;
	}

	private void checkData(Intent intent) {
		if (EntityUtils.isLoaded()) {
			startActivity(intent);
		} else {
			Builder dlg = DialogUtils.getErrorDialog(this, ERROR.NOT_LOADED);
			dlg.show();
		}
	}

	protected void nextAction() {
		if (!PreferencesUtils.isLoaded()) {
			PrefsTask prefTask = new PrefsTask();
			prefTask.execute();
		} else if (IntentUtils.scan()) {
			scan(null);
		} else if (MapUtils.getLocation() == null) {
			GetLocationTask task = new GetLocationTask();
			task.execute();
		} else if (!EntityUtils.isLoaded()) {
			if (PreferencesUtils.isFirstTime()
					|| PreferencesUtils.getUpdateFrequency() == Constants.STARTUP) {
				Updater updater = new Updater();
				updater.execute();
			} else {
				LoadDataFGTask local = new LoadDataFGTask(this);
				local.execute();
			}
		}
	}

	public void shop(View view) {
		checkData(IntentUtils.getShopIntent(getApplicationContext()));
	}

	public void search(View view) {
		checkData(IntentUtils.getLocationSearchIntent(getApplicationContext()));
	}

	public void scan(View view) {
		if (MapUtils.getUserBranch() == null) {
			confirmLocation();
		} else {
			if (Debug.EMULATOR) {
				Intent intent = IntentUtils.getEmulatorIntent();
				onActivityResult(IntentIntegrator.REQUEST_CODE, RESULT_OK,
						intent);
			} else {
				final AlertDialog dialog;
				downloading = false;
				if ((dialog = IntentIntegrator.initiateScan(this)) != null) {
					downloading = true;
					dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									downloading = false;
									onActivityResult(RESULT_CANCELED, 0, null);
									dialog.cancel();
								}

							});

				}
			}
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
					FindProductTask task = new FindProductTask();
					task.execute();
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

	public void confirmLocation() {
		if (MapUtils.getBranches() == null) {
			return;
		}
		final int size = Math.min(MapUtils.getBranches().size(), 5);
		String[] names = new String[size + 1];
		int i = 0;
		for (Branch b : MapUtils.getBranches()) {
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
							MapUtils.setUserBranch(null);
							editLocation();
						} else {
							MapUtils.setUserBranch(MapUtils.getBranches().get(
									selectedBranch));
							scan(null);
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
		Log.v("LOCATION", MapUtils.getLocation().toString());
		startActivity(IntentUtils.getNewBranchIntent(this));
	}

	private void updatePrice(final BranchProduct found, final Product product) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View updateView = factory.inflate(R.layout.update_dialog, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.product_lbl);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.price_text);
		updatePriceText.addTextChangedListener(new TextErrorWatcher(
				updatePriceText, true));
		productText.setText(product.toString());
		AlertDialog.Builder success = new AlertDialog.Builder(this);
		success.setTitle("Product Found");
		success.setView(updateView);
		success.setPositiveButton("Update",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (updatePriceText.getError() == null) {
							double price = (Double.parseDouble(updatePriceText
									.getText().toString()));
							UpdateProductTask task = new UpdateProductTask(
									found, price);
							task.execute();
							dialog.cancel();
						}

					}
				});
		success.show();

	}

	class Updater extends UpdateDataFGTask {
		public Updater() {
			super(HomeActivity.this);
		}

		@Override
		protected void success() {
			super.success();
			LoadDataFGTask local = new LoadDataFGTask(HomeActivity.this);
			local.execute();
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setPositiveButton("Retry", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dlg, int arg1) {
					nextAction();
					dlg.cancel();
				}

			});
			dlg.show();
		}
	}

	class PrefsTask extends ProgressTask {
		public PrefsTask() {
			super(HomeActivity.this, "Initialising...",
					"Getting your preferences");
		}

		@Override
		protected int retrieve() {
			return PreferencesUtils.loadPreferences(HomeActivity.this);
		}

		@Override
		protected void success() {
			nextAction();
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					nextAction();
					dialog.cancel();
				}
			});
			dlg.show();
		}

	}

	class GetLocationTask extends ProgressTask {
		public GetLocationTask() {
			super(HomeActivity.this, "Pinpointing...",
					"Determining your location");
		}

		@Override
		protected int retrieve() {
			return MapUtils.refreshLocation((LocationManager) HomeActivity.this
					.getSystemService(LOCATION_SERVICE));
		}

		@Override
		protected void success() {
			nextAction();
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					nextAction();
					dialog.cancel();
				}
			});
			dlg.show();
		}

	}

	class FindProductTask extends ProgressTask {

		public FindProductTask() {
			super(HomeActivity.this, "Searching",
					"Locating product, please wait...");
		}

		@Override
		protected void success() {
			updatePrice(ScanUtils.getBranchProduct(), ScanUtils.getProduct());
		}

		@Override
		protected void failure(int code) {
			if (code == ERROR.NOT_FOUND) {
				startActivity(IntentUtils
						.getNewProductIntent(getApplicationContext()));
			} else {
				Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
						code);
				dlg.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				dlg.show();
			}
		}

		@Override
		protected int retrieve() {
			Product p = EntityUtils.retrieveProduct(code);
			if (p == null) {
				return ERROR.NOT_FOUND;
			} else {
				BranchProduct bp = EntityUtils.retrieveBranchProduct(code,
						MapUtils.getUserBranch().getId());
				if (bp == null) {
					return ERROR.NOT_FOUND;
				} else {
					ScanUtils.setProduct(p);
					ScanUtils.setBranchProduct(bp);
				}
				return ERROR.SUCCESS;
			}
		}

	}

	class UpdateProductTask extends ProgressTask {
		BranchProduct bp;

		public UpdateProductTask(BranchProduct bp, double price) {
			super(HomeActivity.this, "Updating", "Updating product price...");
			this.bp = bp;
		}

		@Override
		protected int retrieve() {
			return RPCUtils.updateBranchProduct(bp);
		}

		@Override
		protected void success() {
			startActivity(IntentUtils.getBrowseIntent(HomeActivity.this));
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							updatePrice(bp, ScanUtils.getProduct());
							dialog.cancel();
						}
					});
			dlg.show();
		}
	}
}
