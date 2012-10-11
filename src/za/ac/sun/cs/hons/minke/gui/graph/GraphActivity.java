package za.ac.sun.cs.hons.minke.gui.graph;

import com.actionbarsherlock.app.SherlockActivity;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.tasks.ChartTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GraphActivity extends SherlockActivity {

	private TimelineChart chart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		if (BrowseUtils.getBranchProducts().size() != 0) {
			chart = new TimelineChart(this);
			ChartTask task = new ChartTask(chart);
			task.execute();
		}

	}

	public void showChart() {
		LinearLayout holder = (LinearLayout) findViewById(R.id.graph_holder);
		holder.addView(chart.getChart());
		
	}

}
