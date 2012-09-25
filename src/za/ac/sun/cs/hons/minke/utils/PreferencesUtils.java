package za.ac.sun.cs.hons.minke.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesUtils {

	private static int updateFrequency;
	private static long updateInterval = -1;
	private static boolean initial = true;
	private static long lastUpdate = -1;
	private static boolean firstTime = false;

	public static int getUpdateFrequency() {
		return updateFrequency;
	}

	public static void setUpdateFrequency(int updateFrequency) {
		PreferencesUtils.updateFrequency = updateFrequency;
	}

	public static long getUpdateInterval() {
		return updateInterval;
	}

	public static void setUpdateInterval() {
		switch (updateFrequency) {
		case Constants.HOURLY:
			updateInterval = Constants.HOUR;
			break;
		case Constants.DAILY:
			updateInterval = Constants.DAY;
			break;
		case Constants.WEEKLY:
			updateInterval = Constants.WEEK;
			break;
		}
	}

	public static boolean initial() {
		if (initial) {
			initial = false;
			return true;
		}
		return initial;
	}

	public static long getLastUpdate() {
		return lastUpdate;
	}

	public static void setLastUpdate(Context context) {
		lastUpdate = System.currentTimeMillis();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_update", lastUpdate);
		editor.commit();
	}

	public static void setFirstTime(boolean firstTime) {
		PreferencesUtils.firstTime = firstTime;
	}

	public static boolean isFirstTime() {
		return firstTime;
	}
}
