package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferencesUtils {

	private static int updateFrequency;
	private static long updateInterval = -1;
	private static long lastUpdate = -1;
	private static boolean firstTime = false;
	private static boolean loaded = false;
	private static boolean checkServer;
	private static int animationLevel;

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

	public static long getLastUpdate() {
		return lastUpdate;
	}

	public static void storeLastUpdate(Context context) {
		lastUpdate = System.currentTimeMillis();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("last_update", lastUpdate);
		editor.commit();
	}

	public static void setFirstTime(boolean _firstTime) {
		firstTime = _firstTime;
	}

	public static void changeFirstTime(Context context, boolean _firstTime) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("first_time", _firstTime);
		editor.commit();
		firstTime = _firstTime;
	}

	public static boolean isFirstTime() {
		return firstTime;
	}

	public static ERROR loadPreferences(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		setUpdateFrequency(Integer.parseInt(prefs.getString("updating",
				Constants.NO_FREQUENCY_SET + "")));
		setCheckServer(prefs.getBoolean("check_server", false));
		setAnimationLevel(Integer.parseInt(prefs.getString("animation", Constants.STANDARD+"")));
		setUpdateInterval();
		loadLastUpdate(prefs.getLong("last_update", 0));
		setLoaded(true);

		setFirstTime(prefs.getBoolean("first_time", true));
		Log.d("PREFERENCES", " Preferences loaded:\n frequency-> "
				+ updateFrequency + ";\n interval-> " + updateInterval
				+ ";\n last_update-> " + lastUpdate + ";\n firstTime-> "
				+ firstTime +";\n animation-> "+animationLevel);
		return ERROR.SUCCESS;

	}

	private static void setAnimationLevel(int level) {
		animationLevel = level;
	}

	private static void setCheckServer(boolean _checkServer) {
		checkServer = _checkServer;
	}

	public static void loadLastUpdate(long found) {
		lastUpdate = found + updateInterval > System.currentTimeMillis() ? found
				+ updateInterval
				: System.currentTimeMillis();
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void setLoaded(boolean loaded) {
		PreferencesUtils.loaded = loaded;
	}

	public static boolean checkServer() {
		return checkServer;
	}

	public static int getAnimationLevel() {
		return animationLevel;
	}

}
