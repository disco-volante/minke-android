package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public abstract class LoadTask extends AsyncTask<Void, Integer, Void> {
	private ERROR error = ERROR.SUCCESS;
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

	protected abstract void failure(ERROR code);

	protected abstract ERROR retrieve();

	protected boolean isNetworkAvailable() {
		if(context == null){
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

}