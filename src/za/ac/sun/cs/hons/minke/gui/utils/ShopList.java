package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;

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
		if (shop == null || shop.getBranch() == null) {
			return 1;
		}
		if (branch == null) {
			return -1;
		}
		if (MapUtils.getUserBranch() == null
				|| MapUtils.getUserBranch().getCityLocation() == null) {
			return 0;
		}
		double dis0 = ShopUtils.dist(branch.getCityLocation().getLat(), branch
				.getCityLocation().getLon(), MapUtils.getUserBranch()
				.getCityLocation().getLat(), MapUtils.getUserBranch()
				.getCityLocation().getLon());
		double dis1 = ShopUtils.dist(shop.getBranch().getCityLocation()
				.getLat(), shop.getBranch().getCityLocation().getLon(),
				MapUtils.getUserBranch().getCityLocation().getLat(), MapUtils
						.getUserBranch().getCityLocation().getLon());

		if (dis0 > dis1) {
			return 1;
		} else if (dis1 > dis0) {
			return -1;
		}
		return 0;
	}
}
