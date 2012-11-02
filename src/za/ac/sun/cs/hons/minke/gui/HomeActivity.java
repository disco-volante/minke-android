package za.ac.sun.cs.hons.minke.gui;

import java.util.ArrayList;
import java.util.Collections;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.scan.NewBranchFragment;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TabInfo;
import za.ac.sun.cs.hons.minke.gui.utils.TabsAdapter;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.services.IUpdateService;
import za.ac.sun.cs.hons.minke.services.UpdateService;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataFGTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.NAMES;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import za.ac.sun.cs.hons.minke.utils.constants.TIME;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class HomeActivity extends SherlockFragmentActivity {
	TabHost mTabHost;
	TabsAdapter mTabManager;
	TabInfo browse, shop, scan;
	protected int selectedBranch, str;
	public ProgressTask curTask;
	private boolean choosing, updating, errorShowing, downloading, bound;
	private Command cmd;
	private ERROR errorCode;
	private Object[] params;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EntityUtils.getDatePrices(this, 0);
		if (DEBUG.ON) {
			Log.v(TAGS.STATE, "HomeActivity started");
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().build());
		}
		initGUI();
		getSavedState(savedInstanceState);
		if (getLastCustomNonConfigurationInstance() != null) {
			curTask = (ProgressTask) getLastCustomNonConfigurationInstance();

			if (curTask.getStatus().equals(Status.FINISHED)) {
				curTask.cancel(true);
			} else {
				curTask.attach(this);
			}

		}
		requestLocation();
	}

	private void requestLocation() {
		if (!DEBUG.EMULATOR) {
			final LocationManager lm = (LocationManager) getApplication()
					.getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					10 * TIME.MINUTE, 1, new LocationListener() {

						@Override
						public void onLocationChanged(Location loc) {
							if (loc != null) {
								MapUtils.setUserLocation(loc.getLatitude(),
										loc.getLongitude());
								MapUtils.changeCity(getApplicationContext());
							}
						}

						@Override
						public void onProviderDisabled(String arg0) {
							lm.removeUpdates(this);
						}

						@Override
						public void onProviderEnabled(String arg0) {
						}

						@Override
						public void onStatusChanged(String arg0, int arg1,
								Bundle arg2) {
						}

					});
		}
	}

	/**
	 * 
	 * Setup the tabs and the {@link ActionBar}
	 */
	private void initGUI() {
		setContentView(R.layout.activity_home);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabsAdapter(this, mTabHost, R.id.tab_real);

		mTabManager.addTab(
				mTabHost.newTabSpec(VIEW.SCAN).setIndicator(
						getString(R.string.scan)), NAMES.SCAN);
		mTabManager.addTab(
				mTabHost.newTabSpec(VIEW.BROWSE).setIndicator(
						getString(R.string.browse)), NAMES.LOCATION);
		mTabManager.addTab(
				mTabHost.newTabSpec(VIEW.SHOP).setIndicator(
						getString(R.string.shop)), NAMES.SHOP);
	}

	/**
	 * Retrieves saved data from the previous instance's {@link Bundle}.
	 * 
	 * @param savedInstanceState
	 *            the previous instance's Bundle.
	 */
	private void getSavedState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		mTabHost.setCurrentTabByTag(savedInstanceState.getString("TAB"));
		changeTab(savedInstanceState.getString("TAB"),
				savedInstanceState.getString("CLASS"));
		long branchId = savedInstanceState.getLong("USER_BRANCH", -1);
		if (branchId != -1) {
			MapUtils.setUserBranch(EntityUtils.getBranch(this, branchId));

		}
		if (savedInstanceState.getBoolean("CHOOSING")) {
			confirmLocation();
		} else if (savedInstanceState.getBoolean("UPDATING")) {
			updatePrice(ScanUtils.getBranchProduct(), ScanUtils.getProduct());
		}
		errorShowing = savedInstanceState.getBoolean("ERROR_SHOWING", false);
		if (errorShowing) {
			errorCode = (ERROR) savedInstanceState.getSerializable("ERROR");
			str = savedInstanceState.getInt("STRING");
			cmd = getCommand(savedInstanceState.getInt("COMMAND_ID"));
			if (cmd instanceof UpdateProduct) {
				if (params == null || params.length != 2) {
					params = new Object[2];
				}
				params[0] = EntityUtils.getBranchProduct(getApplicationContext(),
						savedInstanceState.getLong("BP_ID"));
				params[1] = EntityUtils.getProduct(getApplicationContext(),
						savedInstanceState.getLong("PRODUCT_ID"));
			}
			showError(cmd, errorCode, str, params);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		if (savedInstanceState == null) {
			return;
		}
		savedInstanceState.putString("TAB", mTabManager.getCurrentTab()
				.getTag());
		savedInstanceState.putString("CLASS", mTabManager.getCurrentTab()
				.getClassName());
		savedInstanceState.putBoolean("CHOOSING", choosing);
		savedInstanceState.putBoolean("UPDATING", updating);
		if (MapUtils.getUserBranch() != null) {
			savedInstanceState.putLong("USER_BRANCH", MapUtils.getUserBranch()
					.getId());
		}
		savedInstanceState.putBoolean("ERROR_SHOWING", errorShowing);
		if (errorShowing) {
			savedInstanceState.putSerializable("ERROR", errorCode);
			savedInstanceState.putInt("STRING", str);
			savedInstanceState.putInt("COMMAND_ID", cmd.getId());
			if (cmd instanceof UpdateProduct) {
				savedInstanceState.putLong("BP_ID",
						((BranchProduct) params[0]).getId());
				savedInstanceState.putLong("PRODUCT_ID",
						((Product) params[1]).getId());
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_info:
			Builder dlg = DialogUtils.getInfoDialog(this);
			dlg.show();
			return true;
		case R.id.menu_item_settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		case R.id.menu_item_refresh:
			update();
			return true;
		case android.R.id.home:
			mTabManager.showParentFragment();
			return true;
		}
		return false;
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (PreferencesUtils.getAnimationLevel() != Constants.NONE) {
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		}
	}

	@Override
	public Object onRetainCustomNonConfigurationInstance() {
		if (curTask == null || curTask.getStatus().equals(Status.FINISHED)) {
			return null;
		}
		curTask.detach();
		return curTask;

	}

	/**
	 * Switches the {@link Activity}'s tabs.
	 * 
	 * @param tag
	 *            the name of the tab to be switched to.
	 * @param className
	 *            the name of the {@link Fragment} to shown in the tab.
	 */
	public void changeTab(String tag, String className) {
		mTabManager.changeTab(tag, className);
	}

	/**
	 * OnClick method which shows the product price histories.
	 * 
	 * @param view
	 */
	public void showHistories(View view) {
		startActivity(IntentUtils.getGraphIntent(getApplicationContext()));
	}

	/**
	 * OnClick method which performs a barcode scan. The user's location is
	 * requested in order to perform an accurate search after the barcode is
	 * scanned. ZXing Barcode Scanner is launched hereafter.
	 * 
	 * @param view
	 */
	public void scan(View view) {
		ScanUtils.setBarCode(0L);
		if (view != null || MapUtils.getUserBranch() == null) {
			confirmLocation();
		} else {
			if (DEBUG.EMULATOR) {
				Intent intent = IntentUtils.getEmulatorIntent();
				onActivityResult(IntentIntegrator.REQUEST_CODE,
						Activity.RESULT_OK, intent);
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
									onActivityResult(Activity.RESULT_CANCELED,
											0, null);
									dialog.cancel();
								}

							});

				}
			}
		}
	}

	/**
	 * Helper method for finding a product asynchronously.
	 */
	private void findProduct() {
		curTask = new FindProductTask(this);
		curTask.execute();
	}

	protected void update() {
		curTask = new Updater(this);
		curTask.execute();
	}

	/**
	 * Helper method for updating a product's price asynchronously.
	 * 
	 * @param found
	 *            the product to be updated.
	 * @param price
	 *            the product's new price.
	 */
	private void updateProduct(final BranchProduct found, final int price) {
		curTask = new UpdateProductTask(this, found, price);
		curTask.execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentIntegrator.REQUEST_CODE
				&& resultCode != Activity.RESULT_CANCELED) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult != null) {
				try {
					if (!ScanUtils.setBarCode(Long.parseLong(scanResult
							.getContents()))) {
						scanFail();
					} else {
						findProduct();
					}
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		} else if (!downloading && resultCode == Activity.RESULT_CANCELED) {
			scanFail();
		}
	}

	private void scanFail() {
		AlertDialog.Builder failed = DialogUtils.getErrorDialog(this,
				ERROR.SCAN);
		failed.setPositiveButton(getString(R.string.retry),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						scan(null);
						dialog.cancel();
					}
				});
		failed.show();
	}

	/**
	 * Allows the user to select the branch they are at from a list.
	 */
	public void confirmLocation() {
		choosing = true;
		selectedBranch = 0;
		final ArrayList<Branch> branches = EntityUtils.getBranches(this);
		if (branches == null || branches.size() == 0) {
			return;
		}
		Collections.sort(branches);
		MapUtils.setUserBranch(branches.get(0));
		final int size = branches.size();
		String[] names = new String[size + 1];
		int i = 0;
		for (Branch b : branches) {
			names[i++] = b.toString();
		}
		names[size] = getString(R.string.other);
		AlertDialog.Builder location = new AlertDialog.Builder(this);
		location.setTitle(getString(R.string.confirm_location));
		location.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				selectedBranch = position;
			}
		});
		location.setPositiveButton(getString(R.string.get_product),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						choosing = false;
						dialog.cancel();
						if (selectedBranch == size) {
							MapUtils.setUserBranch(null);
							editLocation();
						} else {
							MapUtils.setUserBranch(branches.get(selectedBranch));
							scan(null);
						}
					}
				});
		location.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						choosing = false;
						dialog.cancel();
					}
				});
		location.show();

	}

	/**
	 * Switch to the {@link NewBranchFragment} to allow the user to create a new
	 * {@link Branch}.
	 */
	protected void editLocation() {
		if (DEBUG.ON) {
			Log.v("LOCATION", MapUtils.getUserLocation().toString());
		}
		changeTab(VIEW.SCAN, NAMES.BRANCH);
	}

	/**
	 * Allows the user to update the price of an existing product.
	 * 
	 * @param found
	 * @param product
	 */
	private void updatePrice(final BranchProduct found, final Product product) {
		updating = true;
		LayoutInflater factory = LayoutInflater.from(this);
		final View updateView = factory.inflate(R.layout.dialog_update, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.lbl_product);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.text_price);
		if (found != null && found.getDatePrice() != null) {
			updatePriceText.setText(found.getDatePrice()._getFormattedPrice());
		}
		updatePriceText.addTextChangedListener(new TextErrorWatcher(this,
				updatePriceText, true));
		productText.setText(product.toString());
		AlertDialog.Builder success = new AlertDialog.Builder(this);
		success.setTitle(getString(R.string.product_found));
		success.setView(updateView);
		success.setPositiveButton(getString(R.string.update),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						if (updatePriceText.getError() == null) {
							updating = false;
							int price = (int) (Double
									.parseDouble(updatePriceText.getText()
											.toString()) * 100);
							updateProduct(found, price);
							dialog.cancel();
						}
					}
				});
		success.show();

	}

	/**
	 * Overriden in order to allow the back button to function between tabs.
	 */
	@Override
	public void onBackPressed() {
		if (DEBUG.ON) {
			Log.v(TAGS.KEY_PRESS, "BACK PRESSED");
		}
		if (mTabManager == null) {
			super.onBackPressed();
		} else {
			mTabManager.setBackPress(true);
			if (!mTabManager.isStackEmpty()) {
				mTabManager.goBack();
			} else {
				super.onBackPressed();
			}
		}

	}

	/**
	 * Shows an {@link AsyncTask}'s error {@link Dialog}.
	 * 
	 * @param _cmd
	 *            The command to be called should the positive button be
	 *            clicked.
	 * @param _errorCode
	 *            The type of {@link ERROR} to be shown.
	 * @param _str
	 *            The positive button's text.
	 * @param _params
	 *            the {@link Command}'s parameters.
	 */
	public void showError(Command _cmd, ERROR _errorCode, int _str,
			final Object... _params) {
		errorShowing = true;
		cmd = _cmd;
		errorCode = _errorCode;
		str = _str;
		params = _params;
		Builder dlg = DialogUtils.getErrorDialog(this, errorCode);
		if (str != -1) {
			dlg.setPositiveButton(getString(str),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							if (cmd != null) {
								cmd.execute(HomeActivity.this, params);
							}
							dialog.cancel();
							errorShowing = false;
						}
					});
		}
		dlg.show();
	}

	/**
	 * Used to retrieve a {@link Command} by id.
	 * 
	 * @param id
	 *            a Command id.
	 * @return the corresponding Command.
	 */
	public Command getCommand(int id) {
		switch (id) {
		case 0:
			return new CreateProduct();
		case 1:
			return new UpdateProduct();
		case 2:
			return new UpdateData();
		}
		return null;
	}

	/**
	 * {@link AsyncTask} subclass used to wait for the application to
	 * initialised.
	 * 
	 * @author pieter
	 * 
	 */

	/**
	 * {@link AsyncTask} subclass used to find a scanned product.
	 * 
	 * @author pieter
	 * 
	 */
	static class FindProductTask extends ProgressTask {

		public FindProductTask(Activity activity) {
			super(activity, activity.getString(R.string.searching) + "...",
					activity.getString(R.string.searching_product_msg));
		}

		@Override
		protected void success() {
			((HomeActivity) activity).updatePrice(ScanUtils.getBranchProduct(),
					ScanUtils.getProduct());
		}

		@Override
		protected void failure(ERROR code) {
			if (code == ERROR.NOT_FOUND) {
				if (ScanUtils.getProduct() != null) {
					ScanUtils.setBranchProduct(activity.getApplicationContext(), new BranchProduct(0L,
							ScanUtils.getProduct().getId(), MapUtils
									.getUserBranch().getId(), 0L));
					((HomeActivity) activity).updatePrice(
							ScanUtils.getBranchProduct(),
							ScanUtils.getProduct());
				} else {
					((HomeActivity) activity).showError(
							((HomeActivity) activity).new CreateProduct(),
							code, R.string.create_product);
				}

			} else {
				((HomeActivity) activity).showError(null, code, -1);
			}
		}

		@Override
		protected ERROR retrieve() {
			ScanUtils.setProduct(null);
			ScanUtils.setBranchProduct(activity, null);
			Product p = EntityUtils.retrieveProduct(activity.getApplicationContext(),
					ScanUtils.getBarCode());
			if (p == null) {
				if (!PreferencesUtils.checkServer()) {
					return ERROR.NOT_FOUND;
				}
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				p = EntityUtils.retrieveProductServer(activity.getApplicationContext(),
						ScanUtils.getBarCode());
				if (p == null) {
					return ERROR.NOT_FOUND;
				}
			}
			ScanUtils.setProduct(p);
			BranchProduct bp = EntityUtils.retrieveBranchProduct(activity.getApplicationContext(),
					ScanUtils.getBarCode(), MapUtils.getUserBranch().getId());
			if (bp == null) {
				if (!PreferencesUtils.checkServer()) {
					return ERROR.NOT_FOUND;
				}
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				bp = EntityUtils.retrieveBranchProductServer(activity.getApplicationContext(),
						ScanUtils.getBarCode(), MapUtils.getUserBranch()
								.getId());
				if (bp == null) {
					return ERROR.NOT_FOUND;
				}
			}
			ScanUtils.setBranchProduct(activity.getApplicationContext(), bp);
			return ERROR.SUCCESS;
		}

	}

	/**
	 * {@link AsyncTask} subclass used to update a scanned product's price.
	 * 
	 * @author pieter
	 * 
	 */
	static class UpdateProductTask extends ProgressTask {
		BranchProduct bp;
		private int price;

		public UpdateProductTask(Activity activity, BranchProduct _bp, int _price) {
			super(activity, activity.getString(R.string.updating) + "...",
					activity.getString(R.string.updating_product_msg));
			bp = _bp;
			price = _price;
		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.CLIENT;
			}
			if (RPCUtils.startServer() == ERROR.SERVER) {
				return ERROR.SERVER;
			}
			return RPCUtils.updateBranchProduct(activity.getApplicationContext(), bp, price);
		}

		@Override
		protected void success() {
			((HomeActivity) activity).changeTab(VIEW.SCAN, NAMES.BROWSE);
		}

		@Override
		protected void failure(ERROR error_code) {
			((HomeActivity) activity).showError(
					((HomeActivity) activity).new UpdateProduct(), error_code,
					R.string.retry, bp, ScanUtils.getProduct());
		}
	}

	static class Updater extends UpdateDataFGTask {
		public Updater(Activity activity) {
			super(activity);
		}

		@Override
		protected void success() {
			super.success();
			if (PreferencesUtils.isFirstTime()) {
				PreferencesUtils.changeFirstTime(activity, false);
			}
		}

		@Override
		protected void failure(final ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
			dlg.setPositiveButton(activity.getString(R.string.retry),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dlg, int arg1) {
							((HomeActivity) activity).showError(
									((HomeActivity) activity).new UpdateData(),
									error_code, R.string.retry);
							dlg.cancel();
						}

					});
			dlg.show();

		}
	}

	/**
	 * Interface to facilitate passing methods as parameters.
	 * 
	 * @author pieter
	 * 
	 */
	public interface Command {
		public int getId();

		public void execute(HomeActivity activity, Object... data);
	}

	/**
	 * Allows a user to create a new product if it cannot be found.
	 * 
	 * @author pieter
	 * 
	 */
	public class CreateProduct implements Command {
		@Override
		public void execute(HomeActivity activity, Object... data) {
			activity.changeTab(VIEW.SCAN, NAMES.BRANCHPRODUCT);
		}

		@Override
		public int getId() {
			return 0;
		}

	}

	/**
	 * Allows a user to update a product's price.
	 * 
	 * @author pieter
	 * 
	 */
	public class UpdateProduct implements Command {

		@Override
		public void execute(HomeActivity activity, Object... data) {
			activity.updatePrice((BranchProduct) data[0], (Product) data[1]);
		}

		@Override
		public int getId() {
			return 1;
		}

	}

	/**
	 * Allows a user to update data.
	 * 
	 * @author pieter
	 * 
	 */
	public class UpdateData implements Command {

		@Override
		public void execute(HomeActivity activity, Object... data) {
			activity.update();
		}

		@Override
		public int getId() {
			return 2;
		}

	}

	/**
	 * Not Used
	 */

	/**
	 * Connects to and launches the @{link UpdateService}.
	 */
	private ServiceConnection uConnection = new ServiceConnection() {
		private IUpdateService updateService;

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			try {
				updateService = IUpdateService.Stub.asInterface(service);
				if (updateService.iStart()) {
					Toast.makeText(HomeActivity.this,
							getString(R.string.update_success_msg),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(HomeActivity.this,
							getString(R.string.err_server_msg),
							Toast.LENGTH_SHORT).show();
				}
				Log.v(TAGS.STATE, "Update service initialised");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			updateService = null;
		}
	};

	/**
	 * Starts and binds to the @{link UpdateService}.
	 */
	@SuppressWarnings("unused")
	private void initService() {
		Intent i = new Intent(this, UpdateService.class);
		startService(i);
		bindService(i, uConnection, Context.BIND_AUTO_CREATE);
		bound = true;
	}

	/**
	 * Disconnects from the @{link UpdateService}.
	 */
	@SuppressWarnings("unused")
	private void releaseService() {
		if (uConnection != null && bound) {
			unbindService(uConnection);
			uConnection = null;
			bound = false;
		}
	}

}
