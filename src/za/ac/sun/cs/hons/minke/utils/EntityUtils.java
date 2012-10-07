package za.ac.sun.cs.hons.minke.utils;

import java.util.ArrayList;
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
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
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

	public static List<Category> getCategories() {
		if (categories == null) {
			categories = new ArrayList<Category>();
		}
		return categories;
	}

	public static void setCategories(ArrayList<Category> categories) {
		EntityUtils.categories = categories;
	}

	public static ArrayList<Product> getProducts() {
		if (products == null) {
			products = new ArrayList<Product>();
		}
		return products;
	}

	public static void setProducts(ArrayList<Product> products) {
		EntityUtils.products = products;
	}

	public static ArrayList<Branch> getBranches() {
		if (branches == null) {
			branches = new ArrayList<Branch>();
		}
		return branches;
	}

	public static void setBranches(ArrayList<Branch> _branches) {
		branches = _branches;

	}

	public static ArrayList<Brand> getBrands() {
		if (brands == null) {
			brands = new ArrayList<Brand>();
		}
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
		init(context);
		branchProductDAO.addAll(_bps);
	}

	public static void persistProductCategories(Context context,
			ArrayList<ProductCategory> _pcs) {
		init(context);
		productCategoryDAO.addAll(_pcs);
	}

	public static void persistBranches(Context context,
			ArrayList<Branch> _branches) {
		init(context);
		branchDAO.addAll(_branches);
	}

	public static void persistCountries(Context context,
			ArrayList<Country> _countries) {
		init(context);
		countryDAO.addAll(_countries);
	}

	public static void persistStores(Context context, ArrayList<Store> _stores) {
		init(context);
		storeDAO.addAll(_stores);
	}

	public static void persistBrands(Context context, ArrayList<Brand> _brands) {
		init(context);
		brandDAO.addAll(_brands);
	}

	public static void persistDatePrices(Context context,
			ArrayList<DatePrice> _datePrices) {
		init(context);
		datePriceDAO.addAll(_datePrices);
	}

	public static void persistProvinces(Context context,
			ArrayList<Province> _provinces) {
		init(context);
		provinceDAO.addAll(_provinces);
	}

	public static void persistCities(Context context, ArrayList<City> _cities) {
		init(context);
		cityDAO.addAll(_cities);
	}

	public static void persistCityLocations(Context context,
			ArrayList<CityLocation> _cityLocations) {
		init(context);
		cityLocationDAO.addAll(_cityLocations);
	}

	public static void persistProducts(Context context,
			ArrayList<Product> _products) {
		init(context);
		productDAO.addAll(_products);
	}

	public static void persistCategories(Context context,
			ArrayList<Category> _categories) {
		init(context);
		categoryDAO.addAll(_categories);
	}

	public static ERROR loadAll(Context context) {
		init(context);
		setCountries(countryDAO.getAll());
		setStores(storeDAO.getAll());
		setCategories(categoryDAO.getAll());
		setBrands(brandDAO.getAll());
		setProvinces(provinceDAO.getAll());
		setCities(cityDAO.getAll());
		setProducts(productDAO.getAll());
		setBranches(branchDAO.getAll());
		setLocations();
		if (MapUtils.getLocation() != null && branches != null) {
			MapUtils.setBranches(branches);
		}
		loaded = true;
		Log.v(TAGS.ENTITY, products.toString());
		return ERROR.SUCCESS;
	}

	private static void init(Context context) {
		if (dbHelper == null) {
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
		}
	}

	@SuppressWarnings("unchecked")
	public static ERROR retrieveBranches(ArrayList<Product> addedProducts) {
		HashMap<Branch, ArrayList<BranchProduct>> branchMap = new HashMap<Branch, ArrayList<BranchProduct>>();
		HashMap<Branch, ArrayList<BranchProduct>> temp = new HashMap<Branch, ArrayList<BranchProduct>>();
		for (Product p : addedProducts) {
			ArrayList<BranchProduct> bps = branchProductDAO.getAllByParameters(
					new String[] { DBConstants.PRODUCT_ID },
					new String[] { String.valueOf(p.getId()) });
			if (branchMap.size() == 0) {
				for (BranchProduct bp : bps) {
					if (bp.getBranch() != null) {
						if (!branchMap.containsKey(bp.getBranch())) {
							branchMap.put(bp.getBranch(),
									new ArrayList<BranchProduct>());
						}
						branchMap.get(bp.getBranch()).add(bp);
					}
				}
			} else {
				for (BranchProduct bp : bps) {
					if (bp.getBranch() != null
							&& branchMap.containsKey(bp.getBranch())) {
						temp.put(bp.getBranch(), branchMap.get(bp.getBranch()));
					}
				}
				branchMap = (HashMap<Branch, ArrayList<BranchProduct>>) temp
						.clone();
				temp = new HashMap<Branch, ArrayList<BranchProduct>>();
			}

		}
		ShopUtils.setShopLists(branchMap);
		return ERROR.SUCCESS;
	}

	public static ArrayList<DatePrice> getDatePrices(long id) {
		return datePriceDAO.getAllByParameters(
				new String[] { DBConstants.BRANCH_PRODUCT_ID },
				new String[] { String.valueOf(id) });
	}

	public static ERROR retrieveBranchProducts(boolean productsActive) {
		HashSet<Product> _p = new HashSet<Product>();
		if (!productsActive) {
			for (Category c : SearchUtils.getAddedCategories()) {
				ArrayList<Product> matches = productCategoryDAO.getProducts(c
						.getId());
				_p.addAll(matches);
			}
		} else {
			_p.addAll(SearchUtils.getAddedProducts());
		}
		HashSet<BranchProduct> found = new HashSet<BranchProduct>();
		for (Product p : _p) {
			ArrayList<BranchProduct> bps = branchProductDAO.getAllByParameters(
					new String[] { DBConstants.PRODUCT_ID },
					new String[] { String.valueOf(p.getId()) });
			for (BranchProduct bp : bps) {
				Log.v(TAGS.ENTITY, bp.toString());
				City c = bp.getBranch().getCityLocation().getCity();
				Log.v(TAGS.ENTITY, c.toString());
				if (SearchUtils.getAddedCities().contains(c)
						|| SearchUtils.getAddedProvinces().contains(
								c.getProvince())
						|| SearchUtils.getAddedCountries().contains(
								c.getProvince().getCountry())) {
					if (found.add(bp)) {
						SearchUtils.getSearched().add(bp);
					}
				}
			}
		}
		return ERROR.SUCCESS;
	}

	public static BranchProduct addBranchProduct(BranchProduct bp) {
		long id = branchProductDAO.add(bp);
		return branchProductDAO.getByID(id);
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
		return productDAO.getByID(code);
	}

	public static Product retrieveProductServer(long code) {
		Log.v(TAGS.SCAN, String.valueOf(code));
		Product product = RPCUtils.retrieveProduct(code);
		if (product != null) {
			return addProduct(product);
		}
		return product;
	}

	public static BranchProduct retrieveBranchProduct(long productId,
			long branchId) {
		return branchProductDAO.getByParameters(new String[] {
				DBConstants.PRODUCT_ID, DBConstants.BRANCH_ID }, new String[] {
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
				.getAllByParameters(new String[] { DBConstants.PRODUCT_ID },
						new String[] { String.valueOf(productId) });
		return branchProducts;
	}

}
