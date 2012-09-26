package za.ac.sun.cs.hons.minke.services;

import za.ac.sun.cs.hons.minke.tasks.BackgroundUpdateTask;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class UpdateService extends IntentService {
	private BackgroundUpdateTask task;

	public UpdateService() {
		super("UpdateService");
	}

	public UpdateService(Context context) {
		super("UpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("UpdateService", "Service running");
		start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new IUpdateService.Stub() {

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
		task = new BackgroundUpdateTask();
		task.execute();
	}

}