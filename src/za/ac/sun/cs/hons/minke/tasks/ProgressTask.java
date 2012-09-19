package za.ac.sun.cs.hons.minke.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public abstract class ProgressTask extends AsyncTask<Void, Integer, Void> {
	private ProgressDialog progressDialog;
	private Context context;
	private int tasks;
	private String title, message;
	private boolean spinner;

	public ProgressTask(Context context, int tasks, String title, String message, boolean spinner) {
		this.context = context;
		this.tasks = tasks;
		this.title = title;
		this.message = message;
		this.spinner = spinner;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		if(!spinner){
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		}else{
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
	protected Void doInBackground(Void... params) {
		synchronized (this) {
			int counter = 0;
			while (counter <= tasks) {
				retrieve(counter++);
				publishProgress((100 / tasks) * counter);
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		progressDialog.dismiss();
	}

	protected abstract void retrieve(int counter);

}