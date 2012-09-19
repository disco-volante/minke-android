package za.ac.sun.cs.hons.minke.entities.product;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

/**
 * DB Entity used to store data about a specific {@link Product}, e.g. Milk,
 * Dairy, Consumables.
 * 
 * @author godfried
 * 
 */

public class Product extends IsEntity {
	private String name;
	private String brand;
	private double size;
	private String measurement;

	public Product() {
	}

	public Product(String name, String brand, double size, String measurement) {
		super();
		setName(name);
		setBrand(brand);
		setSize(size);
		setMeasurement(measurement);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand2) {
		this.brand = brand2;
	}

	public double getSize() {
		return size;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	@Override
	public String toString() {
		return brand + " " + name;
	}

}
