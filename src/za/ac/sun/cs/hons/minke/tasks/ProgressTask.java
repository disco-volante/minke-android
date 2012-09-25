package za.ac.sun.cs.hons.minke.tasks;

import android.app.ProgressDialog;
import android.content.Context;

public abstract class ProgressTask extends BackgroundTask {
	private ProgressDialog progressDialog;
	private Context context;
	private String title, message;
	private boolean spinner;

	public ProgressTask(Context context, int tasks, String title,
			String message, boolean spinner) {
		super(tasks);
		this.context = context;
		this.title = title;
		this.message = message;
		this.spinner = spinner;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		if (!spinner) {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		} else {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setProgress(0);
		progressDialog.show();
	}

	@Override
	protected void showProgress(int task) {
		publishProgress((100 / tasks) * task);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		progressDialog.dismiss();
		super.onPostExecute(result);
	}

}