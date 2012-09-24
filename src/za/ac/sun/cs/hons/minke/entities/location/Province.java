package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSCoords;

public class Province extends Location {
	private String countryName;

	public Province() {
	}

	public Province(String name, String country, GPSCoords coords) {
		super(name, coords);
		setCountry(country);
	}

	public void setCountry(String country) {
		this.countryName = country;
	}

	public String getCountry() {
		return countryName;
	}

	@Override
	public String toString() {
		return getName() + ", " + getCountry();
	}

}
