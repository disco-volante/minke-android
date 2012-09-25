package za.ac.sun.cs.hons.minke.gui.graph;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class GraphActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
		LinearLayout holder = (LinearLayout) findViewById(R.id.graph_holder);
		if (BrowseUtils.getBranchProducts().size() != 0) {
			holder.addView(new TimelineChart().execute(this,
					BrowseUtils.getBranchProducts()));
		}

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getGraphIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
