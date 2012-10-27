package za.ac.sun.cs.hons.minke.gui;

import java.util.ArrayList;
import java.util.Collections;

import za.ac.sun.cs.hons.minke.MinkeApplication;
import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TabInfo;
import za.ac.sun.cs.hons.minke.gui.utils.TabsAdapter;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataBGTask;
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
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class HomeActivity extends SherlockFragmentActivity {
	TabHost mTabHost;
	TabsAdapter mTabManager;
	TabInfo browse, shop, scan;
	protected int selectedBranch, str;
	public ProgressTask curTask;
	public UpdateDataBGTask update;
	private boolean choosing, updating, errorShowing, downloading;
	private DialogErrorCommand cmd;
	private ERROR errorCode;
	private Object[] params;
	private MenuItem refreshItem;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG.ON) {
			Log.v(TAGS.STATE, "started");
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().build());
		}
		initGUI();
		if (!EntityUtils.isLoaded()) {
			curTask = new WaitTask(this,
					getLastCustomNonConfigurationInstance(), savedInstanceState) {
				@Override
				public void onPostExecute(Void result) {
					super.onPostExecute(result);
					getSavedState(savedInstanceState);
				}
			};
			curTask.execute();

		} else {
			getSavedState(savedInstanceState);
			if (getLastCustomNonConfigurationInstance() != null) {
				curTask = (ProgressTask) getLastCustomNonConfigurationInstance();
				if (curTask.getStatus().equals(ProgressTask.Status.FINISHED)) {
					curTask.cancel(true);
				} else {
					curTask.attach(this);
				}
			}
		}
	}

	private void initGUI() {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
		setRefreshing(update != null && !update.getStatus().equals(Status.FINISHED));
	}

	private void setRefreshing(boolean refreshing) {
		setProgressBarIndeterminateVisibility(refreshing);
	    if(refreshItem != null){
	    	refreshItem.setVisible(!refreshing);
	    }
			}

	private void getSavedState(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			return;
		}
		mTabHost.setCurrentTabByTag(savedInstanceState.getString("TAB"));
		changeTab(savedInstanceState.getString("TAB"),
				savedInstanceState.getString("CLASS"));
		long branchId = savedInstanceState.getLong("USER_BRANCH", -1);
		if (branchId != -1) {
			MapUtils.setUserBranch(EntityUtils.getBranch(branchId));

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
			if (cmd instanceof UpdateProductError) {
				if (params == null || params.length != 2) {
					params = new Object[2];
				}
				params[0] = EntityUtils.getBranchProduct(savedInstanceState
						.getLong("BP_ID"));
				params[1] = EntityUtils.getProduct(savedInstanceState
						.getLong("PRODUCT_ID"));
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
			if (cmd instanceof UpdateProductError) {
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
			refreshItem = item;
			backgroundUpdate();
			return true;
		case android.R.id.home:
			mTabManager.showParentFragment();
			return true;
		}
		return false;
	}

	private void backgroundUpdate() {
		setRefreshing(Boolean.TRUE);
		update = new UpdateDataBGTask(this){
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				setRefreshing(Boolean.FALSE);
			}
		};
		update.execute();
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
		if (curTask == null
				|| curTask.getStatus().equals(ProgressTask.Status.FINISHED)) {
			return null;
		}
		curTask.detach();
		return curTask;

	}

	public void changeTab(String tag, String className) {
		mTabManager.changeTab(tag, className);
	}

	public void showHistories(View view) {
		startActivity(IntentUtils.getGraphIntent(this));
	}

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

	private void findProduct() {
		curTask = new FindProductTask(this);
		curTask.execute();
	}

	private void updateProduct(final BranchProduct found, final int price) {
		curTask = new UpdateProductTask(this, found, price);
		curTask.execute();
	}

	private void update() {
		curTask = new Updater(this);
		curTask.execute();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentIntegrator.REQUEST_CODE
				&& resultCode != Activity.RESULT_CANCELED) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult != null) {
				try {
					ScanUtils.setBarCode(Long.parseLong(scanResult
							.getContents()));
					findProduct();
					return;
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}
		} else if (!downloading && resultCode == Activity.RESULT_CANCELED) {
			AlertDialog.Builder failed = DialogUtils.getErrorDialog(this,
					ERROR.SCAN);
			failed.setPositiveButton(getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							scan(null);
							dialog.cancel();
						}
					});
			failed.show();
		}
	}

	public void confirmLocation() {
		choosing = true;
		selectedBranch = 0;
		final ArrayList<Branch> branches = EntityUtils.getBranches();
		if (branches == null) {
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
					public void onClick(DialogInterface dialog, int id) {
						choosing = false;
						dialog.cancel();
					}
				});
		location.show();

	}

	protected void editLocation() {
		if (DEBUG.ON) {
			Log.v("LOCATION", MapUtils.getUserLocation().toString());
		}
		changeTab(VIEW.SCAN, NAMES.BRANCH);
	}

	private void updatePrice(final BranchProduct found, final Product product) {
		updating = true;
		LayoutInflater factory = LayoutInflater.from(this);
		final View updateView = factory.inflate(R.layout.dialog_update, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.lbl_product);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.text_price);
		updatePriceText.addTextChangedListener(new TextErrorWatcher(this,
				updatePriceText, true));
		productText.setText(product.toString());
		AlertDialog.Builder success = new AlertDialog.Builder(this);
		success.setTitle(getString(R.string.product_found));
		success.setView(updateView);
		success.setPositiveButton(getString(R.string.update),
				new DialogInterface.OnClickListener() {
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

	public void showError(DialogErrorCommand _cmd, ERROR _errorCode, int _str,
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

	public DialogErrorCommand getCommand(int id) {
		switch (id) {
		case 0:
			return new FindError();
		case 1:
			return new UpdateProductError();
		case 2:
			return new UpdateDataError();
		}
		return null;
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
		protected void failure(ERROR error_code) {
			((HomeActivity) activity).showError(
					((HomeActivity) activity).new UpdateDataError(),
					error_code, R.string.retry);
		}
	}

	static class WaitTask extends ProgressTask {
		public WaitTask(Activity activity, Object object,
				Bundle savedInstanceState) {
			super(activity, activity.getString(R.string.initialising) + "...",
					activity.getString(R.string.initialising_msg));
		}

		@Override
		protected ERROR retrieve() {
			while (!MinkeApplication.loaded()) {
				try {
					Thread.sleep(100, 10);
				} catch (InterruptedException e) {
					return ERROR.PARSE;
				}
			}
			return ERROR.SUCCESS;
		}

		@Override
		protected void success() {
			if (PreferencesUtils.isFirstTime()
					|| PreferencesUtils.getUpdateFrequency() == Constants.STARTUP) {
				((HomeActivity) activity).update();
			}
		}

		@Override
		protected void failure(ERROR error_code) {
			((HomeActivity) activity).showError(null, error_code, -1);
		}

	}

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
					ScanUtils.setBranchProduct(new BranchProduct(0L, ScanUtils
							.getProduct().getId(), MapUtils.getUserBranch()
							.getId(), 0L));
					((HomeActivity) activity).updatePrice(
							ScanUtils.getBranchProduct(),
							ScanUtils.getProduct());
				} else {
					((HomeActivity) activity).showError(
							((HomeActivity) activity).new FindError(), code,
							R.string.create_product);
				}

			} else {
				((HomeActivity) activity).showError(null, code, -1);
			}
		}

		@Override
		protected ERROR retrieve() {
			ScanUtils.setProduct(null);
			ScanUtils.setBranchProduct(null);
			Product p = EntityUtils.retrieveProduct(ScanUtils.getBarCode());
			if (p == null) {
				if (!PreferencesUtils.checkServer()) {
					return ERROR.NOT_FOUND;
				}
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				p = EntityUtils.retrieveProductServer(ScanUtils.getBarCode());
				if (p == null) {
					return ERROR.NOT_FOUND;
				}
			}
			ScanUtils.setProduct(p);
			BranchProduct bp = EntityUtils.retrieveBranchProduct(
					ScanUtils.getBarCode(), MapUtils.getUserBranch().getId());
			if (bp == null) {
				if (!PreferencesUtils.checkServer()) {
					return ERROR.NOT_FOUND;
				}
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				bp = EntityUtils.retrieveBranchProductServer(ScanUtils
						.getBarCode(), MapUtils.getUserBranch().getId());
				if (bp == null) {
					return ERROR.NOT_FOUND;
				}
			}
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		}

	}

	static class UpdateProductTask extends ProgressTask {
		BranchProduct bp;
		private int price;

		public UpdateProductTask(Activity activity, BranchProduct bp, int price) {
			super(activity, activity.getString(R.string.updating) + "...",
					activity.getString(R.string.updating_product_msg));
			this.bp = bp;
			this.price = price;
		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.CLIENT;
			}
			if (RPCUtils.startServer() == ERROR.SERVER) {
				return ERROR.SERVER;
			}
			return RPCUtils.updateBranchProduct(bp, price);
		}

		@Override
		protected void success() {
			((HomeActivity) activity).changeTab(VIEW.SCAN, NAMES.BROWSE);
		}

		@Override
		protected void failure(ERROR error_code) {
			((HomeActivity) activity).showError(
					((HomeActivity) activity).new UpdateProductError(),
					error_code, R.string.retry, bp, ScanUtils.getProduct());
		}
	}

	public interface DialogErrorCommand {
		public int getId();

		public void execute(HomeActivity activity, Object... data);
	}

	public class FindError implements DialogErrorCommand {
		@Override
		public void execute(HomeActivity activity, Object... data) {
			activity.changeTab(VIEW.SCAN, NAMES.BRANCHPRODUCT);
		}

		@Override
		public int getId() {
			return 0;
		}

	}

	public class UpdateProductError implements DialogErrorCommand {

		@Override
		public void execute(HomeActivity activity, Object... data) {
			((HomeActivity) activity).updatePrice((BranchProduct) data[0],
					(Product) data[1]);
		}

		@Override
		public int getId() {
			return 1;
		}

	}

	public class UpdateDataError implements DialogErrorCommand {

		@Override
		public void execute(HomeActivity activity, Object... data) {
			((HomeActivity) activity).update();
		}

		@Override
		public int getId() {
			return 2;
		}

	}

}
