package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.ErrorUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

	@Override
	protected void onPostExecute(Void result) {
		if (error != ERROR.SUCCESS) {
			Log.v(TAGS.HTTP, "ERROR");
			Toast.makeText(context, ErrorUtils.getErrorMessage(error, context),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(context,
					context.getString(R.string.update_success_msg),
					Toast.LENGTH_SHORT).show();
		}
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