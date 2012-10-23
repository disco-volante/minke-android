package za.ac.sun.cs.hons.minke.gui;

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

public class HomeActivity extends SherlockFragmentActivity {
	TabHost mTabHost;
	TabsAdapter mTabManager;
	TabInfo browse, shop, scan;
	protected boolean downloading;
	protected int selectedBranch;
	public ProgressTask curTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG.ON) {
			Log.v(TAGS.STATE, "started");
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().build());
		}
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
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("TAB"));
			changeTab(savedInstanceState.getString("TAB"),
					savedInstanceState.getString("CLASS"));
		} 
		if(getLastCustomNonConfigurationInstance() != null){
			curTask = (ProgressTask) getLastCustomNonConfigurationInstance();
			if(curTask.getStatus().equals(ProgressTask.Status.FINISHED)){
				curTask.cancel(true);
			}
			else{
				curTask.attach(this);
			}
		}else if(!EntityUtils.isLoaded()){
			curTask = new WaitTask(this);
			curTask.execute();
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
		savedInstanceState.putString("TAB", mTabManager.getCurrentTab()
				.getTag());
		savedInstanceState.putString("CLASS", mTabManager.getCurrentTab()
				.getClassName());
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
	public Object onRetainCustomNonConfigurationInstance(){
		if(curTask == null || curTask.getStatus().equals(ProgressTask.Status.FINISHED)){
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

	

	private void loadData() {
		curTask = new LoadDataFGTask(this);
		curTask.execute();
	}

	private void findProduct() {
		curTask = new FindProductTask(this);
		curTask.execute();
	}

	private void updateProduct(final BranchProduct found, final int price) {
		curTask = new UpdateProductTask(this,found, price);
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
		selectedBranch = 0;
		if (EntityUtils.getBranches() == null) {
			return;
		}
		final int size = EntityUtils.getBranches().size();
		String[] names = new String[size + 1];
		int i = 0;
		for (Branch b : EntityUtils.getBranches()) {
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
						dialog.cancel();
						if (selectedBranch == size) {
							MapUtils.setUserBranch(null);
							editLocation();
						} else {
							MapUtils.setUserBranch(EntityUtils.getBranches()
									.get(selectedBranch));
							scan(null);
						}
					}
				});
		location.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
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
			((HomeActivity) activity).loadData();
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity,
					error_code);
			dlg.setPositiveButton(activity.getString(R.string.retry),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dlg, int arg1) {
							((HomeActivity) activity).update();
							dlg.cancel();
						}

					});
			dlg.show();

		}
	}

	static class WaitTask extends ProgressTask {
		public WaitTask(Activity activity) {
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
			} else {
				((HomeActivity) activity).loadData();
			}
		}

		@Override
		protected void failure(ERROR error_code) {
			DialogUtils.getErrorDialog(activity, error_code).show();
		}

	}

	static class FindProductTask extends ProgressTask {

		public FindProductTask(Activity activity) {
			super(activity, activity.getString(R.string.searching) + "...",
					activity.getString(R.string.searching_product_msg));
		}

		@Override
		protected void success() {
			((HomeActivity) activity).updatePrice(ScanUtils.getBranchProduct(), ScanUtils.getProduct());
		}

		@Override
		protected void failure(ERROR code) {
			if (code == ERROR.NOT_FOUND) {
				if (ScanUtils.getProduct() != null) {
					ScanUtils.setBranchProduct(new BranchProduct(0L, ScanUtils
							.getProduct().getId(), MapUtils.getUserBranch()
							.getId(), 0L));
					((HomeActivity) activity).updatePrice(ScanUtils.getBranchProduct(),
							ScanUtils.getProduct());
				} else {
					Builder dlg = DialogUtils.getErrorDialog(activity,
							code);
					dlg.setPositiveButton(activity.getString(R.string.create_product),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((HomeActivity) activity).changeTab(VIEW.SCAN, NAMES.BRANCHPRODUCT);
									dialog.cancel();
								}
							});
					dlg.show();
				}

			} else {
				Builder dlg = DialogUtils.getErrorDialog(activity,
						code);
				dlg.setPositiveButton(activity.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				dlg.show();

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
			Builder dlg = DialogUtils.getErrorDialog(activity,
					error_code);
			dlg.setPositiveButton(activity.getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((HomeActivity) activity).updatePrice(bp, ScanUtils.getProduct());
							dialog.cancel();
						}
					});
			dlg.show();

		}
	}

}
