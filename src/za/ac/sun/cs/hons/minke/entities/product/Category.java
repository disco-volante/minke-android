package za.ac.sun.cs.hons.minke.entities.product;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

/**
 * DB Entity used to store data about a {@link Product}'s categories, e.g. Milk,
 * Dairy, Consumables.
 * 
 * @author godfried
 * 
 */
public class Category extends IsEntity {
	private String name;

	public Category() {
	}

	public Category(String name) {
		super();
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;

	}

}
