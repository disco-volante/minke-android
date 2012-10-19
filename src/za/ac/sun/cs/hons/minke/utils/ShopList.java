package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class ShopList implements Comparable<ShopList> {

	private ArrayList<BranchProduct> branchProducts;
	private Branch branch;

	public ShopList(ArrayList<BranchProduct> branchProducts, Branch branch) {
		super();
		this.branchProducts = branchProducts;
		this.branch = branch;
	}

	public ArrayList<BranchProduct> getBranchProducts() {
		return branchProducts;
	}

	public void setBranchProducts(ArrayList<BranchProduct> branchProducts) {
		this.branchProducts = branchProducts;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		return branch.toString();
	}

	@Override
	public int compareTo(ShopList shop) {
		if (shop == null) {
			return 1;
		}
		if (branch == null) {
			return -1;
		}
		return branch.compareTo(shop.getBranch());
	}
}
