package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;

public class MinkeApplication extends Application {
	private boolean loaded = false;

	public void onCreate(){
		new InitTask(this).execute();
	}
	public boolean isLoaded() {
		return loaded;
	}
	public void setLoaded(boolean _loaded) {
		loaded = _loaded;
	}
	static class InitTask extends AsyncTask<Void, Integer, Void> {
		private Application application;

		public InitTask(Application application) {
			this.application = application;		}

		
		@Override
		protected Void doInBackground(Void... params) {
			synchronized (this) {
				PreferencesUtils.loadPreferences(application);
				MapUtils.refreshLocation((LocationManager) application
						.getSystemService(Context.LOCATION_SERVICE));
				EntityUtils.init(application);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			((MinkeApplication) application).setLoaded(true);
			
		}

	}
	
	
}
