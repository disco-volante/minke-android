package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class UpdateDataFGTask extends ProgressTask {

	public UpdateDataFGTask(Context context) {
		super(context, context.getString(R.string.updating), context.getString(R.string.updating_msg));
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(ERROR error_code) {
		Builder dlg = DialogUtils.getErrorDialog(context, error_code);
		dlg.show();
	}

	@Override
	protected ERROR retrieve() {
		if (!isNetworkAvailable()) {
			return ERROR.CLIENT;
		}
		ERROR error = RPCUtils.retrieveAll(context);
		if(!error.equals(ERROR.SUCCESS)){
			error = RPCUtils.retrieveAll(context);
		}
		return error;
	}
}