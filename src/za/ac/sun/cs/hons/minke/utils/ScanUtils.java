package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;

public class ScanUtils {
	private static Product product;
	private static BranchProduct branchProduct;
	private static long barCode;

	public static Product getProduct() {
		return product;
	}

	public static void setProduct(Product product) {
		ScanUtils.product = product;
	}

	public static BranchProduct getBranchProduct() {
		return branchProduct;
	}

	public static void setBranchProduct(BranchProduct branchProduct) {
		ScanUtils.branchProduct = branchProduct;
		if (branchProduct != null) {
			BrowseUtils.setBranchProducts(EntityUtils.retrieveBranchProducts(
					branchProduct.getProductId(), branchProduct));
		}
	}

	public static void setBarCode(long _barCode) {
		barCode = _barCode;
	}

	public static long getBarCode() {
		return barCode;
	}

}
