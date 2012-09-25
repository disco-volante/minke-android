package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;

public class BrowseUtils {

	private static ArrayList<BranchProduct> branchProducts = new ArrayList<BranchProduct>();
	private static boolean storeBrowse;

	public static void setBranchProducts(
			ArrayList<BranchProduct> bps) {
		if (bps != null) {
			branchProducts = bps;
		}else{
			branchProducts = new ArrayList<BranchProduct>();
		}
		
	}

	public static ArrayList<BranchProduct> getBranchProducts() {
		if(branchProducts == null){
			branchProducts = new  ArrayList<BranchProduct>();
		}
		return branchProducts;
	}

	public static boolean isStoreBrowse() {
		return storeBrowse;
	}

	public static void setStoreBrowse(boolean storeBrowse) {
		BrowseUtils.storeBrowse = storeBrowse;
	}
}
