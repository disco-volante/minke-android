package za.ac.sun.cs.hons.minke.gui.prefs;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.services.IScheduledUpdateService;
import za.ac.sun.cs.hons.minke.services.ScheduledUpdateService;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class SettingsActivity extends SherlockPreferenceActivity {
	private IScheduledUpdateService updater;
	private SharedPreferences prefs;
	private UpdateConnection uConnection;
	private SharedPreferences.OnSharedPreferenceChangeListener spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			if (DEBUG.ON) {
				Log.d("PREF_CHANGE", key + " = " + prefs.getAll().get(key));
			}
			if (key.equals("updating") && updater != null) {
				try {
					updater.iRestart();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
			PreferencesUtils.loadPreferences(SettingsActivity.this);
		}
	};

	class UpdateConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			updater = IScheduledUpdateService.Stub.asInterface(binder);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			updater = null;
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(spChanged);
		initService();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return false;
	}

	private void initService() {
		uConnection = new UpdateConnection();
		Intent i = new Intent();
		i.setClassName("za.ac.sun.cs.hons.minke.services",
				ScheduledUpdateService.class.getName());
		bindService(i, uConnection, Context.BIND_AUTO_CREATE);
	}

	private void releaseService() {
		unbindService(uConnection);
		uConnection = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseService();
	}

}
