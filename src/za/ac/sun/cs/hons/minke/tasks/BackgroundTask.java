package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.Constants;
import android.os.AsyncTask;

public abstract class BackgroundTask extends AsyncTask<Void, Integer, Void> {
	protected int tasks;
	private int error = 0;

	public BackgroundTask(int tasks) {
		this.tasks = tasks;
	}

	@Override
	protected Void doInBackground(Void... params) {
		synchronized (this) {
			int counter = 0;
			while (counter < tasks) {
				if((error = retrieve(counter++))>0){
					break;
				}
				showProgress(counter);
			}
		}
		return null;
	}


	protected abstract void showProgress(int task);

	@Override
	protected void onPostExecute(Void result) {
		if (error == Constants.SUCCESS) {
			success();
		} else {
			failure(error);
		}
	}

	protected abstract void success();

	protected abstract void failure(int code);

	protected abstract int retrieve(int counter);

}