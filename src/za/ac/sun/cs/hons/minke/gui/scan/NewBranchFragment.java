package za.ac.sun.cs.hons.minke.gui.scan;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;

public class NewBranchFragment extends SherlockFragment {
	private AutoCompleteTextView branchBox, storeBox, cityBox;
	private EditText branchText;
	private Province province;
	private City city;
	private Store store;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_branch, container, false);
		initGUI(v);
		return v;
	}

	private void initGUI(View v) {
		branchBox = (AutoCompleteTextView) v.findViewById(R.id.box_branch);
		final ArrayAdapter<Branch> branchAdapter = new ArrayAdapter<Branch>(
				getActivity(), R.layout.listitem_default, EntityUtils.getBranches());
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
		branchText = (EditText) v.findViewById(R.id.text_branch);
		branchText.addTextChangedListener(new TextErrorWatcher(branchText,
				false));
		storeBox = (AutoCompleteTextView) v.findViewById(R.id.text_store);
		final ArrayAdapter<Store> storeAdapter = new ArrayAdapter<Store>(getActivity(),
				R.layout.listitem_default, EntityUtils.getStores());
		storeBox.setAdapter(storeAdapter);
		storeBox.addTextChangedListener(new TextErrorWatcher(storeBox, false));
		storeBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				store = storeAdapter.getItem(position);
			}

		});
		cityBox = (AutoCompleteTextView) v.findViewById(R.id.text_city);
		final ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(getActivity(),
				R.layout.listitem_default, EntityUtils.getCities());
		cityBox.setAdapter(cityAdapter);
		cityBox.addTextChangedListener(new TextErrorWatcher(cityBox, false));
		cityBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				city = cityAdapter.getItem(position);
			}

		});
		Button btnBranch = (Button) v.findViewById(R.id.btn_create_branch);
		btnBranch.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				createLocation();				
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

	

	public void createLocation() {
		if (branchText.getError() != null || storeBox.getError() != null
				|| cityBox.getError() != null) {
			return;
		}
		if (city != null) {
			CreateBranchTask task = new CreateBranchTask(MapUtils.getLocation()
					.getLatitudeE6()/1E6, MapUtils.getLocation().getLongitudeE6()/1E6,
					city, store, branchText.getText().toString());

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
		AlertDialog.Builder location = new AlertDialog.Builder(getActivity());
		location.setTitle("Choose Province");
		location.setSingleChoiceItems(names, 0, new android.content.DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				province = EntityUtils.getProvinces().get(position);
			}
		});
		location.setPositiveButton("Add Branch",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						CreateBranchTask task = new CreateBranchTask(MapUtils
								.getLocation().getLatitudeE6(), MapUtils
								.getLocation().getLongitudeE6(), province,
								store, cityBox.getText().toString(), branchText
										.getText().toString());
						task.execute();
						dialog.cancel();
					}
				});
		location.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						((HomeActivity) getActivity()).getView(VIEW.SCAN, ScanFragment.class.getName());
					}
				});
		location.show();
	}
	class CreateBranchTask extends ProgressTask {
		private City city;
		private double lat, lon;
		private String branchName;
		private Store store;
		private Province province;
		private String cityName;

		private CreateBranchTask(double lat, double lon) {
			super(NewBranchFragment.this.getActivity(), "Adding...",
					"Adding branch to the database");
			this.lat = lat;
			this.lon = lon;
		}

		public CreateBranchTask(double lat, double lon, City city, Store store,
				String branchName) {
			this(lat, lon);
			this.city = city;
			this.branchName = branchName;
			this.store = store;
		}

		public CreateBranchTask(double lat, double lon, Province province,
				Store store, String cityName, String branchName) {
			this(lat, lon);
			this.province = province;
			this.cityName = cityName;
			this.store = store;
			this.branchName = branchName;
		}

		@Override
		protected void success() {
			((HomeActivity) getActivity()).getView(VIEW.SCAN, ScanFragment.class.getName());
		}

		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(NewBranchFragment.this.getActivity(),
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							createLocation();
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((HomeActivity) getActivity()).getView(VIEW.SCAN, ScanFragment.class.getName());
							dialog.cancel();
						}
					});
			dlg.show();
		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.CLIENT;
			}
			if (store == null) {
				if (city == null) {
					return RPCUtils.createBranch(province, lat, lon, cityName,
							storeBox.getText().toString(), branchName);
				} else {
					return RPCUtils.createBranch(city, lat, lon, storeBox
							.getText().toString(), branchName);
				}
			} else if (city == null) {
				return RPCUtils.createBranch(province, store, lat, lon,
						cityName, branchName);
			} else {
				return RPCUtils.createBranch(city, store, lat, lon, branchName);
			}
		}
	}
}
