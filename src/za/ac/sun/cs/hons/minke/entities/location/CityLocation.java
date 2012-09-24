package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSCoords;

public class CityLocation extends Location {
	private String city;

	public CityLocation() {
	}

	public CityLocation(String name, String city, GPSCoords coords) {
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
