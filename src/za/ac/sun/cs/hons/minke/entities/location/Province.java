package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSArea;

public class Province extends Location {
	private String countryName;

	public Province() {
	}

	public Province(String name, String country, GPSArea coords) {
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
