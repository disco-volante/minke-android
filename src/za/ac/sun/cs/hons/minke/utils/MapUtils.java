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

public class MapUtils {

	private static BasicMapComponent map;
	private static Route route;
	private static Place[] places;
	private static GPSCoords destination;
	private static ArrayList<IsEntity> branches;
	private static GPSCoords point;

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
		destination = ((Branch) branches.get(position)).getCoords();
	}

	public static void setBranches(ArrayList<IsEntity> branches) {
		MapUtils.branches = branches;
	}

	public static ArrayList<IsEntity> getBranches() {
		return branches;
	}

	public static GPSCoords getDestination() {
		return destination;
	}

	public static int refreshLocation(LocationManager locationManager) {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			point = new GPSCoords(location.getLatitude(),
					location.getLongitude());
			Log.d("MAPUTILS", location.toString());
			return Constants.SUCCESS;
		} else if(Constants.EMULATOR){
			point = new GPSCoords(-33, 18);
			return Constants.SUCCESS;
		}
		return Constants.LOCATION_ERROR;
	}

	public static GPSCoords getLocation() {
		return point;
	}

}
