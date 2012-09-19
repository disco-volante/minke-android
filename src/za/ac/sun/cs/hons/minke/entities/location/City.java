package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.util.GPSArea;

public class City extends Location {
	private String provinceName, countryName;

	public City() {
	}

	public City(String name, String province, String country, GPSArea coords) {
		super(name, coords);
		setProvince(province);
		setCountry(country);
	}

	public void setProvince(String province) {
		this.provinceName = province;
	}

	public String getProvince() {
		return provinceName;
	}

	public void setCountry(String country) {
		this.countryName = country;
	}

	public String getCountry() {
		return countryName;
	}

	@Override
	public String toString() {
		return getName() + ", " + getProvince() + ", " + getCountry();
	}

}
