package za.ac.sun.cs.hons.minke.entities.store;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

/**
 * DB Entity used to store data about a specific store, e.g. Spar.
 * 
 * @author godfried
 * 
 */

public class Store extends IsEntity {
	private String name;

	public Store() {
	}

	/**
	 * Creates a new Store object.
	 * 
	 * @param ID
	 *            the object's ID.
	 * @param name
	 *            the object's name.
	 */
	public Store(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

}
