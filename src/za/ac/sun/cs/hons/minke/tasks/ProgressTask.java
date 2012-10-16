package za.ac.sun.cs.hons.minke.tasks;

import android.app.ProgressDialog;
import android.content.Context;

public abstract class ProgressTask extends LoadTask {
	private ProgressDialog progressDialog;
	private String title, message;

	public ProgressTask(Context context,  String title, String message) {
		super(context);
		this.title = title;
		this.message = message;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(false);
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		progressDialog.dismiss();
		super.onPostExecute(result);
	}
	@Override
	public void onCancelled(){
		progressDialog.setCancelable(true);
		progressDialog.cancel();
	}

}