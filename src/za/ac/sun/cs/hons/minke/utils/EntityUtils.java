package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import za.ac.sun.cs.hons.minke.db.BranchProductProvider;
import za.ac.sun.cs.hons.minke.db.BranchProvider;
import za.ac.sun.cs.hons.minke.db.BrandProvider;
import za.ac.sun.cs.hons.minke.db.CategoryProvider;
import za.ac.sun.cs.hons.minke.db.LocationProvider;
import za.ac.sun.cs.hons.minke.db.ProductProvider;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Location;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import android.content.Context;

public class EntityUtils {
	private static ArrayList<Location> locations;
	private static ArrayList<Category> categories;
	private static ArrayList<Product> products;
	private static ArrayList<BranchProduct> bps;
	private static ArrayList<Brand> brands;
	private static ArrayList<String> stores;
	private static ArrayList<City> cities;
	private static ArrayList<Country> countries;
	private static ArrayList<Province> provinces;
	private static ArrayList<Branch> branches;
	private static BranchProduct scanned;
	private static boolean loaded = false;
	private static Branch userBranch;
	private static boolean productsChanged;
	private static boolean catsChanged;
	private static boolean locsChanged;
	private static boolean branchesChanged;
	private static boolean brandsChanged;
	private static boolean bpsChanged;

	public static List<Category> getCategories() {
		if (categories == null) {
			categories = new ArrayList<Category>();
		}
		return categories;
	}

	public static void setCategories(ArrayList<Category> categories) {
		EntityUtils.categories = categories;
		catsChanged = true;
	}

	public static ArrayList<Product> getProducts() {
		if (products == null) {
			products = new ArrayList<Product>();
		}
		return products;
	}

	public static void setProducts(ArrayList<Product> products) {
		EntityUtils.products = products;
		productsChanged = true;
	}

	public static ArrayList<BranchProduct> getBranchProducts() {
		if (bps == null) {
			bps = new ArrayList<BranchProduct>();
		}
		return bps;
	}

	public static void setBranchProducts(ArrayList<BranchProduct> bps) {
		EntityUtils.bps = bps;
		bpsChanged = true;
	}

	public static ArrayList<Branch> getBranches() {
		if (branches == null) {
			branches = new ArrayList<Branch>();
		}
		return branches;
	}

	public static void setBranches(ArrayList<Branch> _new) {
		branches = new ArrayList<Branch>();
		branches.addAll(_new);
		stores = new ArrayList<String>();
		HashSet<String> tStores = new HashSet<String>();
		for (IsEntity e : branches) {
			Branch b = (Branch) e;
			if (!tStores.contains(b.getStore())) {
				tStores.add(b.getStore());
				stores.add(b.getStore());
			}
		}
		branchesChanged = true;
	}

	public static ArrayList<Location> getLocations() {
		if (locations == null) {
			locations = new ArrayList<Location>();
		}
		return locations;
	}

	public static void setLocations(ArrayList<Location> _new) {
		locations = _new;
		cities = new ArrayList<City>();
		provinces = new ArrayList<Province>();
		countries = new ArrayList<Country>();
		for (Location e : locations) {
			if (e instanceof City) {
				cities.add((City) e);
			} else if (e instanceof Province) {
				provinces.add((Province) e);
			} else if (e instanceof Country) {
				countries.add((Country) e);
			}
		}
		locsChanged = true;
	}

	public static void setScanned(BranchProduct bp) {
		EntityUtils.scanned = bp;
	}

	public static BranchProduct getScanned() {
		if (scanned == null) {
			scanned = new BranchProduct();
		}
		return scanned;
	}

	public static ArrayList<Brand> getBrands() {
		if (brands == null) {
			brands = new ArrayList<Brand>();
		}
		return brands;
	}

	public static void setBrands(ArrayList<Brand> _new) {
		brands = _new;
		brandsChanged = true;
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void setLoaded(boolean _loaded) {
		loaded = _loaded;
	}

	public static ArrayList<String> getStores() {
		return stores;
	}

	public static ArrayList<City> getCities() {
		return cities;

	}

	public static void setUserBranch(Branch branch) {
		userBranch = branch;
	}

	public static Branch getUserBranch() {
		return userBranch;
	}

	public static ArrayList<Province> getProvinces() {
		return provinces;
	}

	public static ArrayList<Country> getCountries() {
		return countries;
	}

	public static void addBranch(Branch branch) {
		branches.add(0, branch);
		branchesChanged = true;
	}

	public static int loadLocalData(Context context) {
		try {
			ProductProvider prd = new ProductProvider(context);
			setProducts(prd.getAll());
			prd.close();
			CategoryProvider ca = new CategoryProvider(context);
			setCategories(categories = ca.getAll());
			ca.close();
			LocationProvider l = new LocationProvider(context);
			setLocations(l.getAll());
			l.close();
			BranchProvider b = new BranchProvider(context);
			setBranches(b.getAll());
			b.close();
			BrandProvider br = new BrandProvider(context);
			setBrands(br.getAll());
			br.close();
			BranchProductProvider bp = new BranchProductProvider(context);
			setBranchProducts(bp.getAll());
			bp.close();
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.DB_ERROR;
		}
		return Constants.SUCCESS;
	}

	public static int storeData(Context context) {
		try {
			if (productsChanged) {
				ProductProvider prd = new ProductProvider(context);
				prd.addAll(products);
				prd.close();
				productsChanged = false;
			}
			if (catsChanged) {
				CategoryProvider ca = new CategoryProvider(context);
				ca.addAll(categories);
				ca.close();
				catsChanged = false;
			}
			if (locsChanged) {
				LocationProvider l = new LocationProvider(context);
				l.addAll(locations);
				l.close();
				locsChanged = false;
			}
			if (branchesChanged) {
				BranchProvider b = new BranchProvider(context);
				b.addAll(branches);
				b.close();
				branchesChanged = false;
			}
			if (bpsChanged) {
				BranchProductProvider bp = new BranchProductProvider(context);
				bp.addAll(bps);
				bp.close();
				bpsChanged = false;
			}
			if (brandsChanged) {
				BrandProvider b = new BrandProvider(context);
				b.addAll(brands);
				b.close();
				brandsChanged = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Constants.DB_ERROR;
		}
		return Constants.SUCCESS;
	}

}
