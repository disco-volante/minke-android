package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.tasks.LoadTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibrary;

import android.app.Application;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.Log;

public class MinkeApplication extends Application {

	class InitDBTask extends LoadTask {

		public InitDBTask(Context context) {
			super(context);
		}

		@Override
		protected void success() {
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(context, error_code);
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			return EntityUtils.init(context);
		}

	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		new InitDBTask(this).execute();

		Log.d("TestApplication", "onCreate()");

		// output debug to LogCat, with tag LittleFluffyLocationLibrary
		LocationLibrary.showDebugOutput(true);

		// in most cases the following initialising code using defaults is
		// probably sufficient:
		//
		// LocationLibrary.initialiseLibrary(getBaseContext(),
		// "com.your.package.name");
		//
		// however for the purposes of the test app, we will request
		// unrealistically frequent location broadcasts
		// every 1 minute, and force a location update if there hasn't been one
		// for 2 minutes.
		LocationLibrary.initialiseLibrary(getBaseContext(), 60 * 30000,
				2 * 60 * 30000, "za.ac.sun.cs.hons.minke");
	}
}
