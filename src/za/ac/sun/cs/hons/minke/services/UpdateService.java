package za.ac.sun.cs.hons.minke.services;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jakewharton.notificationcompat2.NotificationCompat2;
import com.jakewharton.notificationcompat2.NotificationCompat2.Builder;

public class UpdateService extends Service {
	private boolean running;
	private NotificationManager mNM;
	private int NOTIFICATION = R.string.update_service_started;

	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Log.v(TAGS.STATE, getString(R.string.update_service_started));
	}

	@Override
	public void onDestroy() {
		mNM.cancel(NOTIFICATION);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new IUpdateService.Stub() {
			@Override
			public boolean iStart() throws RemoteException {
				return showNotification().equals(ERROR.SUCCESS);

			}

		};
	}

	/**
	 * Show a notification while this service is running.
	 */
	private ERROR showNotification() {
		if (!running) {
			try {
				running = true;
				PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0,
						IntentUtils.getHomeIntent(getApplicationContext()),
						Intent.FLAG_ACTIVITY_NEW_TASK);
				CharSequence title = getText(R.string.app_name);
				CharSequence text = getText(R.string.updating_msg);
				Builder mNotifyBuilder = new NotificationCompat2.Builder(this)
						.setContentTitle(title).setProgress(0, 0, true)
						.setContentText(text)
						.setSmallIcon(R.drawable.ic_launcher)
						.setAutoCancel(false).setContentIntent(pending);
				mNM.notify(NOTIFICATION, mNotifyBuilder.build());
				ERROR error = retrieve();
				mNM.cancelAll();
				running = false;
				stopSelf();
				return error;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return ERROR.SUCCESS;
	}

	protected boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
		EntityUtils.init(this);
		return RPCUtils.retrieveAll(this);
	}

}