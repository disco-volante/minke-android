package za.ac.sun.cs.hons.minke.util;

public class GPSArea {
	private double minLat, maxLat, minLong, maxLong;

	public GPSArea() {
	}

	public GPSArea(double latitude, double longitude) {
		this.minLat = latitude;
		this.maxLat = latitude;
		this.minLong = longitude;
		this.maxLong = longitude;
	}

	public GPSArea(double minLat, double maxLat, double minLong, double maxLong) {
		this.minLat = minLat;
		this.maxLat = maxLat;
		this.minLong = minLong;
		this.maxLong = maxLong;
	}

	public double getMinLatitude() {
		return minLat;
	}

	public double getMinLongitude() {
		return minLong;
	}

	public double getMaxLatitude() {
		return maxLat;
	}

	public double getMaxLongitude() {
		return maxLong;
	}

	public boolean inArea(GPSArea area) {
		return area.getMinLatitude() > minLat || area.getMaxLatitude() < maxLat
				|| area.getMinLongitude() > minLong
				|| area.getMaxLongitude() < maxLong;
	}

	public String toString() {
		return "Latitude: " + minLat + " -> " + maxLat + "; Latitude: "
				+ minLong + " -> " + maxLong;

	}

	public double distanceTo(double latitude, double longitude) {
		return Math.abs(minLat - latitude) + Math.abs(minLong - longitude);
	}

}
