package za.ac.sun.cs.hons.minke.utils;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class JSONBuilderTest {

	@Test
	public void productTest() throws JSONException {
		Product p = getDummyProduct();
		JSONObject obj = new JSONObject();
		obj.put("type", "product");
		obj.put("id", p.getId());
		obj.put("name", p.getName());
		obj.put("brandId", p.getBrandId());
		obj.put("measure", p.getMeasure());
		obj.put("size", p.getSize());
		assertEquals(obj, p.toJSON());
	}

	private Product getDummyProduct() {
		Brand b = getDummyBrand();
		String name = getDummyString();
		double size = getDummyDouble();
		String measure = getDummyString();
		long id = ScanUtils.getDummyCode();
		Product p = new Product(id, name, b.getId(), size, measure);
		p.setBrand(b);
		return p;
	}

	private long getDummyLong() {
		return (long)(Math.random()*10000);
	}

	private double getDummyDouble() {
		return Math.random()*10000;
	}

	private String getDummyString() {
		return "DUMMY";
	}

	private Brand getDummyBrand() {
		return new Brand(getDummyLong(), getDummyString());
	}

}
