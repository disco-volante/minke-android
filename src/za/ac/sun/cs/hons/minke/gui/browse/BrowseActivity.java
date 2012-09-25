package za.ac.sun.cs.hons.minke.gui.browse;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.gui.utils.BPListAdapter;
import za.ac.sun.cs.hons.minke.tasks.StoreDataTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;

public class BrowseActivity extends Activity {
	/** Called when the activity is first created. */
	BPListAdapter bplistAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_browse);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getNextAction(this));
		actionBar.addAction(ActionUtils.getSettingsAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
		showData();

	}
	@Override
	public void onPause(){
		super.onPause();
		StoreDataTask task = new StoreDataTask(this);
		task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.next:
			showHistories(this.getCurrentFocus());
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.refresh:
			startActivity(IntentUtils.getBrowseIntent(this));
			return true;
		case R.id.settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showHistories(View view) {
		startActivity(IntentUtils.getGraphIntent(this));
	}

	private void showData() {
		final ArrayList<BranchProduct> bps = BrowseUtils.getBranchProducts();
		bplistAdapter = new BPListAdapter(this, bps);
		ListView bplist = (ListView) findViewById(R.id.bp_list);
		bplist.setAdapter(bplistAdapter);
		bplistAdapter.notifyDataSetChanged();

	}
}