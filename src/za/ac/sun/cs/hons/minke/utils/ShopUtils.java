package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class ShopUtils {
	private static ArrayList<Product> addedProducts;
	private static ArrayList<BranchProduct> searched;
	private static boolean productsActive;
	private static ArrayList<ShopList> shoplists;

	public static ArrayList<Product> getAddedProducts() {
		if (addedProducts == null) {
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
			total += bp.getDatePrice().getPrice() * bp.getQuantity();
		}
		return total / 100;
	}

	public static ArrayList<ShopList> getShopLists() {
		return shoplists;
	}

	public static void setShopLists(
			HashMap<Branch, ArrayList<BranchProduct>> branchMap) {
		shoplists = new ArrayList<ShopList>();
		if (branchMap != null) {
			for (Entry<Branch, ArrayList<BranchProduct>> entry : branchMap
					.entrySet()) {
				shoplists.add(new ShopList(entry.getValue(), entry.getKey()));
			}
			Collections.sort(shoplists);
		}
	}

	public static void removeProduct(Product product) {
		addedProducts.remove(product);
	}
}
