package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.Activity;
import android.app.AlertDialog.Builder;

public class UpdateDataFGTask extends ProgressTask {

	public UpdateDataFGTask(Activity activity) {
		super(activity, activity.getString(R.string.updating), activity.getString(R.string.updating_msg));
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
		if (!isNetworkAvailable()) {
			return ERROR.CLIENT;
		}
		if(RPCUtils.startServer() == ERROR.SERVER){
			return ERROR.SERVER;
		}
		return RPCUtils.retrieveAll(activity);
	}
}