package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public abstract class LoadTask extends AsyncTask<Void, Integer, Void> {
	private ERROR error = ERROR.SUCCESS;
	protected Activity activity;
	private boolean done;

	public LoadTask(Activity activity) {
		this.activity = activity;
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
		done = true;
		if (error == ERROR.SUCCESS) {
			success();
		} else {
			failure(error);
		}
	}

	protected abstract void success();

	protected abstract void failure(ERROR code);

	protected abstract ERROR retrieve();

	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public void detach() {
		activity = null;
	}

	public void attach(Activity activity) {
		this.activity = activity;
	}
	
	public boolean done() {
		return done;
	}

}