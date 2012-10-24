package za.ac.sun.cs.hons.minke.gui.maps.google;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.DirectionsListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends SherlockMapActivity {
	private MapView mapView;
	private MapController mapController;
	private AlertDialog dirDlg;
	private ProgressTask curTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (!EntityUtils.isLoaded()) {
			startActivity(IntentUtils.getHomeIntent(this));
			finish();
		} else {
			createMap();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_directions:
			showDirections();
			return true;
		case R.id.menu_item_user:
			mapController.animateTo(MapUtils.getUserLocation());
			return true;
		case R.id.menu_item_destination:
			mapController.animateTo(MapUtils.getDestination());
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle args) {
	}

	@Override
	public void onRestoreInstanceState(Bundle args) {
	}

	private void addOverlay(Overlay routeOverlay) {
		mapView.getOverlays().add(routeOverlay);
		mapController.animateTo(MapUtils.getDestination());
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (curTask.getStatus().equals(Status.FINISHED)) {
			return null;
		}
		curTask.detach();
		return curTask;

	}

	@SuppressWarnings("deprecation")
	private void createMap() {
		setContentView(R.layout.activity_maps);
		RelativeLayout holder = (RelativeLayout) findViewById(R.id.map_holder);
		if (!DEBUG.SIGNED) {
			mapView = new MapView(this, Constants.DEBUG_KEY);
		} else {
			mapView = new MapView(this, Constants.APPLICATION_KEY);
		}
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		holder.addView(mapView);
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(MapUtils.getUserLocation());
		BasicOverlay shopsOverlay = new BasicOverlay(GoogleMapsActivity.this
				.getResources().getDrawable(R.drawable.shop),
				GoogleMapsActivity.this);
		for (ShopList sl : ShopUtils.getShopLists()) {
			shopsOverlay.addOverlay(new OverlayItem(sl.getBranch()
					.getCityLocation().getGeoPoint(), sl.toString(), sl
					.getBranch().getCityLocation().toString()));
		}
		BasicOverlay iconOverlay = new BasicOverlay(GoogleMapsActivity.this
				.getResources().getDrawable(R.drawable.user),
				GoogleMapsActivity.this);
		iconOverlay.addOverlay(new OverlayItem(MapUtils.getUserLocation(),
				getString(R.string.you), getString(R.string.str_you)));
		mapView.getOverlays().add(shopsOverlay);
		mapView.getOverlays().add(iconOverlay);
		if (getLastNonConfigurationInstance() != null) {
			curTask = (BuildRouteTask) getLastNonConfigurationInstance();
			if (!curTask.getStatus().equals(Status.FINISHED)) {
				curTask.attach(this);
			}
		} else {
			getDirections();
		}

	}

	private void getDirections() {
		curTask = new BuildRouteTask(this);
		curTask.execute();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	public void showDirections() {
		if (MapUtils.directions != null && MapUtils.directions.size() > 0) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View directionsView = factory.inflate(
					R.layout.dialog_directions, null);
			final ListView dirList = (ListView) directionsView
					.findViewById(R.id.list_directions);
			DirectionsListAdapter adapter = new DirectionsListAdapter(this,
					MapUtils.directions);
			dirList.setAdapter(adapter);
			Builder dlg = DialogUtils.getDirectionsDialog(this);
			dlg.setView(directionsView);
			dirDlg = dlg.show();
		}

	}

	public void plotPosition(GeoPoint point, String title, String message) {
		if (mapView.getOverlays().size() > 3) {
			mapView.getOverlays().remove(mapView.getOverlays().size() - 1);
		}
		BasicOverlay iconOverlay = new BasicOverlay(GoogleMapsActivity.this
				.getResources().getDrawable(R.drawable.pin),
				GoogleMapsActivity.this);
		iconOverlay.addOverlay(new OverlayItem(point, title, message));
		mapView.getOverlays().add(iconOverlay);
		mapController.animateTo(point);
		if (dirDlg != null && dirDlg.isShowing()) {
			dirDlg.cancel();
		}

	}

	static class BuildRouteTask extends ProgressTask {
		private RouteOverlay routeOverlay;

		public BuildRouteTask(GoogleMapsActivity activity) {
			super(activity, activity.getString(R.string.retrieving) + "...",
					activity.getString(R.string.retrieving_msg));
		}

		@Override
		protected void success() {
			((GoogleMapsActivity) activity).addOverlay(routeOverlay);

		}

		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
			if (error_code.equals(ERROR.DIRECTIONS)) {
				dlg.setPositiveButton(activity.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
			} else {
				dlg.setPositiveButton(activity.getString(R.string.retry),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((GoogleMapsActivity) activity).getDirections();
								dialog.cancel();
							}
						});
				dlg.setNegativeButton(activity.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
			}
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.DIRECTIONS;
			}
			try {
				Route route = MapUtils.directions();
				routeOverlay = new RouteOverlay(route, Color.BLUE);
				MapUtils.addDirections(activity);
				return ERROR.SUCCESS;
			} catch (Exception e) {
				if (e != null && e.getMessage() != null) {
					if (DEBUG.ON) {
						Log.v(TAGS.MAP, e.getMessage());
						e.printStackTrace();
					}
				}
				return ERROR.MAP;
			}
		}
	}

}
