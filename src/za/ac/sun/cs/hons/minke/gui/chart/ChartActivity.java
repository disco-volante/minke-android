package za.ac.sun.cs.hons.minke.gui.chart;

import java.util.HashSet;
import java.util.Set;

import org.achartengine.GraphicalView;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.tasks.ChartTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;

public class ChartActivity extends SherlockActivity {

	private TimelineChart chart;
	private GraphicalView view;
	private RelativeLayout holder;
	private ChartTask curTask;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_graph);
		holder = (RelativeLayout) findViewById(R.id.graph_holder);
		if (BrowseUtils.getBranchProducts() == null || BrowseUtils.getBranchProducts().size() == 0) {
			startActivity(IntentUtils.getHomeIntent(getApplicationContext()));
			finish();
		} else if (getLastNonConfigurationInstance() != null) {
			curTask = (ChartTask) getLastNonConfigurationInstance();
			if (!curTask.getStatus().equals(Status.FINISHED)) {
				curTask.attach(this);
				setBuilding(true);
			}
		} else {
			buildChart();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		setBuilding(curTask != null
				&& !curTask.getStatus().equals(Status.FINISHED));

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (curTask.getStatus().equals(Status.FINISHED)) {
			return null;
		}
		curTask.detach();
		return curTask;

	}

	private void setBuilding(boolean building) {
		setProgressBarIndeterminateVisibility(building);
	}

	private void buildChart() {
		setBuilding(true);
		chart = new TimelineChart(this, 5);
		curTask = new ChartTask(chart);
		curTask.execute();
	}

	public void showChart() {
		view = chart.getChart();
		holder.addView(view);
		setBuilding(false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_graph, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_add:
			showEditDlg(true, chart.getRemoved(), getString(R.string.add));
			return true;
		case R.id.menu_item_remove:
			showEditDlg(false, chart.getAdded(), getString(R.string.remove));
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return false;
	}

	private void showEditDlg(final boolean add, HashSet<String> items,
			String btn) {
		final String[] choices = items.toArray(new String[items.size()]);
		final HashSet<String> changed = new HashSet<String>();
		Builder dlg = DialogUtils.getChartDialog(this);
		if(dlg == null){
			return;
		}
		if (items.size() == 0) {
			dlg.setMessage(getString(R.string.no_change));
		} else {
			dlg.setMultiChoiceItems(choices, null,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							if (isChecked) {
								changed.add(choices[which]);
							} else {
								changed.remove(choices[which]);
							}
						}
					});
		}
		dlg.setPositiveButton(btn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				editItems(changed, add);
				dialog.cancel();
			}
		});
		dlg.show();
	}

	protected void editItems(Set<String> changed, boolean add) {
		if (changed.size() == 0) {
			return;
		}
		if (add) {
			chart.addSeries(changed);
		} else {
			chart.removeSeries(changed);
		}
		view.repaint();
	}

}
