package za.ac.sun.cs.hons.minke.entities.location;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.utils.GPSCoords;


public class Location extends IsEntity {
	private GPSCoords coords;
	private String name;

	public Location() {
	};

	public Location(String name, GPSCoords coords) {
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

	public GPSCoords getCoords() {
		return coords;
	}

	public void setCoords(GPSCoords coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return getName();
	}

}
