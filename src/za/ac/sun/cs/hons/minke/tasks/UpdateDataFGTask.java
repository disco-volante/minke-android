package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class UpdateDataFGTask extends ProgressTask {
	private Context context;

	public UpdateDataFGTask(Context context) {
		super(context, "Downloading", "Downloading data, please wait...");
		this.context = context;
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(int error_code) {
		Builder dlg = DialogUtils.getErrorDialog(context, error_code);
		dlg.show();
	}

	@Override
	protected int retrieve() {
		return RPCUtils.retrieveAll(context);
	}
}