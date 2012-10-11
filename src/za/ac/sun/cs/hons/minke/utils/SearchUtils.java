package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.HashSet;

import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class SearchUtils {
	private static HashSet<City> addedCities;

	private static HashSet<Province> addedProvinces;

	private static ArrayList<Product> addedProducts;
	private static ArrayList<Category> addedCategories;

	private static HashSet<Country> addedCountries;

	private static ArrayList<Object> addedLocations;

	private static ArrayList<BranchProduct> searched;

	private static boolean productsActive;

	public static ArrayList<Object> getAddedLocations() {
		if (addedLocations == null) {
			addedLocations = new ArrayList<Object>();
			addedCities = new HashSet<City>();
			addedProvinces = new HashSet<Province>();
			addedCountries = new HashSet<Country>();
		}
		return addedLocations;
	}

	public static void removeLocation(int position) {
		Object location = addedLocations.remove(position);
		if (location instanceof City) {
			addedCities.remove((City) location);
		} else if (location instanceof Province) {
			addedProvinces.remove((Province) location);
		} else if (location instanceof Country) {
			addedCountries.remove((Country) location);

		}
	}

	public static void addLocation(Object location) {
		addedLocations.add(0, location);
		if (location instanceof City) {
			addedCities.add((City) location);
		} else if (location instanceof Province) {
			addedProvinces.add((Province) location);
		} else if (location instanceof Country) {
			addedCountries.add((Country) location);

		}
	}

	public static ArrayList<Product> getAddedProducts() {
		if (addedProducts == null) {
			addedProducts = new ArrayList<Product>();
		}
		return addedProducts;
	}

	public static HashSet<City> getAddedCities() {
		if (addedCities == null) {
			addedCities = new HashSet<City>();
		}
		return addedCities;
	}

	public static HashSet<Province> getAddedProvinces() {
		if (addedProvinces == null) {
			addedProvinces = new HashSet<Province>();
		}
		return addedProvinces;
	}

	public static HashSet<Country> getAddedCountries() {
		if (addedCountries == null) {
			addedCountries = new HashSet<Country>();
		}
		return addedCountries;
	}

	public static void removeProduct(int position) {
		addedProducts.remove(position);
	}
	public static void removeProduct(Product product) {
		addedProducts.remove(product);
	}
	public static void addProduct(Product product) {
		addedProducts.add(0,product);
	}

	public static ArrayList<Category> getAddedCategories() {
		if (addedCategories == null) {
			addedCategories = new ArrayList<Category>();
		}
		return addedCategories;
	}

	public static void removeCategory(int position) {
		addedCategories.remove(position);
	}
	
	public static void removeCategory(Category category) {
		addedCategories.remove(category);
	}

	public static void addCategory(Category category) {
		addedCategories.add(0,category);
	}

	public static boolean isProductsActive() {
		return productsActive;
	}

	public static void setProductsActive(boolean active) {
		productsActive = active;
	}

	public static ArrayList<BranchProduct> getSearched() {
		if (searched == null) {
			searched = new ArrayList<BranchProduct>();
		}
		return searched;
	}

	public static void setSearched(ArrayList<BranchProduct> _new) {
		searched = _new;
	}

	public static void removeLocation(Object loc) {
		if (addedLocations.remove(loc)) {
			if (loc instanceof City) {
				addedCities.remove((City) loc);
			} else if (loc instanceof Province) {
				addedProvinces.remove((Province) loc);
			} else if (loc instanceof Country) {
				addedCountries.remove((Country) loc);

			}
		}
	}

}
