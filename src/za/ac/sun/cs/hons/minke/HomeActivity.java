package za.ac.sun.cs.hons.minke;

import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.markupartist.android.widget.ActionBar;

public class HomeActivity extends Activity {
	private class InitDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			RPCUtils.init();
			return null;

		}
	   

	    
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		InitDataTask task = new InitDataTask();
		task.execute();
		MapUtils.refreshLocation((LocationManager) getSystemService(LOCATION_SERVICE));
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_home);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getScanAction(this));
		actionBar.addAction(ActionUtils.getLocationSearchAction(this));
		actionBar.addAction(ActionUtils.getShopAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));

	}

	public void scan(View view) {
		startActivity(IntentUtils.getScanIntent(getApplicationContext()));
	}

	public void shop(View view) {
		startActivity(IntentUtils.getShopIntent(getApplicationContext()));
	}

	public void search(View view) {
		startActivity(IntentUtils
				.getLocationSearchIntent(getApplicationContext()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan:
			scan(this.getCurrentFocus());
			return true;
		case R.id.search:
			search(this.getCurrentFocus());
			return true;
		case R.id.shop:
			shop(this.getCurrentFocus());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
