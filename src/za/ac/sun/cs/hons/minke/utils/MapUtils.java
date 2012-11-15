package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.Collections;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.routing.Route;
import com.routing.Segment;
import com.routing.parser.GoogleParser;
import com.routing.parser.RouteParser;

public class MapUtils {

	private static GeoPoint dest;
	private static GeoPoint user;
	private static Branch branch;
	public static ArrayList<Segment> directions;
	private static boolean locsChanged;
	private static Route r;

	public static void setDestination(CityLocation cl) {
		dest = cl.getGeoPoint();
		if (DEBUG.ON) {
			Log.v(TAGS.MAP, "latitude = "
					+ cl.getLat()
					+ " longitude = "
					+ cl.getLon());
		}
		locsChanged = true;
	}

	public static GeoPoint getDestination() {
		return dest;
	}

	public static ERROR refreshLocation(LocationManager locationManager) {
		locsChanged = true;
		if (DEBUG.EMULATOR) {
			setUserLocation(-33, 18);
			return ERROR.SUCCESS;
		}
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		if (location != null) {
			setUserLocation(location.getLatitude(), location.getLongitude());
			if (DEBUG.ON) {
				Log.d("MAPUTILS", location.toString());
			}
			return ERROR.SUCCESS;
		}
		return ERROR.LOCATION;
	}

	public static GeoPoint getUserLocation() {
		return user;
	}

	public static double getUserLat() {
		return user.getLatitudeE6() / 1E6;
	}

	public static double getUserLon() {
		return user.getLongitudeE6() / 1E6;
	}

	public static Branch getUserBranch() {
		return branch;
	}

	public static void setUserBranch(Branch _branch) {
		branch = _branch;
	}

	public static void setUserLocation(double lat, double lon) {
		user = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		locsChanged = true;
		
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

	public static Route directions() {
		if (r != null && directions != null && !locsChanged) {
			return r;
		}
		RouteParser parser;
		String jsonURL = "http://maps.google.com/maps/api/directions/json?";
		final StringBuffer sBuf = new StringBuffer(jsonURL);
		sBuf.append("origin=");
		sBuf.append(user.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(user.getLongitudeE6() / 1E6);
		sBuf.append("&destination=");
		sBuf.append(dest.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(dest.getLongitudeE6() / 1E6);
		sBuf.append("&sensor=true&mode=driving");
		parser = new GoogleParser(sBuf.toString());
		r = parser.parse();
		directions = r.getSegments();
		return r;
	}

	private static String getDist(int length) {
		if (length > 1000) {
			double actual = ((double) length) / 1000;
			return actual + "km.\n";
		} else {
			return length + "m.\n";
		}
	}

	public static void addDirections(Context context) {
		if (locsChanged && directions != null && directions.size() > 0) {
			int i = 0;
			double prev = 0.0;
			for (Segment s : directions) {
				i++;
				StringBuilder msg = new StringBuilder();
				msg.append(i);
				msg.append(") ");
				msg.append(prev + " -> " + s.getDistance());
				msg.append(" km : ");
				msg.append(s.getInstruction());
				msg.append(context.getString(R.string.str_continue));
				msg.append(getDist(s.getLength()));
				prev = s.getDistance();
				while (true) {
					int pos = getMistakes(msg.toString().toCharArray());
					if (pos == msg.length()) {
						break;
					}
					msg.insert(pos, ". ");
				}
				s.setMessage(msg.toString());
			}
		}
		locsChanged = false;
	}

	private static int getMistakes(char[] chars) {
		char c0 = ' ';
		int pos = 0;
		for (char c1 : chars) {
			if (c0 != ' '
					&& (Character.isLowerCase(c0) && Character.isUpperCase(c1))
					|| (Character.isDigit(c0) && Character.isUpperCase(c1))) {
				return pos;
			}
			c0 = c1;
			pos++;
		}
		return chars.length;
	}

	public static City changeCity(Context context){
		City city = null;
		ArrayList<Branch> branches = EntityUtils.getBranches(context);
		if (branches == null || branches.size() == 0) {
			return null;
		}
		Collections.sort(branches);
		int pos = 0;
		while (city == null) {
			if (pos == branches.size()) {
				return null;
			}
			if (branches.get(pos) != null
					&& branches.get(pos).getCityLocation() != null
					&& branches.get(pos).getCityLocation().getCity() != null) {
				city = branches.get(pos).getCityLocation().getCity();
				break;
			}
			pos++;
		}
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		prefs.edit().putLong("cityID", city.getId()).commit();
		return city;
	}
	

}
