package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class ShopUtils {
	private static ArrayList<Product> addedProducts;
	private static ArrayList<BranchProduct> searched;
	private static boolean productsActive;

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
			total +=  bp.getPrice();
		}
		return total;
	}
}
