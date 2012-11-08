package za.ac.sun.cs.hons.minke.entities.product;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * DB Entity used to store data about a specific {@link Product}, e.g. Milk,
 * Dairy, Consumables.
 * 
 * @author godfried
 * 
 */

public class Product {
	private long id, brandId;
	private String name, measure;
	private double size;
	private Brand brand;
	private int quantity = 1;

	public Product() {
	}

	public Product(long id, String name, long brandId, double size,
			String measure) {
		setId(id);
		setBrandId(brandId);
		setName(name);
		setSize(size);
		setMeasure(measure);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSize() {
		return size;
	}

	public String getMeasure() {
		return measure;
	}

	public CharSequence getSizeString() {
		int intified = (int) size;
		if (size / intified != 1) {
			return size + " " + measure;
		}
		return intified + " " + measure;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	@Override
	public String toString() {
		if (brand == null) {
			return getSizeString() + " " + name;
		}
		return getSizeString() + " " + brand.toString() + " " + name;

	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "product");
		obj.put("id", id);
		obj.put("name", name);
		obj.put("brandId", brandId);
		obj.put("measure", measure);
		obj.put("size", size);
		return obj;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + (int) (brandId ^ (brandId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((measure == null) ? 0 : measure.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(size);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (brandId != other.brandId)
			return false;
		if (id != other.id)
			return false;
		if (measure == null) {
			if (other.measure != null)
				return false;
		} else if (!measure.equals(other.measure))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name)){
			return false;
		}
		if (Double.doubleToLongBits(size) != Double
				.doubleToLongBits(other.size)){
			return false;
		}
		return true;
	}

	public void setQuantity(int _quantity) {
		this.quantity = _quantity;
	}

	public int getQuantity() {
		return quantity;
	}

}
