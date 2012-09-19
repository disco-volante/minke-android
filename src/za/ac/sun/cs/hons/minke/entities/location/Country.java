package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSArea;
public class Country extends Location {

	public Country() {
	}

	public Country(String name, GPSArea coords) {
		super(name, coords);
	}

	@Override
	public String toString() {
		return getName();
	}

}
