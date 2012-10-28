package za.ac.sun.cs.hons.minke.gui.scan;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;

public class NewBranchFragment extends SherlockFragment {
	private AutoCompleteTextView storeBox, cityBox;
	private EditText branchText;
	private TextErrorWatcher cityWatcher, storeWatcher, branchWatcher;
	private boolean watchers;
	private ArrayAdapter<Store> storeAdapter;
	private ArrayAdapter<City> cityAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View v = inflater.inflate(R.layout.fragment_new_branch, container,
				false);
		initGUI(v);
		return v;
	}

	@Override
	public void onPause() {
		super.onPause();
		addWatchers();
		if (branchText.getError() == null) {
			ScanUtils.branchName = branchText.getText().toString();
		}
		if (storeBox.getError() == null) {
			ScanUtils.storeName = storeBox.getText().toString();
		}
		if (cityBox.getError() == null) {
			ScanUtils.cityName = cityBox.getText().toString();
		}
	}

	private void addWatchers() {
		if (!watchers) {
			watchers = true;
			cityWatcher = new TextErrorWatcher(getActivity(), cityBox, false);
			storeWatcher = new TextErrorWatcher(getActivity(), storeBox, false);
			branchWatcher = new TextErrorWatcher(getActivity(), branchText,
					false);
			cityBox.addTextChangedListener(cityWatcher);
			storeBox.addTextChangedListener(storeWatcher);
			branchText.addTextChangedListener(branchWatcher);
		} else {
			cityWatcher.afterTextChanged(cityBox.getEditableText());
			storeWatcher.afterTextChanged(storeBox.getEditableText());
			branchWatcher.afterTextChanged(branchText.getEditableText());
		}
	}

	private void initGUI(View v) {
		branchText = (EditText) v.findViewById(R.id.text_branch);
		storeBox = (AutoCompleteTextView) v.findViewById(R.id.text_store);
		storeAdapter = new ArrayAdapter<Store>(getActivity(),
				R.layout.listitem_default, EntityUtils.getStores());
		storeBox.setAdapter(storeAdapter);
		storeBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ScanUtils.store = storeAdapter.getItem(position);
			}

		});
		storeBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				storeBox.dismissDropDown();
				return false;
			}

		});
		cityBox = (AutoCompleteTextView) v.findViewById(R.id.text_city);
		cityAdapter = new ArrayAdapter<City>(getActivity(),
				R.layout.listitem_default, EntityUtils.getCities());
		cityBox.setAdapter(cityAdapter);
		cityBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ScanUtils.city = cityAdapter.getItem(position);
			}

		});
		cityBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				cityBox.dismissDropDown();
				return false;
			}

		});
		Button btnBranch = (Button) v.findViewById(R.id.btn_create_branch);
		btnBranch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkInput();
			}

		});
		addInput();
	}

	private void addInput() {
		if (ScanUtils.cityName != null && !ScanUtils.cityName.equals("")) {
			cityBox.setText(ScanUtils.cityName);
		}
		if (ScanUtils.storeName != null && !ScanUtils.storeName.equals("")) {
			storeBox.setText(ScanUtils.storeName);
		}
		if (ScanUtils.branchName != null && !ScanUtils.branchName.equals("")) {
			branchText.setText(ScanUtils.branchName);
		}
	}

	private void checkInput() {
		addWatchers();
		if (branchText.getError() != null || storeBox.getError() != null
				|| cityBox.getError() != null) {
			return;
		}
		createLocation();
	}

	public void createLocation() {
		ScanUtils.branchName = branchText.getText().toString();
		ScanUtils.cityName = cityBox.getText().toString();
		ScanUtils.storeName = storeBox.getText().toString();
		if (!ScanUtils.city.toString().equals(ScanUtils.cityName)) {
			ScanUtils.city = null;
		}
		if (!ScanUtils.store.toString().equals(ScanUtils.storeName)) {
			ScanUtils.store = null;
		}
		clear();
		if (ScanUtils.city != null) {
			createBranch();

		} else {
			requestProvince();
		}
	}

	private void clear() {
		watchers = false;
		cityBox.removeTextChangedListener(cityWatcher);
		storeBox.removeTextChangedListener(storeWatcher);
		branchText.removeTextChangedListener(branchWatcher);
		branchText.setText("");
		storeBox.setText("");
		cityBox.setText("");
	}

	private void requestProvince() {
		final ArrayList<Province> provinces = EntityUtils.getProvinces();
		if (provinces == null) {
			return;
		}
		ScanUtils.province = provinces.get(0);
		final int size = Math.min(provinces.size(), 10);
		final String[] names = new String[size];
		int i = 0;
		for (Province p : provinces) {
			if (i == size) {
				break;
			}
			names[i++] = p.toString();
		}
		AlertDialog.Builder location = new AlertDialog.Builder(getActivity());
		location.setTitle(NewBranchFragment.this
				.getString(R.string.str_province));
		location.setSingleChoiceItems(names, 0,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int position) {
						ScanUtils.province = provinces.get(position);
					}
				});
		location.setPositiveButton(
				NewBranchFragment.this.getString(R.string.add) + " "
						+ NewBranchFragment.this.getString(R.string.branch),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						createBranch();
						dialog.cancel();
					}
				});
		location.setNegativeButton(
				NewBranchFragment.this.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
								ScanFragment.class.getName());
					}
				});
		location.show();
	}

	private void createBranch() {
		((HomeActivity) getActivity()).curTask = new CreateBranchTask();
		((HomeActivity) getActivity()).curTask.execute();
	}

	class CreateBranchTask extends ProgressTask {

		private CreateBranchTask() {
			super(NewBranchFragment.this.getActivity(), NewBranchFragment.this
					.getString(R.string.adding) + "...", NewBranchFragment.this
					.getString(R.string.adding_branch));

		}

		@Override
		protected void success() {
			ScanUtils.clearFields();
			((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
					ScanFragment.class.getName());
			((HomeActivity) getActivity()).scan(null);
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(
					NewBranchFragment.this.getActivity(), error_code);
			dlg.setPositiveButton(
					NewBranchFragment.this.getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							createLocation();
							dialog.cancel();
						}
					});
			dlg.setNegativeButton(
					NewBranchFragment.this.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
									ScanFragment.class.getName());
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
			if (RPCUtils.startServer() == ERROR.SERVER) {
				return ERROR.SERVER;
			}
			if (ScanUtils.store == null) {
				if (ScanUtils.city == null) {
					return RPCUtils.createBranch(ScanUtils.province,
							MapUtils.getUserLat(), MapUtils.getUserLon(),
							ScanUtils.cityName, ScanUtils.storeName,
							ScanUtils.branchName);
				} else {
					return RPCUtils.createBranch(ScanUtils.city,
							MapUtils.getUserLat(), MapUtils.getUserLon(),
							ScanUtils.storeName, ScanUtils.branchName);
				}
			} else if (ScanUtils.city == null) {
				return RPCUtils.createBranch(ScanUtils.province,
						ScanUtils.store, MapUtils.getUserLat(),
						MapUtils.getUserLon(), ScanUtils.cityName,
						ScanUtils.branchName);
			} else {
				return RPCUtils.createBranch(ScanUtils.city, ScanUtils.store,
						MapUtils.getUserLat(), MapUtils.getUserLon(),
						ScanUtils.branchName);
			}
		}
	}
}
