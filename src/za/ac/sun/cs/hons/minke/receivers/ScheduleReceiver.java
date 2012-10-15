package za.ac.sun.cs.hons.minke.receivers;

import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Debug.ON) {
			Log.d("SCHEDULER", " Scheduler started");
		}
		if (!PreferencesUtils.isLoaded()) {
			PreferencesUtils.loadPreferences(context);
		}
		if (!PreferencesUtils.isFirstTime()
				&& PreferencesUtils.getUpdateFrequency() != Constants.STARTUP
				&& PreferencesUtils.getUpdateFrequency() != Constants.NEVER) {
			AlarmManager service = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent i = new Intent(context, UpdateServiceReceiver.class);
			PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);

			service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					PreferencesUtils.getLastUpdate(),
					PreferencesUtils.getUpdateInterval(), pending);
		}

	}

}
