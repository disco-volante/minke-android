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

	public static void init(Context context) {
		dbHelper = new BaseDBHelper(context);
	}

	public static void initCountry(Context context) {
		if (dbHelper == null) {
			init(context);
		}
		countryDAO = new CountryDAO(dbHelper);
	}

	public static void initStore(Context context) {
		if (dbHelper == null) {
			init(context);
		}
		storeDAO = new StoreDAO(dbHelper);

	}

	public static void initCategory(Context context) {
		if (dbHelper == null) {
			init(context);
		}
		categoryDAO = new CategoryDAO(dbHelper);

	}

	public static void initBrand(Context context) {
		if (dbHelper == null) {
			init(context);
		}
		brandDAO = new BrandDAO(dbHelper);
	}

	public static void initDatePrice(Context context) {
		if (dbHelper == null) {
			init(context);
		}
		datePriceDAO = new DatePriceDAO(dbHelper);
	}

	public static void initProvince(Context context) {
		initCountry(context);
		provinceDAO = new ProvinceDAO(dbHelper, countryDAO);
	}

	public static void initCity(Context context) {
		initProvince(context);
		cityDAO = new CityDAO(dbHelper, provinceDAO);
	}

	public static void initCityLocation(Context context) {
		initCity(context);
		cityLocationDAO = new CityLocationDAO(dbHelper, cityDAO);
	}

	public static void initProduct(Context context) {
		initBrand(context);
		productDAO = new ProductDAO(dbHelper, brandDAO);
	}

	public static void initBranch(Context context) {
		initStore(context);
		initCityLocation(context);
		branchDAO = new BranchDAO(dbHelper, storeDAO, cityLocationDAO);
	}

	public static void initBranchProduct(Context context) {
		initBranch(context);
		initProduct(context);
		initDatePrice(context);
		branchProductDAO = new BranchProductDAO(dbHelper, datePriceDAO,
				productDAO, branchDAO);
	}

	public static void initProductCategory(Context context) {
		initProduct(context);
		initCategory(context);
		productCategoryDAO = new ProductCategoryDAO(dbHelper, productDAO,
				categoryDAO);
	}

	public static List<Category> getCategories(Context context) {
		if (categoryDAO == null) {
			initCategory(context);
		}
		ArrayList<Category> categories = categoryDAO.getAll();
		if(categories != null){
			return categories;
		}
		return new ArrayList<Category>();
	}

	public static ArrayList<Product> getProducts(Context context) {
		if (productDAO == null) {
			initProduct(context);
		}
		ArrayList<Product> products = productDAO.getAll();
		if(products != null){
			return products;
		}
		return new ArrayList<Product>();
	}

	public static ArrayList<Branch> getBranches(Context context) {
		if (branchDAO == null) {
			initBranch(context);
		}
		ArrayList<Branch> branches =  branchDAO.getAll();
		if(branches != null){
			return branches;
		}
		return new ArrayList<Branch>();
	}

	public static ArrayList<Brand> getBrands(Context context) {
		if (brandDAO == null) {
			initBrand(context);
		}
		ArrayList<Brand> brands =  brandDAO.getAll();
		if(brands != null){
			return brands;
		}
		return new ArrayList<Brand>();
	}

	public static ArrayList<Store> getStores(Context context) {
		if (storeDAO == null) {
			initStore(context);
		}
		ArrayList<Store> stores =  storeDAO.getAll();
		if(stores != null){
			return stores;
		}
		return new ArrayList<Store>();
	}

	public static ArrayList<Object> getLocations(Context context) {
		if (cityDAO == null) {
			initCity(context);
		}
		if (provinceDAO == null) {
			initProvince(context);
		}
		if (countryDAO == null) {
			initCountry(context);
		}
		ArrayList<Object> locs = new ArrayList<Object>();
		ArrayList<Country> countries = countryDAO.getAll();
		if (countries != null) {
			locs.addAll(countries);
		}
		ArrayList<Province> provinces = provinceDAO.getAll();
		if (provinces != null) {
			locs.addAll(provinces);
		}
		ArrayList<City> cities = cityDAO.getAll();
		if (cities != null) {
			locs.addAll(cities);
		}
		return locs;
	}

	public static void persistBranchProducts(Context context,
			ArrayList<BranchProduct> _bps) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		branchProductDAO.addAll(_bps);
	}

	public static void persistProductCategories(Context context,
			ArrayList<ProductCategory> _pcs) {
		if (productCategoryDAO == null) {
			initProductCategory(context);
		}
		productCategoryDAO.addAll(_pcs);
	}

	public static void persistBranches(Context context,
			ArrayList<Branch> _branches) {
		if (branchDAO == null) {
			initBranch(context);
		}
		branchDAO.addAll(_branches);
	}

	public static void persistCountries(Context context,
			ArrayList<Country> _countries) {
		if (countryDAO == null) {
			initCountry(context);
		}
		countryDAO.addAll(_countries);
	}

	public static void persistStores(Context context, ArrayList<Store> _stores) {
		if (storeDAO == null) {
			initStore(context);
		}
		storeDAO.addAll(_stores);
	}

	public static void persistBrands(Context context, ArrayList<Brand> _brands) {
		if (brandDAO == null) {
			initBrand(context);
		}
		brandDAO.addAll(_brands);
	}

	public static void persistDatePrices(Context context,
			ArrayList<DatePrice> _datePrices) {
		if (datePriceDAO == null) {
			initDatePrice(context);
		}
		datePriceDAO.addAll(_datePrices);
	}

	public static void persistProvinces(Context context,
			ArrayList<Province> _provinces) {
		if (provinceDAO == null) {
			initProvince(context);
		}
		provinceDAO.addAll(_provinces);
	}

	public static void persistCities(Context context, ArrayList<City> _cities) {
		if (cityDAO == null) {
			initCity(context);
		}
		cityDAO.addAll(_cities);
	}

	public static void persistCityLocations(Context context,
			ArrayList<CityLocation> _cityLocations) {
		if (cityLocationDAO == null) {
			initCityLocation(context);
		}
		cityLocationDAO.addAll(_cityLocations);
	}

	public static void persistProducts(Context context,
			ArrayList<Product> _products) {
		if (productDAO == null) {
			initProduct(context);
		}
		productDAO.addAll(_products);
	}

	public static void persistCategories(Context context,
			ArrayList<Category> _categories) {
		if (categoryDAO == null) {
			initCategory(context);
		}
		categoryDAO.addAll(_categories);
	}

	public static ERROR retrieveBranches(Context context,
			ArrayList<Product> addedProducts) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		HashMap<Branch, ArrayList<BranchProduct>> branchMap = new HashMap<Branch, ArrayList<BranchProduct>>();
		HashSet<Branch> notfound = new HashSet<Branch>();
		if (addedProducts.size() == 0) {
			return ERROR.NOT_FOUND;
		} else {
			boolean firstIter = true;
			for (Product p : addedProducts) {
				ArrayList<BranchProduct> bps = branchProductDAO
						.getAllByParameters(new String[] { DB.PRODUCT_ID },
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

	public static ArrayList<DatePrice> getDatePrices(Context context, long id) {
		if (datePriceDAO == null) {
			initDatePrice(context);
		}
		return datePriceDAO.getAllByParameters(
				new String[] { DB.BRANCH_PRODUCT_ID },
				new String[] { String.valueOf(id) });
	}

	public static ERROR retrieveBranchProducts(Context context,
			boolean productsActive) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		if (productCategoryDAO == null) {
			initProductCategory(context);
		}
		if (productDAO == null) {
			initProduct(context);
		}
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
				_p.addAll(productDAO.getAll());
			}
		} else {
			_p.addAll(SearchUtils.getAddedProducts());
		}
		HashSet<BranchProduct> found = new HashSet<BranchProduct>();
		if (_p.size() == 0) {
			_p.addAll(getProducts(context));
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
		Collections.sort(SearchUtils.getSearched());
		return ERROR.SUCCESS;
	}

	private static boolean inLocations(City city) {
		try {
			return SearchUtils.getAddedLocations().size() == 0
					|| SearchUtils.getAddedCities().contains(city)
					|| SearchUtils.getAddedProvinces().contains(
							city.getProvince())
					|| SearchUtils.getAddedCountries().contains(
							city.getProvince().getCountry());
		} catch (Exception e) {
			return false;
		}
	}

	public static BranchProduct addBranchProduct(Context context,
			BranchProduct bp) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		long id = branchProductDAO.add(bp);
		return branchProductDAO.getByID(id);
	}

	public static BranchProduct updateBranchProduct(Context context,
			BranchProduct bp) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		if (!branchProductDAO.updateByCloudID(bp, bp.getId())) {
			return addBranchProduct(context, bp);
		}
		return branchProductDAO.getByCloudID(bp.getId());
	}

	public static Product addProduct(Context context, Product p) {
		if (productDAO == null) {
			initProduct(context);
		}
		long id = productDAO.add(p);
		p = productDAO.getByID(id);
		return p;
	}

	public static Brand addBrand(Context context, Brand b) {
		if (brandDAO == null) {
			initBrand(context);
		}
		long id = brandDAO.add(b);
		b = brandDAO.getByID(id);
		return b;
	}

	public static Branch addBranch(Context context, Branch branch) {
		if (branchDAO == null) {
			initBranch(context);
		}
		long id = branchDAO.add(branch);
		branch = branchDAO.getByID(id);
		return branch;
	}

	public static Category addCategory(Context context, Category c) {
		if (categoryDAO == null) {
			initCategory(context);
		}
		long id = categoryDAO.add(c);
		c = categoryDAO.getByID(id);
		return c;
	}

	public static CityLocation addCityLocation(Context context, CityLocation cl) {
		if (cityLocationDAO == null) {
			initCityLocation(context);
		}
		long id = cityLocationDAO.add(cl);
		return cityLocationDAO.getByID(id);
	}

	public static City addCity(Context context, City c) {
		if (cityDAO == null) {
			initCity(context);
		}
		long id = cityDAO.add(c);
		c = cityDAO.getByID(id);
		return c;

	}

	public static Store addStore(Context context, Store s) {
		if (storeDAO == null) {
			initStore(context);
		}
		long id = storeDAO.add(s);
		s = storeDAO.getByID(id);
		return s;
	}

	public static DatePrice addDatePrice(Context context, DatePrice dp) {
		if (datePriceDAO == null) {
			initDatePrice(context);
		}
		long id = datePriceDAO.add(dp);
		return datePriceDAO.getByID(id);
	}

	public static Product retrieveProduct(Context context, long code) {
		if (productDAO == null) {
			initProduct(context);
		}
		return productDAO.getByCloudID(code);
	}

	public static Product retrieveProductServer(Context context, long code) {
		if (DEBUG.ON) {
			Log.v(TAGS.SCAN, String.valueOf(code));
		}
		Product product = RPCUtils.retrieveProduct(code);
		if (product != null) {
			return addProduct(context, product);
		}
		return product;
	}

	public static BranchProduct retrieveBranchProduct(Context context,
			long productId, long branchId) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		return branchProductDAO.getByParameters(new String[] { DB.PRODUCT_ID,
				DB.BRANCH_ID }, new String[] { String.valueOf(productId),
				String.valueOf(branchId) });
	}

	public static BranchProduct retrieveBranchProductServer(Context context,
			long productId, long branchId) {
		BranchProduct branchProduct = RPCUtils.retrieveBranchProduct(productId,
				branchId);
		if (branchProduct != null) {
			return addBranchProduct(context, branchProduct);
		}
		return branchProduct;
	}

	public static ArrayList<BranchProduct> retrieveBranchProducts(
			Context context, long productId, BranchProduct main) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		ArrayList<BranchProduct> branchProducts = branchProductDAO
				.getAllByParameters(new String[] { DB.PRODUCT_ID },
						new String[] { String.valueOf(productId) });
		return branchProducts;
	}

	public static ArrayList<Province> getProvinces(Context context) {
		if (provinceDAO == null) {
			initProvince(context);
		}
		return provinceDAO.getAll();
	}

	public static ArrayList<City> getCities(Context context) {
		if (cityDAO == null) {
			initCity(context);
		}
		return cityDAO.getAll();
	}

	public static Branch getBranch(Context context, long branchId) {
		if (branchDAO == null) {
			initBranch(context);
		}
		return branchDAO.getByCloudID(branchId);
	}

	public static BranchProduct getBranchProduct(Context context,
			long branchProductId) {
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		return branchProductDAO.getByCloudID(branchProductId);
	}

	public static Product getProduct(Context context, long productId) {
		if (productDAO == null) {
			initProduct(context);
		}
		return productDAO.getByCloudID(productId);
	}

	public static BranchProduct selectBranchProduct(Context context,
			ArrayList<Branch> branches, long cityID) {
		if (branches == null) {
			return null;
		}
		if (branchProductDAO == null) {
			initBranchProduct(context);
		}
		int pos = 0;
		ArrayList<BranchProduct> bps = new ArrayList<BranchProduct>();
		for (Branch b : branches) {
			if (b == null || b.getCityLocation() == null
					|| b.getCityLocation().getCity() == null
					|| b.getCityLocation().getCityId() != cityID) {
				continue;
			}
			ArrayList<BranchProduct> temp = branchProductDAO
					.getAllByParameters(new String[] { DB.BRANCH_ID },
							new String[] { String.valueOf(b.getId()) });
			if (temp != null) {
				bps.addAll(temp);
			}
		}
		if (bps.size() == 0) {
			return null;
		}
		bps = filterLatest(bps);
		pos = (int) (Math.random() * (bps.size() - 1));
		return bps.get(pos);
	}

	private static ArrayList<BranchProduct> filterLatest(
			ArrayList<BranchProduct> bps) {
		Collections.sort(bps);
		ArrayList<BranchProduct> latest = new ArrayList<BranchProduct>();
		int len = Math.min(10, bps.size());
		latest.addAll(bps.subList(0, len));
		return latest;

	}

	public static City getCity(Context context, long cityID) {
		if (cityDAO == null) {
			initCity(context);
		}
		return cityDAO.getByCloudID(cityID);
	}

	public static ArrayList<Branch> getBranches(Context context, City city) {
		if (branchDAO == null) {
			initBranch(context);
		}
		if (cityLocationDAO == null) {
			initCityLocation(context);
		}
		ArrayList<CityLocation> locs = cityLocationDAO.getAllByParameters(
				new String[] { DB.CITY_ID },
				new String[] { String.valueOf(city.getId()) });
		ArrayList<Branch> branches = new ArrayList<Branch>();
		for (CityLocation cl : locs) {
			ArrayList<Branch> temp = branchDAO.getAllByParameters(
					new String[] { DB.CITY_LOCATION_ID },
					new String[] { String.valueOf(cl.getId()) });
			if (temp != null) {
				branches.addAll(temp);
			}
		}
		return branches;
	}

}
