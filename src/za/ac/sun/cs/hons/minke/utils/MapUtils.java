package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
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
	private static ArrayList<Branch> branches;
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
		destination = shopList.getBranch().getCityLocation()
				.getGeoPoint();
		if (Debug.ON) {
			Log.v(TAGS.MAP,
					"latitude = "
							+shopList.getBranch().getCityLocation().getLat()
							+ " longitude = "
							+ shopList.getBranch().getCityLocation().getLon());
		}
	}

	public static void setBranches(ArrayList<Branch> _branches) {
		branches = _branches;
	}



	public static ArrayList<Branch> getBranches() {
		return branches;
	}

	public static GeoPoint getDestination() {
		return destination;
	}

	public static ERROR refreshLocation(LocationManager locationManager) {
		branch = null;
		if (point != null) {
			return ERROR.SUCCESS;
		}
		if (Debug.EMULATOR) {
			point = new GeoPoint((int) (-33 * 1E6), (int) (18 * 1E6));
			return ERROR.SUCCESS;
		}
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			point = new GeoPoint((int) (location.getLatitude() * 1E6),
					(int) (location.getLongitude() * 1E6));
			if (Debug.ON) {
				Log.d("MAPUTILS", location.toString());
			}
			return ERROR.SUCCESS;
		}
		return ERROR.LOCATION;
	}

	public static GeoPoint getLocation() {
		return point;
	}

	public static Branch getUserBranch() {
		return branch;
	}

	public static void setUserBranch(Branch _branch) {
		branch = _branch;
	}

	public static void setLocation(float lastLat, float lastLong) {
		point = new GeoPoint((int) (lastLat * 1E6), (int) (lastLong * 1E6));
		branch = null;
	}

}
