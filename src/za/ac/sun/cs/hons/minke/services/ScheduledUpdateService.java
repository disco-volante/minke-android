package za.ac.sun.cs.hons.minke.services;

import za.ac.sun.cs.hons.minke.tasks.UpdateDataBGTask;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class ScheduledUpdateService extends IntentService {
	private UpdateDataBGTask task;

	public ScheduledUpdateService() {
		super("ScheduledUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (DEBUG.ON) {
			Log.i("ScheduledUpdateService", "Service running");
		}
		start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new IScheduledUpdateService.Stub() {

			@Override
			public void iStart() throws RemoteException {
				start();
			}

			@Override
			public void iStop() throws RemoteException {
				stop();
			}

			@Override
			public void iRestart() throws RemoteException {
				restart();
			}

		};
	}

	public void stop() {
		if (task != null) {
			task.cancel(true);
		}
	}

	public void restart() {
		stop();
		start();
	}

	private void start() {
		task = new UpdateDataBGTask(this);
		task.execute();
	}

}