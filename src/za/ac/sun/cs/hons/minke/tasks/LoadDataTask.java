package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import android.app.AlertDialog.Builder;
import android.content.Context;

public class LoadDataTask extends BackgroundTask {

	private Context context;

	public LoadDataTask(Context context) {
		super(1);
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
	protected int retrieve(int counter) {
		return EntityUtils.loadLocalData(context);
	}

	@Override
	protected void showProgress(int task) {

	}
}