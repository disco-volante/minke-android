package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class LoadDataBGTask extends LoadTask {


	public LoadDataBGTask(Context context) {
		super(context);
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
		return EntityUtils.loadAll(context);
	}

}