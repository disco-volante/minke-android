package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;

public abstract class ProgressTask extends LoadTask {
	private ProgressDialog progressDialog;
	private String title, message;

	public ProgressTask(Activity activity, String title, String message) {
		super(activity);
		this.title = title;
		this.message = message;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ProgressTask.this.cancel(true);
					}
				});
		progressDialog.setOwnerActivity(activity);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		progressDialog.dismiss();
		super.onPostExecute(result);
	}

	@Override
	public void onCancelled() {
		progressDialog.setCancelable(true);
		progressDialog.cancel();
	}

	@Override
	public void detach() {
		progressDialog.dismiss();
		super.detach();
	}

	@Override
	public void attach(Activity activity) {
		super.attach(activity);
		progressDialog = new ProgressDialog(activity);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
				activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						ProgressTask.this.cancel(true);
					}
				});
		progressDialog.setOwnerActivity(activity);
		progressDialog.show();
	}

}