package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.List;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

public class BrowseUtils {

	private static ArrayList<IsEntity> branchProducts = new ArrayList<IsEntity>();
	private static boolean storeBrowse;

	public static void setBranchProducts(
			List<IsEntity> list) {
		branchProducts = new ArrayList<IsEntity>();
		if (list != null) {
			branchProducts.addAll(list);
		}
		
	}

	public static ArrayList<IsEntity> getBranchProducts() {
		return branchProducts;
	}

	public static boolean isStoreBrowse() {
		return storeBrowse;
	}

	public static void setStoreBrowse(boolean storeBrowse) {
		BrowseUtils.storeBrowse = storeBrowse;
	}
}
