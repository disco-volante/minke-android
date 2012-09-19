package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.components.Place;
import com.nutiteq.components.Route;
import com.nutiteq.components.WgsPoint;

public class MapUtils {

	private static BasicMapComponent map;
	private static Route route;
	private static Place[] places;
	private static WgsPoint destination;
	private static ArrayList<IsEntity> branches;
	private static WgsPoint point;

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

	public static void setDestination(int position) {
		Branch b = (Branch) branches.get(position);
		destination = new WgsPoint(b.getCoords().getMinLongitude(), b
				.getCoords().getMinLatitude());
	}

	public static void setBranches(ArrayList<IsEntity> branches) {
		MapUtils.branches = branches;
	}

	public static ArrayList<IsEntity> getBranches() {
		return branches;
	}

	public static WgsPoint getDestination() {
		return destination;
	}

	public static void refreshLocation(LocationManager locationManager) {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			point = new WgsPoint(location.getLongitude(),
					location.getLatitude());
			Log.d("MAPUTILS", location.toString());
		} else {
			point = new WgsPoint(18.878612, -33.926617);
		}
	}

	public static WgsPoint getLocation() {
		return point;
	}

}
