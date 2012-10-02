package za.ac.sun.cs.hons.minke.gui.create;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;


public class NewBranchActivity extends Activity {
	private AutoCompleteTextView branchBox, storeBox, cityBox;
	private EditText branchText;
	private Province province;
	private City city;
	private Store store;

	class CreateBranchTask extends ProgressTask {
		private City city;
		private int lat, lon;
		private String branchName;
		private Store store;
		private Province province;
		private String cityName;

		private CreateBranchTask(int lat, int lon) {
			super(NewBranchActivity.this, "Adding...",
					"Adding branch to the database");
			this.lat = lat;
			this.lon = lon;
		}
	

		public CreateBranchTask(int lat, int lon, City city, Store store,
				String branchName) {
			this(lat, lon);
			this.city = city;
			this.branchName = branchName;
			this.store = store;
		}

		public CreateBranchTask(int lat, int lon, Province province,
				Store store, String cityName, String branchName) {
			this(lat, lon);
			this.province = province;
			this.cityName = cityName;
			this.store = store;
			this.branchName = branchName;
		}

		@Override
		protected void success() {
			startActivity(IntentUtils.getScanIntent(NewBranchActivity.this));
		}

		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(NewBranchActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							createLocation(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(IntentUtils
									.getHomeIntent(NewBranchActivity.this));
							dialog.cancel();
						}
					});
			dlg.show();
		}

		@Override
		protected int retrieve() {
			if (store == null) {
				if (city == null) {
					return RPCUtils.createBranch(province, lat, lon, cityName,
							storeBox.getText().toString(), branchName);
				} else {
					return RPCUtils.createBranch(city, lat, lon, storeBox.getText().toString(),
							branchName);
				}
			} else if (city == null) {
				return RPCUtils.createBranch(province, store, lat, lon,
						cityName, branchName);
			} else {
				return RPCUtils.createBranch(city, store, lat, lon, branchName);
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();

	}


	private void initGUI() {
		setContentView(R.layout.new_branch);

		branchBox = (AutoCompleteTextView) findViewById(R.id.branchBox);
		final ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<Branch>(
				this, R.layout.dropdown_item, EntityUtils.getBranches());
		branchBox.setAdapter(branchAdapter);
		branchBox
				.addTextChangedListener(new TextErrorWatcher(branchBox, false));
		branchBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MapUtils.setUserBranch(branchAdapter.getItem(position));
			}

		});
		branchText = (EditText) findViewById(R.id.branchText);
		branchText.addTextChangedListener(new TextErrorWatcher(branchText,
				false));
		storeBox = (AutoCompleteTextView) findViewById(R.id.storeBox);
		final ArrayAdapter<Store> storeAdapter = new ArrayAdapter<Store>(this,
				R.layout.dropdown_item, EntityUtils.getStores());
		storeBox.setAdapter(storeAdapter);
		storeBox.addTextChangedListener(new TextErrorWatcher(storeBox, false));
		storeBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				store = storeAdapter.getItem(position);
			}

		});
		cityBox = (AutoCompleteTextView) findViewById(R.id.cityBox);
		final ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				R.layout.dropdown_item, EntityUtils.getCities());
		cityBox.setAdapter(cityAdapter);
		cityBox.addTextChangedListener(new TextErrorWatcher(cityBox, false));
		cityBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				city = cityAdapter.getItem(position);
			}

		});
		clear();
	}

	private void clear() {
		branchBox.setText("");
		branchText.setText("");
		storeBox.setText("");
		cityBox.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu2, menu);
		return true;
	}


	public void setBranch(View view) {
		startActivity(IntentUtils.getScanIntent(this));
	}

	public void createLocation(View view) {
		if (branchText.getError() != null || storeBox.getError() != null
				|| cityBox.getError() != null) {
			return;
		}
		if (city != null) {
			CreateBranchTask task = new CreateBranchTask(MapUtils.getLocation()
						.getLatitudeE6(), MapUtils.getLocation()
						.getLongitudeE6(), city, store, branchText.getText()
						.toString());
			
			task.execute();

		} else {
			requestLocations();
		}
	}

	private void requestLocations() {
		if (EntityUtils.getProvinces() == null) {
			return;
		}
		final int size = Math.min(EntityUtils.getProvinces().size(), 10);
		final String[] names = new String[size];
		int i = 0;
		for (Province p : EntityUtils.getProvinces()) {
			if (i == size) {
				break;
			}
			names[i++] = p.toString();
		}
		AlertDialog.Builder location = new AlertDialog.Builder(this);
		location.setTitle("Choose Province");
		location.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				province = EntityUtils.getProvinces().get(position);
			}
		});
		location.setPositiveButton("Add Branch",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						CreateBranchTask task = new CreateBranchTask(MapUtils.getLocation()
									.getLatitudeE6(), MapUtils.getLocation()
									.getLongitudeE6(), province, store, cityBox
									.getText().toString(), branchText.getText()
									.toString());
						task.execute();
						dialog.cancel();
					}
				});
		location.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(IntentUtils
								.getHomeIntent(NewBranchActivity.this));
					}
				});
		location.show();
	}
}
