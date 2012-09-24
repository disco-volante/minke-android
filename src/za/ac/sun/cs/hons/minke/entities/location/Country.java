package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.utils.GPSCoords;
public class Country extends Location {

	public Country() {
	}

	public Country(String name, GPSCoords coords) {
		super(name, coords);
	}

	@Override
	public String toString() {
		return getName();
	}

}
