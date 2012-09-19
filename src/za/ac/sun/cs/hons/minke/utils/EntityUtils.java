package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;

public class EntityUtils {
	private static List<IsEntity> locations, categories, products, bps, brands;
	private static ArrayList<IsEntity> branches;
	private static HashMap<BranchProduct, ArrayList<DatePrice>> dps;
	private static BranchProduct scanned;

	public static ArrayList<DatePrice> getDatePrices(BranchProduct bp) {
		return dps.get(bp);
	}

	public static List<IsEntity> getCategories() {
		return categories;
	}

	public static void setCategories(List<IsEntity> categories) {
		EntityUtils.categories = categories;
	}

	public static List<IsEntity> getProducts() {
		return products;
	}

	public static void setProducts(List<IsEntity> products) {
		EntityUtils.products = products;
	}

	public static List<IsEntity> getBranchProducts() {
		return bps;
	}

	public static void setBranchProducts(List<IsEntity> bps) {
		EntityUtils.bps = bps;
	}

	public static ArrayList<IsEntity> getBranches() {
		return branches;
	}

	public static void setBranches(List<IsEntity> _new) {
		branches = new ArrayList<IsEntity>();
		branches.addAll(_new);

	}

	public static HashMap<BranchProduct, ArrayList<DatePrice>> getDps() {
		return dps;
	}

	public static void setDps(HashMap<BranchProduct, ArrayList<DatePrice>> dps) {
		EntityUtils.dps = dps;
	}

	public static List<IsEntity> getLocations() {
		return locations;
	}

	public static void setLocations(List<IsEntity> _new) {
		locations = _new;
	}

	public static void setScanned(IsEntity isEntity) {
		EntityUtils.scanned = (BranchProduct) isEntity;
	}

	public static BranchProduct getScanned() {
		return scanned;
	}

	public static List<IsEntity> getBrands() {
		return brands;
	}

	public static void setBrands(List<IsEntity> _new) {
		brands = _new;
	}
}
