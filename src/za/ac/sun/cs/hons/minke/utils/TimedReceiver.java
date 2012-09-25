package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.tasks.TaskService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimedReceiver extends BroadcastReceiver {

	private static final String DEBUG_TAG = "TimedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(DEBUG_TAG, "Recurring alarm; requesting download service.");
		// start the download

		Log.i("Alarm Receiver", "Entered");

		Log.i("Alarm Receiver", "If loop");
		Intent inService = new Intent(context, TaskService.class);
		context.startService(inService);

	}

}
