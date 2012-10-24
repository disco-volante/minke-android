package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import za.ac.sun.cs.hons.minke.db.dao.BranchDAO;
import za.ac.sun.cs.hons.minke.db.dao.BranchProductDAO;
import za.ac.sun.cs.hons.minke.db.dao.BrandDAO;
import za.ac.sun.cs.hons.minke.db.dao.CategoryDAO;
import za.ac.sun.cs.hons.minke.db.dao.CityDAO;
import za.ac.sun.cs.hons.minke.db.dao.CityLocationDAO;
import za.ac.sun.cs.hons.minke.db.dao.CountryDAO;
import za.ac.sun.cs.hons.minke.db.dao.DatePriceDAO;
import za.ac.sun.cs.hons.minke.db.dao.ProductCategoryDAO;
import za.ac.sun.cs.hons.minke.db.dao.ProductDAO;
import za.ac.sun.cs.hons.minke.db.dao.ProvinceDAO;
import za.ac.sun.cs.hons.minke.db.dao.StoreDAO;
import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.product.ProductCategory;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.content.Context;
import android.util.Log;

public class EntityUtils {
	private static ArrayList<Category> categories;
	private static ArrayList<Product> products;
	private static ArrayList<Brand> brands;
	private static ArrayList<Store> stores;
	private static ArrayList<City> cities;
	private static ArrayList<Country> countries;
	private static ArrayList<Province> provinces;
	private static ArrayList<Branch> branches;
	private static boolean loaded = false;
	private static ArrayList<Object> locs;
	private static BaseDBHelper dbHelper;
	private static CountryDAO countryDAO;
	private static StoreDAO storeDAO;
	private static CategoryDAO categoryDAO;
	private static BrandDAO brandDAO;
	private static DatePriceDAO datePriceDAO;
	private static ProvinceDAO provinceDAO;
	private static CityDAO cityDAO;
	private static CityLocationDAO cityLocationDAO;
	private static ProductDAO productDAO;
	private static BranchDAO branchDAO;
	private static BranchProductDAO branchProductDAO;
	private static ProductCategoryDAO productCategoryDAO;

	public static ERROR init(Context context) {
			dbHelper = new BaseDBHelper(context);
			countryDAO = new CountryDAO(dbHelper);
			storeDAO = new StoreDAO(dbHelper);
			categoryDAO = new CategoryDAO(dbHelper);
			brandDAO = new BrandDAO(dbHelper);
			datePriceDAO = new DatePriceDAO(dbHelper);
			provinceDAO = new ProvinceDAO(dbHelper, countryDAO);
			cityDAO = new CityDAO(dbHelper, provinceDAO);
			cityLocationDAO = new CityLocationDAO(dbHelper, cityDAO);
			productDAO = new ProductDAO(dbHelper, brandDAO);
			branchDAO = new BranchDAO(dbHelper, storeDAO, cityLocationDAO);
			branchProductDAO = new BranchProductDAO(dbHelper, datePriceDAO,
					productDAO, branchDAO);
			productCategoryDAO = new ProductCategoryDAO(dbHelper, productDAO,
					categoryDAO);
		return ERROR.SUCCESS;
	}

	public static List<Category> getCategories() {
		return categories;
	}

	public static void setCategories(ArrayList<Category> categories) {
		EntityUtils.categories = categories;
	}

	public static ArrayList<Product> getProducts() {
		return products;
	}

	public static void setProducts(ArrayList<Product> products) {
		EntityUtils.products = products;
	}

	public static ArrayList<Branch> getBranches() {
		return branches;
	}

	public static void setBranches(ArrayList<Branch> _branches) {
		branches = _branches;
		sortBranches();
	}

	public static ArrayList<Brand> getBrands() {
		return brands;
	}

	public static void setBrands(ArrayList<Brand> _brands) {
		brands = _brands;
	}

	public static boolean isLoaded() {
		return loaded;
	}

	public static void setLoaded(boolean _loaded) {
		loaded = _loaded;
	}

	public static ArrayList<Store> getStores() {
		return stores;
	}

	public static ArrayList<City> getCities() {
		return cities;

	}

	public static ArrayList<Province> getProvinces() {
		return provinces;
	}

	public static ArrayList<Country> getCountries() {
		return countries;
	}

	public static void setBranchBPs(Branch branch, ArrayList<BranchProduct> bps2) {

	}

	public static void setStores(ArrayList<Store> _stores) {
		stores = _stores;
	}

	public static void setCities(ArrayList<City> _cities) {
		cities = _cities;
	}

	public static void setProvinces(ArrayList<Province> _provinces) {
		provinces = _provinces;
	}

	public static void setCountries(ArrayList<Country> _countries) {
		countries = _countries;
	}

	public static ArrayList<Object> getLocations() {
		return locs;
	}

	public static void setLocations() {
		locs = new ArrayList<Object>();
		locs.addAll(cities);
		locs.addAll(countries);
		locs.addAll(provinces);
	}

	public static void persistBranchProducts(Context context,
			ArrayList<BranchProduct> _bps) {
		branchProductDAO.addAll(_bps);
	}

	public static void persistProductCategories(Context context,
			ArrayList<ProductCategory> _pcs) {
		productCategoryDAO.addAll(_pcs);
	}

	public static void persistBranches(Context context,
			ArrayList<Branch> _branches) {
		branchDAO.addAll(_branches);
	}

	public static void persistCountries(Context context,
			ArrayList<Country> _countries) {
		countryDAO.addAll(_countries);
	}

	public static void persistStores(Context context, ArrayList<Store> _stores) {
		storeDAO.addAll(_stores);
	}

	public static void persistBrands(Context context, ArrayList<Brand> _brands) {
		brandDAO.addAll(_brands);
	}

	public static void persistDatePrices(Context context,
			ArrayList<DatePrice> _datePrices) {
		datePriceDAO.addAll(_datePrices);
	}

	public static void persistProvinces(Context context,
			ArrayList<Province> _provinces) {
		provinceDAO.addAll(_provinces);
	}

	public static void persistCities(Context context, ArrayList<City> _cities) {
		cityDAO.addAll(_cities);
	}

	public static void persistCityLocations(Context context,
			ArrayList<CityLocation> _cityLocations) {
		cityLocationDAO.addAll(_cityLocations);
	}

	public static void persistProducts(Context context,
			ArrayList<Product> _products) {
		productDAO.addAll(_products);
	}

	public static void persistCategories(Context context,
			ArrayList<Category> _categories) {
		categoryDAO.addAll(_categories);
	}

	public static ERROR loadAll(Context context) {
		setCountries(countryDAO.getAll());
		setStores(storeDAO.getAll());
		setCategories(categoryDAO.getAll());
		setBrands(brandDAO.getAll());
		setProvinces(provinceDAO.getAll());
		setCities(cityDAO.getAll());
		setProducts(productDAO.getAll());
		setBranches(branchDAO.getAll());
		setLocations();
		loaded = true;
		if (DEBUG.ON) {
			Log.v(TAGS.ENTITY, products.toString());
		}
		return ERROR.SUCCESS;
	}

	public static ERROR retrieveBranches(ArrayList<Product> addedProducts) {
		HashMap<Branch, ArrayList<BranchProduct>> branchMap = new HashMap<Branch, ArrayList<BranchProduct>>();
		HashSet<Branch> notfound = new HashSet<Branch>();
		if (addedProducts.size() == 0) {
			return ERROR.NOT_FOUND;
		} else {
			boolean firstIter = true;
			for (Product p : addedProducts) {
				ArrayList<BranchProduct> bps = branchProductDAO
						.getAllByParameters(
								new String[] { DB.PRODUCT_ID },
								new String[] { String.valueOf(p.getId()) });
				if (bps != null) {
					for (BranchProduct bp : bps) {
						if (bp.getBranch() != null
								&& bp.getBranch().getCityLocation() != null) {
							if (!branchMap.containsKey(bp.getBranch())
									&& !firstIter) {
								continue;
							} else if (!branchMap.containsKey(bp.getBranch())) {
								branchMap.put(bp.getBranch(),
										new ArrayList<BranchProduct>());
							}
							bp.setQuantity(p.getQuantity());
							branchMap.get(bp.getBranch()).add(bp);
							if (notfound.contains(bp.getBranch())) {
								notfound.remove(bp.getBranch());
							}
						}
					}
				}
				if (!notfound.isEmpty()) {
					for (Branch b : notfound) {
						branchMap.remove(b);
					}
				}
				firstIter = false;
				notfound.addAll(branchMap.keySet());

			}
		}
		ShopUtils.setShopLists(branchMap);
		if (branchMap == null || branchMap.size() == 0) {
			return ERROR.NOT_FOUND;
		}
		return ERROR.SUCCESS;
	}

	public static ArrayList<DatePrice> getDatePrices(long id) {
		return datePriceDAO.getAllByParameters(
				new String[] { DB.BRANCH_PRODUCT_ID },
				new String[] { String.valueOf(id) });
	}

	public static ERROR retrieveBranchProducts(boolean productsActive) {
		SearchUtils.setSearched(null);
		HashSet<Product> _p = new HashSet<Product>();
		if (!productsActive) {
			if (SearchUtils.getAddedCategories().size() > 0) {
				for (Category c : SearchUtils.getAddedCategories()) {
					ArrayList<Product> matches = productCategoryDAO
							.getProducts(c.getId());
					if (matches != null) {
						_p.addAll(matches);
					}
				}
			} else {
				_p.addAll(products);
			}
		} else {
			_p.addAll(SearchUtils.getAddedProducts());
		}
		HashSet<BranchProduct> found = new HashSet<BranchProduct>();
		if (_p.size() == 0) {
			_p.addAll(products);
		}
		for (Product p : _p) {
			ArrayList<BranchProduct> bps = branchProductDAO.getAllByParameters(
					new String[] { DB.PRODUCT_ID },
					new String[] { String.valueOf(p.getId()) });
			if (bps != null) {
				for (BranchProduct bp : bps) {
					if (bp.getBranch() != null
							&& bp.getBranch().getCityLocation() != null) {
						City c = bp.getBranch().getCityLocation().getCity();
						if (c != null && inLocations(c)) {
							if (found.add(bp)) {
								SearchUtils.getSearched().add(bp);
							}
						}
					}
				}
			}
		}
		if (SearchUtils.getSearched() == null
				|| SearchUtils.getSearched().size() == 0) {
			return ERROR.NOT_FOUND;
		}
		return ERROR.SUCCESS;
	}

	private static boolean inLocations(City city) {
		return SearchUtils.getAddedLocations().size() == 0
				|| SearchUtils.getAddedCities().contains(city)
				|| SearchUtils.getAddedProvinces().contains(city.getProvince())
				|| SearchUtils.getAddedCountries().contains(
						city.getProvince().getCountry());
	}

	public static BranchProduct addBranchProduct(BranchProduct bp) {
		long id = branchProductDAO.add(bp);
		return branchProductDAO.getByID(id);
	}

	public static BranchProduct updateBranchProduct(BranchProduct bp) {
		if (!branchProductDAO.updateByCloudID(bp, bp.getId())) {
			return addBranchProduct(bp);
		}
		return branchProductDAO.getByCloudID(bp.getId());
	}

	public static Product addProduct(Product p) {
		long id = productDAO.add(p);
		p = productDAO.getByID(id);
		products.add(p);
		return p;
	}

	public static Brand addBrand(Brand b) {
		long id = brandDAO.add(b);
		b = brandDAO.getByID(id);
		brands.add(b);
		return b;
	}

	public static Branch addBranch(Branch branch) {
		long id = branchDAO.add(branch);
		branch = branchDAO.getByID(id);
		branches.add(0, branch);
		return branch;
	}

	public static Category addCategory(Category c) {
		long id = categoryDAO.add(c);
		c = categoryDAO.getByID(id);
		categories.add(c);
		return c;
	}

	public static CityLocation addCityLocation(CityLocation cl) {
		long id = cityLocationDAO.add(cl);
		return cityLocationDAO.getByID(id);
	}

	public static City addCity(City c) {
		long id = cityDAO.add(c);
		c = cityDAO.getByID(id);
		cities.add(c);
		return c;

	}

	public static Store addStore(Store s) {
		long id = storeDAO.add(s);
		s = storeDAO.getByID(id);
		stores.add(s);
		return s;
	}

	public static DatePrice addDatePrice(DatePrice dp) {
		long id = datePriceDAO.add(dp);
		return datePriceDAO.getByID(id);
	}

	public static Product retrieveProduct(long code) {
		return productDAO.getByCloudID(code);
	}

	public static Product retrieveProductServer(long code) {
		if (DEBUG.ON) {
			Log.v(TAGS.SCAN, String.valueOf(code));
		}
		Product product = RPCUtils.retrieveProduct(code);
		if (product != null) {
			return addProduct(product);
		}
		return product;
	}

	public static BranchProduct retrieveBranchProduct(long productId,
			long branchId) {
		return branchProductDAO.getByParameters(new String[] {
				DB.PRODUCT_ID, DB.BRANCH_ID }, new String[] {
				String.valueOf(productId), String.valueOf(branchId) });
	}

	public static BranchProduct retrieveBranchProductServer(long productId,
			long branchId) {
		BranchProduct branchProduct = RPCUtils.retrieveBranchProduct(productId,
				branchId);
		if (branchProduct != null) {
			return addBranchProduct(branchProduct);
		}
		return branchProduct;
	}

	public static ArrayList<BranchProduct> retrieveBranchProducts(
			long productId, BranchProduct main) {
		ArrayList<BranchProduct> branchProducts = branchProductDAO
				.getAllByParameters(new String[] { DB.PRODUCT_ID },
						new String[] { String.valueOf(productId) });
		return branchProducts;
	}

	public static void sortBranches() {
		if (branches != null && MapUtils.getUserLocation() != null) {
			Collections.sort(branches);
			MapUtils.setUserBranch(branches.get(0));
		}
	}

}
