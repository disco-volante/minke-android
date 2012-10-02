package za.ac.sun.cs.hons.minke.utils.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import za.ac.sun.cs.hons.minke.utils.ScanUtils;

public class JSONParser {

	public static ArrayList<Product> parseProducts(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("products");
		ArrayList<Product> products = new ArrayList<Product>();
		for (int i = 0; i < array.length(); i++) {
			products.add(parseProduct(array.getJSONObject(i)));
		}
		return products;
	}

	public static Product parseProduct(JSONObject obj) throws JSONException {
		Product product = new Product();
		product.setBrandId(obj.getLong("brandId"));
		product.setId(obj.getLong("id"));
		product.setName(obj.getString("name"));
		product.setMeasure(obj.getString("measure"));
		product.setSize(obj.getDouble("size"));
		return product;
	}
	
	public static ArrayList<ProductCategory> parseProductCategories(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("productCategories");
		ArrayList<ProductCategory> productCategories = new ArrayList<ProductCategory>();
		for (int i = 0; i < array.length(); i++) {
			productCategories.add(parseProductCategory(array.getJSONObject(i)));
		}
		return productCategories;
	}

	public static ProductCategory parseProductCategory(JSONObject obj) throws JSONException {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductID(obj.getLong("productId"));
		productCategory.setId(obj.getLong("id"));
		productCategory.setCategoryID(obj.getLong("categoryId"));
		return productCategory;
	}

	public static ArrayList<Category> parseCategories(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("categories");
		ArrayList<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < array.length(); i++) {
			categories.add(parseCategory(array.getJSONObject(i)));
		}
		return categories;
	}

	public static Category parseCategory(JSONObject obj) throws JSONException {
		Category category = new Category();
		category.setId(obj.getLong("id"));
		category.setName(obj.getString("name"));
		return category;
	}

	public static ArrayList<City> parseCities(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("cities");
		ArrayList<City> cities = new ArrayList<City>();
		for (int i = 0; i < array.length(); i++) {
			cities.add(parseCity(array.getJSONObject(i)));
		}
		return cities;
	}

	public static City parseCity(JSONObject obj) throws JSONException {
		City city = new City();
		city.setProvinceId(obj.getLong("provinceId"));
		city.setId(obj.getLong("id"));
		city.setName(obj.getString("name"));
		city.setLat(obj.getDouble("lat"));
		city.setLon(obj.getDouble("lon"));
		return city;
	}

	public static ArrayList<Country> parseCountries(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("countries");
		ArrayList<Country> countries = new ArrayList<Country>();
		for (int i = 0; i < array.length(); i++) {
			countries.add(parseCountry(array.getJSONObject(i)));
		}
		return countries;
	}

	public static Country parseCountry(JSONObject obj) throws JSONException {
		Country country = new Country();
		country.setId(obj.getLong("id"));
		country.setName(obj.getString("name"));
		return country;
	}

	public static ArrayList<Province> parseProvinces(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("provinces");
		ArrayList<Province> provinces = new ArrayList<Province>();
		for (int i = 0; i < array.length(); i++) {
			provinces.add(parseProvince(array.getJSONObject(i)));
		}
		return provinces;
	}

	public static Province parseProvince(JSONObject obj) throws JSONException {
		Province province = new Province();
		province.setCountryId(obj.getLong("countryId"));
		province.setId(obj.getLong("id"));
		province.setName(obj.getString("name"));
		return province;
	}

	public static ArrayList<BranchProduct> parseBranchProducts(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("branchProducts");
		ArrayList<BranchProduct> branchProducts = new ArrayList<BranchProduct>();
		for (int i = 0; i < array.length(); i++) {
			branchProducts.add(parseBranchProduct(array.getJSONObject(i)));
		}
		return branchProducts;
	}

	public static BranchProduct parseBranchProduct(JSONObject obj)
			throws JSONException {
		BranchProduct branchProduct = new BranchProduct();
		branchProduct.setBranchId(obj.getLong("branchId"));
		branchProduct.setId(obj.getLong("id"));
		branchProduct.setDatePriceId(obj.getLong("datePriceId"));
		branchProduct.setProductId(obj.getLong("productId"));
		return branchProduct;
	}

	public static ArrayList<DatePrice> parseDatePrices(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("datePrices");
		ArrayList<DatePrice> datePrices = new ArrayList<DatePrice>();
		for (int i = 0; i < array.length(); i++) {
			datePrices.add(parseDatePrice(array.getJSONObject(i)));
		}
		return datePrices;
	}

	public static DatePrice parseDatePrice(JSONObject obj) throws JSONException {
		DatePrice datePrice = new DatePrice();
		datePrice.setBranchProductID(obj.getLong("branchProductID"));
		datePrice.setId(obj.getLong("id"));
		datePrice.setPrice(obj.getInt("price"));
		datePrice.setDate(obj.getLong("date"));
		return datePrice;
	}

	public static ArrayList<Store> parseStores(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("stores");
		ArrayList<Store> stores = new ArrayList<Store>();
		for (int i = 0; i < array.length(); i++) {
			stores.add(parseStore(array.getJSONObject(i)));
		}
		return stores;
	}

	public static Store parseStore(JSONObject obj) throws JSONException {
		Store store = new Store();
		store.setId(obj.getLong("id"));
		store.setName(obj.getString("name"));
		return store;
	}

	public static ArrayList<Branch> parseBranches(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("branches");
		ArrayList<Branch> branches = new ArrayList<Branch>();
		for (int i = 0; i < array.length(); i++) {
			branches.add(parseBranch(array.getJSONObject(i)));
		}
		return branches;
	}

	public static Branch parseBranch(JSONObject obj) throws JSONException {
		Branch branch = new Branch();
		branch.setCityLocationId(obj.getLong("cityLocationId"));
		branch.setId(obj.getLong("id"));
		branch.setName(obj.getString("name"));
		branch.setStoreId(obj.getLong("storeId"));
		return branch;
	}

	public static ArrayList<CityLocation> parseCityLocations(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("cityLocations");
		ArrayList<CityLocation> cityLocations = new ArrayList<CityLocation>();
		for (int i = 0; i < array.length(); i++) {
			cityLocations.add(parseCityLocation(array.getJSONObject(i)));
		}
		return cityLocations;
	}

	public static CityLocation parseCityLocation(JSONObject obj)
			throws JSONException {
		CityLocation cityLocation = new CityLocation();
		cityLocation.setCityId(obj.getLong("cityId"));
		cityLocation.setId(obj.getLong("id"));
		cityLocation.setName(obj.getString("name"));
		cityLocation.setLat(obj.getDouble("lat"));
		cityLocation.setLon(obj.getDouble("lon"));
		return cityLocation;
	}
	
	public static ArrayList<Brand> parseBrands(JSONObject obj)
			throws JSONException {
		JSONArray array = obj.getJSONArray("brands");
		ArrayList<Brand> brands = new ArrayList<Brand>();
		for (int i = 0; i < array.length(); i++) {
			brands.add(parseBrand(array.getJSONObject(i)));
		}
		return brands;
	}

	public static Brand parseBrand(JSONObject obj)
			throws JSONException {
		Brand brand = new Brand();
		brand.setId(obj.getLong("id"));
		brand.setName(obj.getString("name"));
		return brand;
	}


	public static boolean parseSuccess(JSONObject obj) throws JSONException {
		return obj.getBoolean("success");
	}


	public static boolean parseScanResult(JSONObject obj) throws JSONException {
		if (!obj.isNull("branchProduct") && !obj.isNull("product")) {
			ScanUtils.setBranchProduct(parseBranchProduct(obj
					.getJSONObject("branchProduct")));
			ScanUtils.setProduct(parseProduct(obj.getJSONObject("product")));
			return true;
		}
		return false;
	}

}
