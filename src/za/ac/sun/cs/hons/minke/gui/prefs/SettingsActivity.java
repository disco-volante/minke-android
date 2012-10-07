package za.ac.sun.cs.hons.minke.gui.prefs;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.services.IUpdateService;
import za.ac.sun.cs.hons.minke.services.UpdateService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity {
	private IUpdateService updater;
	private SharedPreferences prefs;
	private UpdateConnection uConnection;
	private SharedPreferences.OnSharedPreferenceChangeListener spChanged = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences prefs,
				String key) {
			Log.d("PREF_CHANGE", key + " = " + prefs.getAll().get(key));
			if (key.equals("updating") && updater != null) {
				try {
					updater.iRestart();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};

	class UpdateConnection implements ServiceConnection {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			updater = IUpdateService.Stub.asInterface((IBinder) binder);
		}

		public void onServiceDisconnected(ComponentName className) {
			updater = null;
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		prefs.registerOnSharedPreferenceChangeListener(spChanged);
		initService();
	}

	private void initService() {
		uConnection = new UpdateConnection();
		Intent i = new Intent();
		i.setClassName("za.ac.sun.cs.hons.minke.services",
				UpdateService.class.getName());
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
