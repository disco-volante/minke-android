package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class EntityUtils {
	private static List<IsEntity> locations, categories, products, bps, brands;
	private static ArrayList<String> stores;
	private static ArrayList<City> cities;
	private static ArrayList<Country> countries;
	private static ArrayList<Province> provinces;

	private static ArrayList<IsEntity> branches;
	private static HashMap<BranchProduct, ArrayList<DatePrice>> dps;
	private static BranchProduct scanned;
	private static boolean loaded = false;
	private static Branch userBranch;

	public static ArrayList<DatePrice> getDatePrices(BranchProduct bp) {
		if (dps == null || dps.get(bp) == null) {
			return new ArrayList<DatePrice>();
		}
		return dps.get(bp);
	}

	public static List<IsEntity> getCategories() {
		if (categories == null) {
			categories = new ArrayList<IsEntity>();
		}
		return categories;
	}

	public static void setCategories(List<IsEntity> categories) {
		EntityUtils.categories = categories;
	}

	public static List<IsEntity> getProducts() {
		if (products == null) {
			products = new ArrayList<IsEntity>();
		}
		return products;
	}

	public static void setProducts(List<IsEntity> products) {
		EntityUtils.products = products;
	}

	public static List<IsEntity> getBranchProducts() {
		if (bps == null) {
			bps = new ArrayList<IsEntity>();
		}
		return bps;
	}

	public static void setBranchProducts(List<IsEntity> bps) {
		EntityUtils.bps = bps;
	}

	public static ArrayList<IsEntity> getBranches() {
		if (branches == null) {
			branches = new ArrayList<IsEntity>();
		}
		return branches;
	}

	public static void setBranches(List<IsEntity> _new) {
		branches = new ArrayList<IsEntity>();
		branches.addAll(_new);
		stores = new ArrayList<String>();
		HashSet<String> tStores = new HashSet<String>();
		for (IsEntity e : branches) {
			Branch b = (Branch) e;
			if (!tStores.contains(b.getStore())) {
				tStores.add(b.getStore());
				stores.add(b.getStore());
			}
		}

	}

	public static HashMap<BranchProduct, ArrayList<DatePrice>> getDps() {
		if (dps == null) {
			dps = new HashMap<BranchProduct, ArrayList<DatePrice>>();
		}
		return dps;
	}

	public static void setDps(HashMap<BranchProduct, ArrayList<DatePrice>> dps) {
		EntityUtils.dps = dps;
	}

	public static List<IsEntity> getLocations() {
		if (locations == null) {
			locations = new ArrayList<IsEntity>();
		}
		return locations;
	}

	public static void setLocations(List<IsEntity> _new) {
		locations = _new;
		cities = new ArrayList<City>();
		provinces = new ArrayList<Province>();
		countries = new ArrayList<Country>();
		for (IsEntity e : locations) {
			if (e instanceof City) {
				cities.add((City) e);
			} else if (e instanceof Province) {
				provinces.add((Province) e);
			} else if (e instanceof Country) {
				countries.add((Country) e);
			}
		}
	}

	public static void setScanned(IsEntity isEntity) {
		EntityUtils.scanned = (BranchProduct) isEntity;
	}

	public static BranchProduct getScanned() {
		if (scanned == null) {
			scanned = new BranchProduct();
		}
		return scanned;
	}

	public static List<IsEntity> getBrands() {
		if (brands == null) {
			brands = new ArrayList<IsEntity>();
		}
		return brands;
	}

	public static void setBrands(List<IsEntity> _new) {
		brands = _new;
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void setLoaded(boolean _loaded) {
		loaded = _loaded;
	}

	public static ArrayList<String> getStores() {
		return stores;
	}

	public static ArrayList<City> getCities() {
		return cities;

	}

	public static void setUserBranch(Branch branch) {
		userBranch = branch;
	}

	public static Branch getUserBranch() {
		return userBranch;
	}

	public static ArrayList<Province> getProvinces() {
		return provinces;
	}

	public static ArrayList<Country> getCountries() {
		return countries;
	}

	public static void addBranch(IsEntity branch) {
		branches.add(0, branch);
	}

}
