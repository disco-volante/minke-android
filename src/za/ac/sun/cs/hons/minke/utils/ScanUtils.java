package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class ScanUtils {
	private static Product product;
	private static BranchProduct branchProduct;

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
		BrowseUtils.setBranchProducts(EntityUtils.retrieveBranchProducts(branchProduct.getProductId(),branchProduct));
	}

	public static void setBranch(Branch b) {
		// TODO Auto-generated method stub
		
	}

}
