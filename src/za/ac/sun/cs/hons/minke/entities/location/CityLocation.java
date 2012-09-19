package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSArea;

public class CityLocation extends Location {
	private String city;

	public CityLocation() {
	}

	public CityLocation(String name, String city, GPSArea coords) {
		super(name, coords);
		setCity(city);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return getName() + ", " + getCity();
	}

}
