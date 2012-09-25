package za.ac.sun.cs.hons.minke.gui.prefs;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.tasks.LoadDataTask;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataTask;
import za.ac.sun.cs.hons.minke.utils.Constants;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.TimedReceiver;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {
	private PendingIntent pendingIntent;
	private SharedPreferences prefs;
	private SharedPreferences.OnSharedPreferenceChangeListener spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			Log.d("PREF_CHANGE", key + " = " + prefs.getAll().get(key));
			if (key.equals("updating")) {
				nextAction();
			}
		}
	};

	class Updater extends UpdateDataTask {
		public Updater() {
			super(SettingsActivity.this);
		}

		@Override
		protected void success() {
			super.success();
			startActivity(IntentUtils.getHomeIntent(SettingsActivity.this));
		}

		@Override
		protected void failure(int error_code) {
			super.failure(error_code);
			startActivity(IntentUtils.getHomeIntent(SettingsActivity.this));
		}
	}

	class GetLocationTask extends ProgressTask {
		public GetLocationTask() {
			super(SettingsActivity.this, 1, "Pinpointing...",
					"Determining your location", true);
		}

		@Override
		protected int retrieve(int counter) {
			return MapUtils
					.refreshLocation((LocationManager) SettingsActivity.this
							.getSystemService(LOCATION_SERVICE));
		}

		@Override
		protected void success() {
			nextAction();
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(SettingsActivity.this,
					error_code);
			dlg.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					nextAction();
					dialog.cancel();
				}
			});
			dlg.show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(spChanged);
		addPreferencesFromResource(R.xml.preferences);
		if (PreferencesUtils.initial()) {
			GetLocationTask task = new GetLocationTask();
			task.execute();
		}
	}

	private void nextAction() {
		if (checkPreferences()) {
			startActivity(IntentUtils.getHomeIntent(this));
		}
	}

	private boolean checkPreferences() {
		if (!checkLoaded()) {
			return false;
		}
		if (!loadPreferences()) {
			return false;
		}
		if (PreferencesUtils.getUpdateFrequency() == Constants.STARTUP) {
			Updater task = new Updater();
			task.execute();
			return false;
		} else {
			if (PreferencesUtils.getUpdateFrequency() != Constants.NEVER) {
				startScheduler();
			}
			if (!PreferencesUtils.isFirstTime()) {
				LoadDataTask task = new LoadDataTask(this);
				task.execute();
			} else {
				Updater task = new Updater();
				task.execute();
				return false;
			}
			return true;
		}

	}

	private void startScheduler() {

		try {
			AlarmManager alarms = (AlarmManager) this
					.getSystemService(ALARM_SERVICE);

			Intent intent = new Intent(getApplicationContext(),
					TimedReceiver.class);
			pendingIntent = PendingIntent.getBroadcast(this, 1234567, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			long updateInterval = PreferencesUtils.getUpdateInterval();
			if (updateInterval == -1) {
				return;
			}
			long lastUpdate = PreferencesUtils.getLastUpdate();
			if (lastUpdate == -1) {
				lastUpdate = prefs.getLong("last_update", 0);
			}
			long nextUpdate = lastUpdate + updateInterval < System
					.currentTimeMillis() ? System.currentTimeMillis()
					: lastUpdate + updateInterval;
			alarms.setRepeating(AlarmManager.RTC_WAKEUP, nextUpdate,
					updateInterval, pendingIntent);
			alarms.cancel(pendingIntent);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean checkLoaded() {
		boolean loaded = prefs.getBoolean("loaded", false);
		if (!loaded) {
			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean("loaded", true);
			editor.commit();
			PreferencesUtils.setFirstTime(true);
		}
		return loaded;
	}

	public boolean loadPreferences() {
		PreferencesUtils.setUpdateFrequency(Integer.parseInt(prefs.getString(
				"updating", Constants.NO_FREQUENCY_SET + "")));
		if (PreferencesUtils.getUpdateFrequency() == Constants.NO_FREQUENCY_SET) {
			return false;
		}
		return true;
	}

}
