package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Location;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class SearchUtils {
	private static ArrayList<City> addedCities;

	private static ArrayList<Province> addedProvinces;

	private static ArrayList<Product> addedProducts;
	private static ArrayList<Category> addedCategories;

	private static ArrayList<Country> addedCountries;

	private static ArrayList<Location> addedLocations;

	private static ArrayList<BranchProduct> searched;

	private static boolean productsActive;

	public static ArrayList<Location> getAddedLocations(boolean reset) {
		if (reset || addedLocations == null) {
			addedLocations = new ArrayList<Location>(100);
			addedCountries = new ArrayList<Country>(100);
			addedCities = new ArrayList<City>(100);
			addedProvinces = new ArrayList<Province>(100);
		}
		return addedLocations;
	}

	public static void removeLocation(int position) {
		addedLocations.remove(position);
		addedCountries.remove(position);
		addedProvinces.remove(position);
		addedCities.remove(position);
	}

	public static void addLocation(Location location) {
		addedLocations.add(location);
		City city = null;
		Province province = null;
		Country country = null;
		if (location instanceof City) {
			city = (City) location;
		} else if (location instanceof Province) {
			province = (Province) location;
		} else if (location instanceof Country) {
			country = (Country) location;
		}
		addedCities.add(city);
		addedProvinces.add(province);
		addedCountries.add(country);
	}

	public static ArrayList<Product> getAddedProducts(boolean reset) {
		if (reset || addedProducts == null) {
			addedProducts = new ArrayList<Product>();
		}
		return addedProducts;
	}

	public static ArrayList<City> getAddedCities() {
		return addedCities;
	}

	public static ArrayList<Province> getAddedProvinces() {
		return addedProvinces;
	}

	public static ArrayList<Country> getAddedCountries() {
		return addedCountries;
	}

	public static void removeProduct(int position) {
		addedProducts.remove(position);
	}

	public static void addProduct(Product product) {
		addedProducts.add(product);
	}

	public static ArrayList<Category> getAddedCategories(boolean reset) {
		if (reset || addedCategories == null) {
			addedCategories = new ArrayList<Category>();
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

	public static ArrayList<BranchProduct> getSearched() {
		return searched;
	}

	public static void setSearched(ArrayList<BranchProduct> _new) {
		searched = _new;
	}

}
