package za.ac.sun.cs.hons.minke.utils.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class JSONBuilder {

	public static JSONObject ProductsToJSON(ArrayList<Product> products)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "products");
		JSONArray array = new JSONArray();
		for (Product p : products) {
			array.put(p.toJSON());
		}
		obj.put("products", array);
		return obj;

	}

	public static JSONObject toJSON(JSONObject... objs) throws JSONException {
		JSONObject all = new JSONObject();
		for (JSONObject obj : objs) {
			all.put(obj.getString("type"), obj);
		}
		return all;
	}

	public static JSONObject CountriesToJSON(ArrayList<Country> countries)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "countries");
		JSONArray array = new JSONArray();
		for (Country c : countries) {
			array.put(c.toJSON());
		}
		obj.put("countries", array);
		return obj;

	}

	public static JSONObject ProvincesToJSON(ArrayList<Province> provinces)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "provinces");
		JSONArray array = new JSONArray();
		for (Province p : provinces) {
			array.put(p.toJSON());
		}
		obj.put("provinces", array);
		return obj;

	}

	public static JSONObject CitiesToJSON(ArrayList<City> cities)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "cities");
		JSONArray array = new JSONArray();
		for (City c : cities) {
			array.put(c.toJSON());
		}
		obj.put("cities", array);
		return obj;

	}

	public static JSONObject CategoriesToJSON(ArrayList<Category> categories)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "categories");
		JSONArray array = new JSONArray();
		for (Category c : categories) {
			array.put(c.toJSON());
		}
		obj.put("categories", array);
		return obj;
	}

	public static JSONObject toJSON(String name, Object arg)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", name);
		obj.put(name, arg);
		return obj;
	}

	public static JSONObject BranchProductProtoToJSON(String name,
			String category, String brand, double size, String measure, int price)
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "branchProductProto");
		obj.put("name", name);
		obj.put("size", size);
		obj.put("price", price);
		obj.put("measure", measure);
		if (category != null) {
			obj.put("categoryName", category);
		}
		if (brand != null) {
			obj.put("brandName", brand);
		}
		return obj;
	}

	public static JSONObject BranchProtoToJSON(String name, String cityName,
			String storeName, double lon, double lat) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "branchProto");
		obj.put("name", name);
		obj.put("lon", lon);
		obj.put("lat", lat);
		if (cityName != null) {
			obj.put("cityName", cityName);
		}
		if (storeName != null) {
			obj.put("storeName", storeName);
		}
		return obj;
	}

}
