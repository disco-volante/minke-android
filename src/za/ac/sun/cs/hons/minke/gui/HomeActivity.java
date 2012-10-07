package za.ac.sun.cs.hons.minke.gui;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.assets.IntentIntegrator;
import za.ac.sun.cs.hons.minke.assets.IntentResult;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.browse.BrowseFragment;
import za.ac.sun.cs.hons.minke.gui.browse.LocationSearchFragment;
import za.ac.sun.cs.hons.minke.gui.browse.ProductSearchFragment;
import za.ac.sun.cs.hons.minke.gui.scan.NewBranchFragment;
import za.ac.sun.cs.hons.minke.gui.scan.NewProductFragment;
import za.ac.sun.cs.hons.minke.gui.scan.ScanFragment;
import za.ac.sun.cs.hons.minke.gui.shop.ShopFragment;
import za.ac.sun.cs.hons.minke.gui.shop.StoreFragment;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TabInfo;
import za.ac.sun.cs.hons.minke.gui.utils.TabsAdapter;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.LoadDataFGTask;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataFGTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.Activity;
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
	public long code;
	protected boolean downloading;
	protected int selectedBranch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock_Light);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new TabsAdapter(this, mTabHost, R.id.tab_real);

		mTabManager.addTab(mTabHost.newTabSpec(VIEW.SCAN).setIndicator("Scan"),
				ScanFragment.class.getName());
		mTabManager.addTab(
				mTabHost.newTabSpec(VIEW.BROWSE).setIndicator("Browse"),
				LocationSearchFragment.class.getName());
		mTabManager.addTab(mTabHost.newTabSpec(VIEW.SHOP).setIndicator("Shop"),
				ShopFragment.class.getName());

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString(VIEW.SCAN));
		}
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
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_info:
			displayInfo();
			return true;
		case R.id.menu_item_settings:
			displaySettings();
			return true;
		}
		return false;
	}

	private void displaySettings() {
		startActivity(IntentUtils.getSettingsIntent(this));
	}

	private void displayInfo() {
		Builder dlg = DialogUtils.getInfoDialog(this);
		dlg.show();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	public void getView(String tag, String className) {
		mTabManager.changeTab(tag, className);
	}

	public void getProductSearch(View view) {
		getView(VIEW.BROWSE, ProductSearchFragment.class.getName());
	}

	public void getProducts(View view) {
		SearchTask task = new SearchTask();
		task.execute();
	}

	public void showHistories(View view) {
		startActivity(IntentUtils.getGraphIntent(this));
	}

	public void setBranch(View view) {
		getView(VIEW.SCAN, ScanFragment.class.getName());
	}

	public void findStores(View view) {
		FindBranchesTask task = new FindBranchesTask();
		task.execute();
	}

	public void scan(View view) {
		if (MapUtils.getUserBranch() == null) {
			confirmLocation();
		} else {
			if (Debug.EMULATOR) {
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

	public void showDirections(View view) {
		String[] names = new String[ShopUtils.getBranches().size()];
		MapUtils.setBranches(ShopUtils.getBranches());
		MapUtils.setDestination(0);
		int i = 0;
		for (Branch b : ShopUtils.getBranches()) {
			names[i++] = b.toString();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Directions to?");
		builder.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				MapUtils.setDestination(position);
			}
		});
		builder.setPositiveButton("View Map",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(IntentUtils
								.getMapIntent(HomeActivity.this));
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

	protected void nextAction() {
		if (!PreferencesUtils.isLoaded()) {
			PrefsTask prefTask = new PrefsTask();
			prefTask.execute();
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == IntentIntegrator.REQUEST_CODE
				&& resultCode != Activity.RESULT_CANCELED) {
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
		} else if (!downloading && resultCode == Activity.RESULT_CANCELED) {
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
		getView(VIEW.SCAN, NewBranchFragment.class.getName());
	}

	private void updatePrice(final BranchProduct found, final Product product) {

		LayoutInflater factory = LayoutInflater.from(this);
		final View updateView = factory.inflate(R.layout.dialog_update, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.lbl_product);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.text_price);
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

	@Override
	public void onBackPressed() {
		Log.v(TAGS.KEY_PRESS, "BACK PRESSED");
		mTabManager.setBackPress(true);
		if (!mTabManager.isStackEmpty()) {
			mTabManager.goBack();
		} else {
			super.onBackPressed();
		}
	}

	class SearchTask extends ProgressTask {

		public SearchTask() {
			super(HomeActivity.this, "Searching...", "Searching for products");
		}

		@Override
		protected void success() {
			BrowseUtils.setBranchProducts(SearchUtils.getSearched());
			BrowseUtils.setStoreBrowse(false);
			getView(VIEW.BROWSE, BrowseFragment.class.getName());

		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							getProducts(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			return EntityUtils.retrieveBranchProducts(SearchUtils
					.isProductsActive());
		}
	}

	class FindBranchesTask extends ProgressTask {

		public FindBranchesTask() {
			super(HomeActivity.this, "Searching", "Searching for branches...");
		}

		@Override
		protected void success() {
			getView(VIEW.SHOP, StoreFragment.class.getName());

		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							findStores(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			dlg.show();
		}

		@Override
		protected ERROR retrieve() {
			return EntityUtils.retrieveBranches(ShopUtils
					.getAddedProducts(false));
		}
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
		protected void failure(ERROR error_code) {
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
		protected ERROR retrieve() {
			return PreferencesUtils.loadPreferences(HomeActivity.this);
		}

		@Override
		protected void success() {
			nextAction();
		}

		@Override
		protected void failure(ERROR error_code) {
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
		protected ERROR retrieve() {
			return MapUtils.refreshLocation((LocationManager) HomeActivity.this
					.getSystemService(Activity.LOCATION_SERVICE));
		}

		@Override
		protected void success() {
			nextAction();
		}

		@Override
		protected void failure(ERROR error_code) {
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
		protected void failure(ERROR code) {
			if (code == ERROR.NOT_FOUND) {
				Builder dlg = DialogUtils.getErrorDialog(HomeActivity.this,
						code);
				dlg.setPositiveButton("Create",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								getView(VIEW.SCAN,
										NewProductFragment.class.getName());
								dialog.cancel();
							}
						});
				dlg.show();
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
		protected ERROR retrieve() {
			Product p = EntityUtils.retrieveProduct(code);
			if (p == null) {
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				p = EntityUtils.retrieveProductServer(code);
				if (p == null) {
					return ERROR.NOT_FOUND;
				}
			}
			BranchProduct bp = EntityUtils.retrieveBranchProduct(code, MapUtils
					.getUserBranch().getId());
			if (bp == null) {
				if (!isNetworkAvailable()) {
					return ERROR.NOT_FOUND;
				}
				bp = EntityUtils.retrieveBranchProductServer(code, MapUtils
						.getUserBranch().getId());
				if (bp == null) {
					return ERROR.NOT_FOUND;
				}
			}
			ScanUtils.setProduct(p);
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		}

	}

	class UpdateProductTask extends ProgressTask {
		BranchProduct bp;

		public UpdateProductTask(BranchProduct bp, double price) {
			super(HomeActivity.this, "Updating", "Updating product price...");
			this.bp = bp;
		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.CLIENT;
			}
			return RPCUtils.updateBranchProduct(bp);
		}

		@Override
		protected void success() {
			getView(VIEW.SCAN, BrowseFragment.class.getName());
		}

		@Override
		protected void failure(ERROR error_code) {
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
