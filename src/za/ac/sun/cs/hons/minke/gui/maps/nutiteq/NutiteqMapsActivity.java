package za.ac.sun.cs.hons.minke.gui.maps.nutiteq;

import java.io.IOException;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.Constants;
import za.ac.sun.cs.hons.minke.utils.GPSCoords;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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
	private GPSCoords src;
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
			Log.d(Constants.DIR_TAG, location.toString());
			onLocationChanged(location);
		}
			createNutiteq();
	}

	private void createNutiteq() {
		setContentView(R.layout.nutiteq_directions);
		WgsPoint center = GPSCoords.toWgsPoint(src);
		mapComponent = new BasicMapComponent(Constants.KEY,
				new AppContext(this), 1, 1, center, 15);
		MapUtils.setMap(mapComponent);
		mapComponent.setMap(OpenStreetMap.MAPNIK);
		mapComponent.setPanningStrategy(new ThreadDrivenPanning());
		mapComponent.startMapping();
		try {
			Image branchIcon = Image
					.createImage("/res/drawable-hdpi/shop_40.png");
			for (IsEntity b : MapUtils.getBranches()) {
				PlaceLabel branchLabel = new PlaceLabel(b.toString());
				Place p = new Place(1, branchLabel, branchIcon, ((Branch) b)
						.getCoords().getLongitude(), ((Branch) b).getCoords()
						.getLatitude());
				mapComponent.addPlace(p);
			}

			Image userIcon = Image
					.createImage("/res/drawable-hdpi/android_40.png");
			PlaceLabel userLabel = new PlaceLabel("Your Location");
			Place userPos = new Place(1, userLabel, userIcon, center);
			mapComponent.addPlace(userPos);
			if (src != null) {
				mapComponent.setMiddlePoint(center);
			}
			new NutiteqRouteWaiter(center, GPSCoords.toWgsPoint(MapUtils
					.getDestination()), Constants.USER_ID,
					Constants.ROUTING_CLOUDMADE, this);
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
		Log.d(Constants.DIR_TAG,
				"onLocationChanged with location " + location.toString());
		src = new GPSCoords(location.getLatitude(), location.getLongitude());
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

}
