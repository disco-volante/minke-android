package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.util.GPSArea;


public class Location extends IsEntity {
	private GPSArea coords;
	private String name;

	public Location() {
	};

	public Location(String name, GPSArea coords) {
		super();
		this.setName(name);
		this.setCoords(coords);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GPSArea getCoords() {
		return coords;
	}

	public void setCoords(GPSArea coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return getName();
	}

}
