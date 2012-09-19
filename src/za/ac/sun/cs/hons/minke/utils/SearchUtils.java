package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.List;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class SearchUtils {
	private static ArrayList<IsEntity> addedCities, addedLocations,
			addedCountries, addedProvinces, addedProducts, addedCategories;

	static List<IsEntity> searched;

	private static boolean productsActive;

	public static ArrayList<IsEntity> getAddedLocations(boolean reset) {
		if (reset || addedLocations == null) {
			addedLocations = new ArrayList<IsEntity>(100);
			addedCountries = new ArrayList<IsEntity>(100);
			addedCities = new ArrayList<IsEntity>(100);
			addedProvinces = new ArrayList<IsEntity>(100);
		}
		return addedLocations;
	}

	public static void removeLocation(int position) {
		addedLocations.remove(position);
		addedCountries.remove(position);
		addedProvinces.remove(position);
		addedCities.remove(position);
	}

	public static void addLocation(IsEntity location) {
		addedLocations.add(location);
		IsEntity city = null, province = null, country = null;
		if (location instanceof City) {
			city = location;
		} else if (location instanceof Province) {
			province = location;
		} else if (location instanceof Country) {
			country = location;
		}
		addedCities.add(city);
		addedProvinces.add(province);
		addedCountries.add(country);
	}

	public static ArrayList<IsEntity> getAddedProducts(boolean reset) {
		if (reset || addedProducts == null) {
			addedProducts = new ArrayList<IsEntity>();
		}
		return addedProducts;
	}
	public static List<IsEntity> getAddedCities() {
		return addedCountries;
	}
	public static List<IsEntity> getAddedProvinces() {
		return addedProvinces;
	}
	public static List<IsEntity> getAddedCountries() {
		return addedCities;
	}

	public static void removeProduct(int position) {
		addedProducts.remove(position);
	}

	public static void addProduct(Product product) {
		addedProducts.add(product);
	}

	public static ArrayList<IsEntity> getAddedCategories(boolean reset) {
		if (reset || addedCategories == null) {
			addedCategories = new ArrayList<IsEntity>();
		}
		return addedCategories;
	}

	public static void removeCategory(int position) {
		addedCategories.remove(position);
	}

	public static void addCategory(Category category) {
		addedCategories.add(category);
	}

	public static boolean isProductsActive() {
		return productsActive;
	}

	public static void setProductsActive(boolean active) {
		productsActive = active;
	}

	public static List<IsEntity> getSearched() {
		return searched;
	}

	public static void setSearched(List<IsEntity> bps) {
		searched = bps;
	}



}
