package za.ac.sun.cs.hons.minke.utils;

import com.google.android.maps.GeoPoint;
import com.nutiteq.components.WgsPoint;

public class GPSCoords {
	private double lat,  lon;

	public GPSCoords() {
	}

	public GPSCoords(double latitude, double longitude) {
		this.lat = latitude;
		this.lon = longitude;
	}

	

	public double getLatitude() {
		return lat;
	}

	public double getLongitude() {
		return lon;
	}

	

	@Override
	public String toString(){
		return "Latitude: "+lat+"; Longitude: "+lon+";";

	}

	public double distanceTo(double latitude, double longitude) {
		return Math.abs(lat - latitude) + Math.abs(lon - longitude);
	}

	public static WgsPoint toWgsPoint(GPSCoords src) {
		return new WgsPoint(src.getLongitude(), src.getLatitude());
	}
	public static GeoPoint toGeoPoint(GPSCoords src) {
		return new GeoPoint((int)(src.getLatitude()* 1E6),(int)( src.getLongitude()* 1E6));
	}
}
