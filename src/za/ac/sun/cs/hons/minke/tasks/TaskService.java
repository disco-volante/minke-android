package za.ac.sun.cs.hons.minke.tasks;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TaskService extends IntentService {

	public TaskService(Context context) {
		super("TaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.i("TaskService", "Service running");
        BackgroundUpdateDataTask task = new BackgroundUpdateDataTask();
        task.execute();
	}

}