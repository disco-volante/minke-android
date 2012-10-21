package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.nutiteq.BasicMapComponent;
import com.nutiteq.components.Place;
import com.nutiteq.components.Route;

public class MapUtils {

	private static BasicMapComponent map;
	private static Route route;
	private static Place[] places;
	private static GeoPoint destination;
	private static GeoPoint point;
	private static Branch branch;

	public static void setMap(BasicMapComponent mapComponent) {
		map = mapComponent;
	}

	public static BasicMapComponent getMap() {
		return map;
	}

	public static void setRoute(Route nroute) {
		route = nroute;
	}

	public static void setInstrutionPlaces(Place[] instructionPlaces) {
		places = instructionPlaces;
	}

	public static Route getRoute() {
		return route;
	}

	public static Place getInstructionPlace(int position) {
		return places[position];
	}

	public static void setDestination(ShopList shopList) {
		destination = shopList.getBranch().getCityLocation().getGeoPoint();
		if (DEBUG.ON) {
			Log.v(TAGS.MAP, "latitude = "
					+ shopList.getBranch().getCityLocation().getLat()
					+ " longitude = "
					+ shopList.getBranch().getCityLocation().getLon());
		}
	}

	public static GeoPoint getDestination() {
		return destination;
	}

	public static ERROR refreshLocation(LocationManager locationManager) {
		branch = null;
		if (point != null) {
			return ERROR.SUCCESS;
		}
		if (DEBUG.EMULATOR) {
			setUserLocation(-33,18);
			return ERROR.SUCCESS;
		}
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			setUserLocation(location.getLatitude(),
					location.getLongitude());
			if (DEBUG.ON) {
				Log.d("MAPUTILS", location.toString());
			}
			return ERROR.SUCCESS;
		}
		return ERROR.LOCATION;
	}

	public static GeoPoint getUserLocation() {
		return point;
	}
	
	public static double getUserLat() {
		return ((double) point.getLatitudeE6())/1E6;
	}
	
	public static double getUserLon() {
		return ((double) point.getLongitudeE6())/1E6;
	}

	public static Branch getUserBranch() {
		return branch;
	}

	public static void setUserBranch(Branch _branch) {
		branch = _branch;
	}

	public static void setUserLocation(double lat, double lon) {
		point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		EntityUtils.sortBranches();
	}

	public static double rad(double x) {
		return x * Math.PI / 180;
	}

	public static double dist(double sLat, double sLon, double lat, double lon) {
		int r = 6371;
		double dLat = rad(lat - sLat);
		double dLon = rad(lon - sLon);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(lat))
				* Math.cos(rad(lat)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return r * c;

	}

}
