package za.ac.sun.cs.hons.minke.gui;

import za.ac.sun.cs.hons.minke.MinkeApplication;
import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.tasks.UpdateDataBGTask;
import za.ac.sun.cs.hons.minke.utils.ErrorUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (DEBUG.ON) {
			Log.d(TAGS.STATE, "SplashActivity onCreate()");
		}
		new Thread(){
			@Override
			public void run(){
				while(!((MinkeApplication) getApplication()).isLoaded()){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				update();
			}
		}.start();

	}

	/**
	 * Helper method for updating date asynchronously in the foreground.
	 */
	private void update() {
		if (PreferencesUtils.isFirstTime()
				|| PreferencesUtils.getUpdateFrequency() == Constants.STARTUP) {
			new Updater(this).execute();
		} else {
			loadHome();
		}
	}

	private void loadHome() {
		startActivity(IntentUtils.getHomeIntent(getApplicationContext()));
		if (PreferencesUtils.getAnimationLevel() != Constants.NONE) {
			overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		}
		finish();
	}

	

	/**
	 * {@link AsyncTask} subclass used to update data in the foreground.
	 * 
	 * @author pieter
	 * 
	 */
	static class Updater extends UpdateDataBGTask {
		private Activity activity;

		public Updater(Activity _activity) {
			super(_activity.getApplicationContext());
			activity = _activity;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (error != ERROR.SUCCESS) {
				Log.v(TAGS.HTTP, "ERROR");
				Toast.makeText(activity,
						ErrorUtils.getErrorMessage(error, activity),
						Toast.LENGTH_LONG).show();
			} else {
				if (PreferencesUtils.isFirstTime()) {
					PreferencesUtils.changeFirstTime(context, false);
				}
			}
			((SplashActivity) activity).loadHome();

		}
	}

}