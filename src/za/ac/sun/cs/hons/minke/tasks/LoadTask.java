package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.content.Context;
import android.os.AsyncTask;

public abstract class LoadTask extends AsyncTask<Void, Integer, Void> {
	private int error = 0;
	protected Context context;

	public LoadTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {
		synchronized (this) {
			error = retrieve();
			}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if (error == ERROR.SUCCESS) {
			success();
		} else {
			failure(error);
		}
	}

	protected abstract void success();

	protected abstract void failure(int code);

	protected abstract int retrieve();

}