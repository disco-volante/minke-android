package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.ShopList;

public class ShopUtils {
	private static ArrayList<Product> addedProducts;
	private static ArrayList<BranchProduct> searched;
	private static boolean productsActive;
	private static ArrayList<ShopList> shoplists;
	private static ArrayList<Branch> branches;

	public static ArrayList<Product> getAddedProducts(boolean reset) {
		if (reset || addedProducts == null) {
			addedProducts = new ArrayList<Product>();
		}
		return addedProducts;
	}

	public static void removeProduct(int position) {
		addedProducts.remove(position);
	}

	public static void addProduct(Product product) {
		addedProducts.add(product);
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

	public static void setSearched(ArrayList<BranchProduct> nsearched) {
		searched = nsearched;
	}

	public static double getTotal(ArrayList<BranchProduct> bps) {
		if (bps == null) {
			return 0;
		}
		double total = 0;
		for (BranchProduct bp : bps) {
			total += bp.getDatePrice().getPrice();
		}
		return total / 100;
	}

	public static ArrayList<ShopList> getShopLists() {
		return shoplists;
	}

	public static ArrayList<Branch> getBranches() {
		return branches;
	}

	public static void setShopLists(
			HashMap<Branch, ArrayList<BranchProduct>> branchMap) {
		shoplists = new ArrayList<ShopList>();
		for (Entry<Branch, ArrayList<BranchProduct>> entry : branchMap
				.entrySet()) {
			shoplists.add(new ShopList(entry.getValue(), entry.getKey()));
		}
		branches = new ArrayList<Branch>();
		branches.addAll(branchMap.keySet());
	}
}
