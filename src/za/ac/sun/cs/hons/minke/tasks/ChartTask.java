package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.graph.TimelineChart;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.AlertDialog.Builder;

public class ChartTask extends ProgressTask {

	private TimelineChart chart;

	public ChartTask(TimelineChart _chart) {
		super(_chart.getGraphActivity(), _chart.getGraphActivity().getString(R.string.drawing)+"...",  _chart.getGraphActivity().getString(R.string.drawing_msg));
		this.chart = _chart;
	}
	
	@Override
	protected void success() {
		chart.getGraphActivity().showChart();
	}

	@Override
	protected void failure(ERROR error_code) {
		Builder dlg = DialogUtils.getErrorDialog(context, error_code);
		dlg.show();

	}

	@Override
	protected ERROR retrieve() {
		chart.buildData(BrowseUtils.getBranchProducts());
		if(!chart.isLoaded()){
			return ERROR.PARSE;
		}
		return ERROR.SUCCESS;
	}
	

}