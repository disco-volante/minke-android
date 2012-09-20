package za.ac.sun.cs.hons.minke.gui.create;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.GPSArea;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.markupartist.android.widget.ActionBar;

public class NewBranchActivity extends Activity {
	private AutoCompleteTextView branchBox;
	private EditText branchText;
	private AutoCompleteTextView storeBox;
	private AutoCompleteTextView cityBox;
	public String province, country;

	class AddBranchTask extends ProgressTask {
		private Branch branch;

		public AddBranchTask(Branch branch) {
			super(NewBranchActivity.this, 1, "Adding...",
					"Adding branch to the database", true);
			this.branch = branch;
		}

		@Override
		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			setBranch(null);
		}

		@Override
		protected void retrieve(int counter) {
			RPCUtils.addBranch(branch, province, country);
			setBranch(null);

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
		final ArrayAdapter<IsEntity> branchAdapter = new ArrayAdapter<IsEntity>(
				this, R.layout.dropdown_item, EntityUtils.getBranches());
		branchBox.setAdapter(branchAdapter);
		branchBox
				.addTextChangedListener(new TextErrorWatcher(branchBox, false));
		branchBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EntityUtils.setUserBranch((Branch) branchAdapter
						.getItem(position));
			}

		});
		branchText = (EditText) findViewById(R.id.branchText);
		branchText
				.addTextChangedListener(new TextErrorWatcher(branchText, true));
		storeBox = (AutoCompleteTextView) findViewById(R.id.storeBox);
		ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(this,
				R.layout.dropdown_item, EntityUtils.getStores());
		storeBox.setAdapter(storeAdapter);
		storeBox.addTextChangedListener(new TextErrorWatcher(storeBox, false));
		cityBox = (AutoCompleteTextView) findViewById(R.id.cityBox);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this,
				R.layout.dropdown_item, EntityUtils.getCities());
		cityBox.setAdapter(cityAdapter);
		cityBox.addTextChangedListener(new TextErrorWatcher(cityBox, false));
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_newbranch);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
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
		getMenuInflater().inflate(R.menu.newbranch_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getNewProductIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setBranch(View view) {
		IntentUtils.setScan(true);
		startActivity(IntentUtils.getHomeIntent(this));
	}

	public void createLocation(View view) {
		if (branchText.getError() != null || storeBox.getError() != null
				|| cityBox.getError() != null) {
			return;
		}
		String[] locs = cityBox.getText().toString().split(",");
		if (locs.length == 1) {
			requestLocations();
		} else {
			province = locs[1];
			country = locs[2];
		}
		Branch b = new Branch(branchText.getText().toString(), storeBox
				.getText().toString(), locs[0], new GPSArea(MapUtils
				.getLocation().getLat(), MapUtils.getLocation().getLon()), null);
		AddBranchTask task = new AddBranchTask(b);
		task.execute();
	}

	private void requestLocations() {
		if (EntityUtils.getProvinces() == null) {
			return;
		}
		final int size = Math.min(EntityUtils.getProvinces().size(), 10);
		final String[] names = new String[size];
		int i = 0;
		for (IsEntity b : EntityUtils.getProvinces()) {
			if (i == size) {
				break;
			}
			names[i++] = b.toString();
		}
		AlertDialog.Builder location = new AlertDialog.Builder(this);
		location.setTitle("Choose Province");
		location.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				String[] s = names[position].split(",");
				province = s[0];
				country = s[1];

			}
		});
		location.setPositiveButton("Add Branch",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
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
