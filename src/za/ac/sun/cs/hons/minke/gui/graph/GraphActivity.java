package za.ac.sun.cs.hons.minke.gui.graph;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import android.app.Activity;
import android.os.Bundle;
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

}
