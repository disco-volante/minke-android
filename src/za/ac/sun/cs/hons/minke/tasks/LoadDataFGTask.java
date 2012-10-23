package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.Activity;
import android.app.AlertDialog.Builder;

public class LoadDataFGTask extends ProgressTask {

	public LoadDataFGTask(Activity activity) {
		super(activity, activity.getString(R.string.loading), activity.getString(R.string.loading_msg));
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(ERROR error_code) {
		Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
		dlg.show();

	}

	@Override
	protected ERROR retrieve() {
		return EntityUtils.loadAll(activity);
	}

}