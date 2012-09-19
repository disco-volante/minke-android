package za.ac.sun.cs.hons.minke.entities.product;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

public class Brand extends IsEntity {
	private String name;

	public Brand() {
	}

	public Brand(String name) {
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
