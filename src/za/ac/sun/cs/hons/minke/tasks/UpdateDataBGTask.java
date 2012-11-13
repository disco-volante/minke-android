package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class UpdateDataBGTask extends AsyncTask<Void, Integer, Void> {

	protected Context context;
	protected ERROR error;

	public UpdateDataBGTask(Context context) {
		this.context = context;
	}

	@Override
	protected Void doInBackground(Void... params) {
		synchronized (this) {
			error = retrieve();
		}
		return null;
	}
	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	protected ERROR retrieve() {
		if (!isNetworkAvailable()) {
			return ERROR.CLIENT;
		}
		if (RPCUtils.startServer() == ERROR.SERVER) {
			return ERROR.SERVER;
		}
		return RPCUtils.retrieveAll(context);
	}

}