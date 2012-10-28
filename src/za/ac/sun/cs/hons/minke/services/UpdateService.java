package za.ac.sun.cs.hons.minke.services;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataBGTask;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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
			public void iStart() throws RemoteException {
				showNotification();
			}

		};
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		if (!running) {
			running = true;
			UpdateDataBGTask update = new UpdateDataBGTask(this) {
				private Builder mNotifyBuilder;

				@Override
				public void onPreExecute() {
					PendingIntent pending = PendingIntent.getActivity(context,
							0, IntentUtils.getHomeIntent(context),
							Intent.FLAG_ACTIVITY_NEW_TASK);
					CharSequence title = getText(R.string.updating);
					mNotifyBuilder = new NotificationCompat2.Builder(context)
							.setContentTitle(title)
							.setSmallIcon(R.drawable.ic_launcher)
							.setAutoCancel(false).setProgress(0, 0, true)
							.setContentIntent(pending);
					mNM.notify(NOTIFICATION, mNotifyBuilder.build());
				}

				@Override
				public void onPostExecute(Void result) {
					super.onPostExecute(result);
					mNM.cancelAll();
					running = false;
					UpdateService.this.stopSelf();
				}
			};
			update.execute();

		}
	}

}