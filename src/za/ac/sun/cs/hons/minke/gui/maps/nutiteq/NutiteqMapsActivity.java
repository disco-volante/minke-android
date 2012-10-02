package za.ac.sun.cs.hons.minke.gui.maps.nutiteq;

import java.io.IOException;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ZoomControls;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.android.MapView;
import com.nutiteq.components.Place;
import com.nutiteq.components.PlaceLabel;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.maps.OpenStreetMap;
import com.nutiteq.ui.ThreadDrivenPanning;
import com.nutiteq.wrappers.AppContext;
import com.nutiteq.wrappers.Image;

public class NutiteqMapsActivity extends Activity implements LocationListener {
	private BasicMapComponent mapComponent;
	private boolean onRetainCalled;
	private WgsPoint src;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		createNutiteq();
	}

	private void createNutiteq() {
		setContentView(R.layout.nutiteq_directions);
		mapComponent = new BasicMapComponent(Constants.KEY,
				new AppContext(this), 1, 1, src, 15);
		MapUtils.setMap(mapComponent);
		mapComponent.setMap(OpenStreetMap.MAPNIK);
		mapComponent.setPanningStrategy(new ThreadDrivenPanning());
		mapComponent.startMapping();
		try {
			Image branchIcon = Image
					.createImage("/res/drawable-hdpi/shop_40.png");
			for (Branch b : MapUtils.getBranches()) {
				PlaceLabel branchLabel = new PlaceLabel(b.toString());
				Place p = new Place(1, branchLabel, branchIcon, b
						.getCityLocation().getLon(), b.getCityLocation()
						.getLat());
				mapComponent.addPlace(p);
			}

			Image userIcon = Image
					.createImage("/res/drawable-hdpi/android_40.png");
			PlaceLabel userLabel = new PlaceLabel("Your Location");
			Place userPos = new Place(1, userLabel, userIcon, src);
			mapComponent.addPlace(userPos);
			if (src != null) {
				mapComponent.setMiddlePoint(src);
			}
			new NutiteqRouteWaiter(src, MapUtils.getDestinationWGS(),
					Constants.USER_ID, Constants.ROUTING_CLOUDMADE, this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mapComponent.setZoom(14);
		MapView mapView = (MapView) findViewById(R.id.mapview);
		mapView.setMapComponent(mapComponent);
		ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoomcontrols);
		zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				mapComponent.zoomIn();
			}
		});
		zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
			public void onClick(final View v) {
				mapComponent.zoomOut();
			}
		});
		onRetainCalled = false;

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		onRetainCalled = true;
		return mapComponent;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (!onRetainCalled) {
			mapComponent.stopMapping();
			mapComponent = null;
		}
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
		src = new WgsPoint(location.getLatitude(), location.getLongitude());
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
