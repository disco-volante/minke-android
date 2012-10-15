package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.tasks.LoadTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

public class MinkeApplication extends Application {
	private boolean loaded = true;

	class InitTask extends LoadTask {

		public InitTask(Context context) {
			super(context);
		}

		@Override
		protected void success() {
			loaded = true;
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(context, error_code);
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			PreferencesUtils.loadPreferences(context);
			LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 30000,
					2 * 60 * 30000, "za.ac.sun.cs.hons.minke");
			MapUtils.refreshLocation((LocationManager) context
					.getSystemService(Activity.LOCATION_SERVICE));
			return EntityUtils.init(context);
		}

	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		new InitTask(this).execute();
		if (Debug.ON) {
			Log.d(TAGS.STATE, "Application onCreate()");
			LocationLibrary.showDebugOutput(true);
		}
	}

	public boolean loaded() {
		return loaded;
	}

}
