package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class MinkeApplication extends Application {
	private static boolean loaded = true;

	static class InitTask extends AsyncTask<Void, Integer, Void> {

		private Application app;
		private ERROR error;

		public InitTask(Application app) {
			this.app = app;
		}

		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				error = retrieve();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (error == ERROR.SUCCESS) {
				success();
			} else {
				failure(error);
			}
		}

		protected void success() {
			loaded = true;
		}

		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(app, error_code);
			dlg.show();

		}

		protected ERROR retrieve() {
			PreferencesUtils.loadPreferences(app);
			LocationLibrary.initialiseLibrary(app.getBaseContext(), 60 * 30000,
					2 * 60 * 30000, "za.ac.sun.cs.hons.minke");
			MapUtils.refreshLocation((LocationManager) app
					.getSystemService(Context.LOCATION_SERVICE));
			return EntityUtils.init(app);
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		new InitTask(this).execute();
		if (DEBUG.ON) {
			Log.d(TAGS.STATE, "Application onCreate()");
			LocationLibrary.showDebugOutput(true);
		}
	}

	public static boolean loaded() {
		return loaded;
	}

}
