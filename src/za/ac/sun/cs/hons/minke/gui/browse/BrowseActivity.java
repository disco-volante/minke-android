package za.ac.sun.cs.hons.minke.gui.browse;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.gui.utils.BPListAdapter;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class BrowseActivity extends Activity {
	/** Called when the activity is first created. */
	BPListAdapter bplistAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse);
		showData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu1, menu);
		return true;
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