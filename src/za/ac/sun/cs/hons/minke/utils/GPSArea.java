package za.ac.sun.cs.hons.minke.utils;

public class GPSArea {
	private double lat,  lon;

	public GPSArea() {
	}

	public GPSArea(double latitude, double longitude) {
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

}
