package za.ac.sun.cs.hons.minke.gui.shop;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.BranchListAdapter;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;

public class StoreActivity extends Activity {

	private BranchListAdapter branchListAdapter;
	private ArrayList<IsEntity> branches;
	protected Branch branch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.store_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.refresh:
			startActivity(IntentUtils.getStoreIntent(this));
			return true;
		case R.id.directions:
			showDirections(this.getCurrentFocus());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initGUI() {
		setContentView(R.layout.store);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_store);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getMapAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
		branches = EntityUtils.getBranches();
		branchListAdapter = new BranchListAdapter(this, branches);
		ListView storeList = (ListView) findViewById(R.id.store_list);
		storeList.setAdapter(branchListAdapter);
		branchListAdapter.notifyDataSetChanged();

	}

	public void showDirections(View view) {
		String[] names = new String[branches.size()];
		MapUtils.setBranches(branches);
		MapUtils.setDestination(0);
		int i = 0;
		for (IsEntity b : branches) {
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
								.getMapIntent(StoreActivity.this));
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

}
