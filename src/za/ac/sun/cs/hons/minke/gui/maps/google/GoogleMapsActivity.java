package za.ac.sun.cs.hons.minke.gui.maps.google;

import java.util.List;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class GoogleMapsActivity extends MapActivity implements LocationListener {
	private GeoPoint src;
	private LocationManager locationManager;
	private MapView mapView;
	private MapController mapController;
	private List<Segment> directions;

	class BuildRouteTask extends ProgressTask {
		private RouteOverlay routeOverlay;
		private GeoPoint dest = MapUtils.getDestination();

		public BuildRouteTask() {
			super(GoogleMapsActivity.this, "Retrieving...",
					"Retrieving route to store.");
		}

		@Override
		protected void success() {
			mapView.getOverlays().add(routeOverlay);
			mapController.animateTo(dest);
		}

		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(GoogleMapsActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							getDirections();
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			dlg.show();
		}

		@Override
		protected int retrieve() {
			try {
				Route route = directions(src, dest);
				routeOverlay = new RouteOverlay(route, Color.BLUE);
				return ERROR.SUCCESS;
			} catch (Exception e) {
				return ERROR.MAP;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		src = MapUtils.getLocation();
		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			Log.d(TAGS.LOCATION, location.toString());
			onLocationChanged(location);
		}
		createGoogle();
	}

	private void createGoogle() {
		setContentView(R.layout.google_directions);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setClickable(true);
		mapController = mapView.getController();
		mapController.setZoom(15);
		mapController.setCenter(src);
		BasicOverlay shopsOverlay = new BasicOverlay(GoogleMapsActivity.this
				.getResources().getDrawable(R.drawable.shop_40),
				GoogleMapsActivity.this);
		for (Branch b : EntityUtils.getBranches()) {
			shopsOverlay.addOverlay(new OverlayItem(b.getCityLocation()
					.getGeoPoint(), b.toString(), b.getCityLocation()
					.toString()));
		}
		BasicOverlay iconOverlay = new BasicOverlay(GoogleMapsActivity.this
				.getResources().getDrawable(R.drawable.android_40),
				GoogleMapsActivity.this);
		iconOverlay.addOverlay(new OverlayItem(src, "You",
				"You are currently here."));
		mapView.getOverlays().add(shopsOverlay);
		mapView.getOverlays().add(iconOverlay);
		getDirections();

	}

	private void getDirections() {
		BuildRouteTask task = new BuildRouteTask();
		task.execute();
	}

	private Route directions(GeoPoint start, GeoPoint dest) {
		RouteParser parser;
		String jsonURL = "http://maps.google.com/maps/api/directions/json?";
		final StringBuffer sBuf = new StringBuffer(jsonURL);
		sBuf.append("origin=");
		sBuf.append(start.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(start.getLongitudeE6() / 1E6);
		sBuf.append("&destination=");
		sBuf.append(dest.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(dest.getLongitudeE6() / 1E6);
		sBuf.append("&sensor=true&mode=driving");
		parser = new GoogleParser(sBuf.toString());
		Route r = parser.parse();
		directions = r.getSegments();
		return r;
	}

	@Override
	public void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000, 10, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAGS.LOCATION,
				"onLocationChanged with location " + location.toString());
		src = new GeoPoint((int) (location.getLatitude() * 1E6),
				(int) (location.getLongitude() * 1E6));
	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	public void getDirections(View view) {
		DialogUtils.getDirectionsDialog(this, directions, "Directions").show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getMapIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
