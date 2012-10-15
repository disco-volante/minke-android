package za.ac.sun.cs.hons.minke.services;

import za.ac.sun.cs.hons.minke.tasks.UpdateDataBGTask;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class UpdateService extends IntentService {
	private UpdateDataBGTask task;

	public UpdateService() {
		super("UpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (Debug.ON) {
			Log.i("UpdateService", "Service running");
		}
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
		task = new UpdateDataBGTask(this);
		task.execute();
	}

}