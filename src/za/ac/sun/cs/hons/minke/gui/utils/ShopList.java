package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class ShopList {

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

}
