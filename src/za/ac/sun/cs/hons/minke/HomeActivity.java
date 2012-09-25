package za.ac.sun.cs.hons.minke;

import java.util.Date;

import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.StoreDataTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.Constants;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

	class FindProductTask extends ProgressTask {

		public FindProductTask() {
			super(HomeActivity.this, 1, "Searching",
					"Locating product, please wait...", true);
		}

		@Override
		protected void success() {
			updatePrice(EntityUtils.getScanned());
		}

		@Override
		protected void failure(int code) {
			if (code == Constants.PARSE_ERROR) {
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
		protected int retrieve(int counter) {
			return RPCUtils.getBranchProduct(code, EntityUtils.getUserBranch());

		}

	}

	class UpdateProductTask extends ProgressTask {
		BranchProduct bp;

		public UpdateProductTask(BranchProduct bp) {
			super(HomeActivity.this, 1, "Updating",
					"Updating product price...", true);
			this.bp = bp;
		}

		@Override
		protected int retrieve(int counter) {
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
							updatePrice(bp);
							dialog.cancel();
						}
					});
			dlg.show();
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (IntentUtils.scan()) {
			startScan(EntityUtils.getUserBranch() != null);
		}
		initGUI();

	}

	@Override
	public void onPause() {
		super.onPause();
		StoreDataTask task = new StoreDataTask(this);
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void initGUI() {
		setContentView(R.layout.home);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_home);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getSettingsAction(this));

	}

	/*private void checkPreferences() {
		if (!PreferencesUtils.checkLoaded(this)) {
			startActivity(IntentUtils.getSettingsIntent(this));
		}
		if (!PreferencesUtils.loadPreferences(this)) {
			startActivity(IntentUtils.getSettingsIntent(this));
			PreferencesUtils.loadPreferences(this);
		}
		if (PreferencesUtils.getUpdateFrequency() == Constants.STARTUP) {
			UpdateDataTask task = new UpdateDataTask(this);
			task.execute();
		} else {
			LoadDataTask task = new LoadDataTask(this);
			task.execute();
			if (PreferencesUtils.getUpdateFrequency() != Constants.NEVER) {
				startScheduler();
			}
		}

	}
	
	private void startScheduler() {

		try {
			AlarmManager alarms = (AlarmManager) this
					.getSystemService(ALARM_SERVICE);

			Intent intent = new Intent(getApplicationContext(),
					TimedReceiver.class);
			pendingIntent = PendingIntent.getBroadcast(this,
					1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			long updateInterval = PreferencesUtils.getUpdateInterval();
			if (updateInterval == -1) {
				return;
			}
			alarms.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), updateInterval, pIntent);
			alarms.cancel(pIntent);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/

	public void shop(View view) {
		startActivity(IntentUtils.getShopIntent(getApplicationContext()));
	}

	public void search(View view) {

		startActivity(IntentUtils
				.getLocationSearchIntent(getApplicationContext()));
	}

	private void startScan(boolean userLoc) {
		if (userLoc) {
			scan();
		} else {
			confirmLocation(null);
		}
	}

	private void scan() {
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
							scan();
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

	public void confirmLocation(View view) {
		if (EntityUtils.getBranches() == null) {
			return;
		}
		final int size = Math.min(EntityUtils.getBranches().size(), 5);
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
							EntityUtils.setUserBranch((Branch) EntityUtils
									.getBranches().get(selectedBranch));
							scan();
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
							UpdateProductTask task = new UpdateProductTask(
									found);
							task.execute();
							dialog.cancel();
						}

					}
				});
		success.show();

	}


}
