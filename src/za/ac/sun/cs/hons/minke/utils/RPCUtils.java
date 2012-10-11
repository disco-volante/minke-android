package za.ac.sun.cs.hons.minke.utils;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import za.ac.sun.cs.hons.minke.utils.json.JSONBuilder;
import za.ac.sun.cs.hons.minke.utils.json.JSONParser;
import android.content.Context;
import android.util.Log;

public class RPCUtils {

	public static ERROR retrieveAll(Context context) {
		String suffix = "get_all";
		String url = Constants.URL_PREFIX + suffix;

		try {
			JSONObject obj;
			obj = HTTPUtils.doGetWithResponse(url);

			if (obj == null || obj.isNull("branches")) {
				return ERROR.SERVER;
			}
			Log.v(TAGS.JSON, obj.toString());
			EntityUtils.persistBranches(context,
					JSONParser.parseBranches(obj.getJSONObject("branches")));
			EntityUtils.persistProducts(context,
					JSONParser.parseProducts(obj.getJSONObject("products")));
			EntityUtils.persistStores(context,
					JSONParser.parseStores(obj.getJSONObject("stores")));
			EntityUtils.persistBranchProducts(context, JSONParser
					.parseBranchProducts(obj.getJSONObject("branchProducts")));
			EntityUtils
					.persistCategories(context, JSONParser.parseCategories(obj
							.getJSONObject("categories")));
			EntityUtils.persistCities(context,
					JSONParser.parseCities(obj.getJSONObject("cities")));
			EntityUtils.persistProvinces(context,
					JSONParser.parseProvinces(obj.getJSONObject("provinces")));
			EntityUtils.persistCountries(context,
					JSONParser.parseCountries(obj.getJSONObject("countries")));
			EntityUtils
					.persistDatePrices(context, JSONParser.parseDatePrices(obj
							.getJSONObject("datePrices")));
			EntityUtils.persistCityLocations(context, JSONParser
					.parseCityLocations(obj.getJSONObject("cityLocations")));
			EntityUtils.persistProductCategories(context, JSONParser
					.parseProductCategories(obj
							.getJSONObject("productCategories")));
			EntityUtils.persistBrands(context,
					JSONParser.parseBrands(obj.getJSONObject("brands")));
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static ERROR updateBranchProduct(BranchProduct bp, int price) {
		String suffix = "update_branchproduct";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject obj = HTTPUtils.doJSONPost(url,
					bp.toJSON(), JSONBuilder.toJSON("price", price));
			Log.v(TAGS.JSON, obj.toString());
			bp = JSONParser.parseBranchProduct(obj.getJSONObject("branchProduct"));
			DatePrice dp = JSONParser.parseDatePrice(obj.getJSONObject("datePrice"));
			EntityUtils.addDatePrice(dp);
			bp = EntityUtils.updateBranchProduct(bp);
			ScanUtils.setBranchProduct(bp);
			if (bp == null) {
				return ERROR.SERVER;
			}
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}

	}

	public static ERROR createBranch(City city, Store store, double lat,
			double lon, String branchName) {
		String suffix = "create_branch0";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject branchJSON = JSONBuilder.BranchProtoToJSON(branchName,
					null, null, lon, lat);
			JSONObject response = HTTPUtils.doJSONPost(url, branchJSON,
					city.toJSON(), store.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			Branch b = JSONParser.parseBranch(response.getJSONObject("branch"));
			CityLocation cl = JSONParser.parseCityLocation(response
					.getJSONObject("cityLocation"));
			if (b == null || cl == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addCityLocation(cl);
			b = EntityUtils.addBranch(b);
			MapUtils.setUserBranch(b);
			MapUtils.setBranches(EntityUtils.getBranches());
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static ERROR createBranch(City city, double lat, double lon,
			String storeName, String branchName) {
		String suffix = "create_branch1";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject branchJSON = JSONBuilder.BranchProtoToJSON(branchName,
					null, storeName, lon, lat);
			JSONObject response = HTTPUtils.doJSONPost(url, branchJSON,
					city.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			Branch b = JSONParser.parseBranch(response.getJSONObject("branch"));
			Store s = JSONParser.parseStore(response.getJSONObject("store"));
			CityLocation cl = JSONParser.parseCityLocation(response
					.getJSONObject("cityLocation"));
			if (b == null || s == null || cl == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addStore(s);
			EntityUtils.addCityLocation(cl);
			b = EntityUtils.addBranch(b);
			MapUtils.setUserBranch(b);
			MapUtils.setBranches(EntityUtils.getBranches());
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static ERROR createBranch(Province province, Store store,
			double lat, double lon, String cityName, String branchName) {
		String suffix = "create_branch2";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject branchJSON = JSONBuilder.BranchProtoToJSON(branchName,
					cityName, null, lon, lat);
			JSONObject response = HTTPUtils.doJSONPost(url, branchJSON,
					store.toJSON(), province.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			Branch b = JSONParser.parseBranch(response.getJSONObject("branch"));
			City c = JSONParser.parseCity(response.getJSONObject("city"));
			CityLocation cl = JSONParser.parseCityLocation(response
					.getJSONObject("cityLocation"));
			if (b == null || c == null || cl == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addCity(c);
			EntityUtils.addCityLocation(cl);
			b = EntityUtils.addBranch(b);
			MapUtils.setUserBranch(b);
			MapUtils.setBranches(EntityUtils.getBranches());
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
		return ERROR.PARSE;
	}

	public static ERROR createBranch(Province province, double lat, double lon,
			String cityName, String storeName, String branchName) {
		String suffix = "create_branch3";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject branchJSON = JSONBuilder.BranchProtoToJSON(branchName,
					cityName, storeName, lon, lat);
			JSONObject response = HTTPUtils.doJSONPost(url, branchJSON,
					province.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			Store s = JSONParser.parseStore(response.getJSONObject("store"));
			Branch b = JSONParser.parseBranch(response.getJSONObject("branch"));
			City c = JSONParser.parseCity(response.getJSONObject("city"));
			CityLocation cl = JSONParser.parseCityLocation(response
					.getJSONObject("cityLocation"));
			if (b == null || c == null || cl == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addStore(s);
			EntityUtils.addCity(c);
			EntityUtils.addCityLocation(cl);
			b = EntityUtils.addBranch(b);
			MapUtils.setUserBranch(b);
			MapUtils.setBranches(EntityUtils.getBranches());
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static ERROR createBranchProduct(Branch branch, String name,
			Brand brand, Category category, double size, String measure,
			int price, long barCode) {
		String suffix = "create_branchproduct0";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject bpJSON = JSONBuilder.BranchProductProtoToJSON(name,
					null, null, size, measure, price, barCode);
			JSONObject response = HTTPUtils.doJSONPost(url, branch.toJSON(),
					bpJSON, brand.toJSON(), category.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			BranchProduct bp = JSONParser.parseBranchProduct(response
					.getJSONObject("branchProduct"));
			Product p = JSONParser.parseProduct(response
					.getJSONObject("product"));
			DatePrice dp = JSONParser.parseDatePrice(response
					.getJSONObject("datePrice"));
			if (bp == null || p == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addDatePrice(dp);
			EntityUtils.addProduct(p);
			bp = EntityUtils.addBranchProduct(bp);
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
		return ERROR.PARSE;
	}

	public static ERROR createBranchProduct(Branch branch, String name,
			Brand brand, String category, double size, String measure,
			int price, long barCode) {
		String suffix = "create_branchproduct1";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject bpJSON = JSONBuilder.BranchProductProtoToJSON(name,
					category, null, size, measure, price, barCode);
			JSONObject response = HTTPUtils.doJSONPost(url, branch.toJSON(),
					bpJSON, brand.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			BranchProduct bp = JSONParser.parseBranchProduct(response
					.getJSONObject("branchProduct"));
			Product p = JSONParser.parseProduct(response
					.getJSONObject("product"));
			Category c = JSONParser.parseCategory(response
					.getJSONObject("category"));
			DatePrice dp = JSONParser.parseDatePrice(response
					.getJSONObject("datePrice"));
			if (bp == null || p == null || c == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addDatePrice(dp);
			EntityUtils.addCategory(c);
			EntityUtils.addProduct(p);
			bp = EntityUtils.addBranchProduct(bp);
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
		return ERROR.PARSE;
	}

	public static ERROR createBranchProduct(Branch branch, String name,
			String brand, Category category, double size, String measure,
			int price, long barCode) {
		String suffix = "create_branchproduct2";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject bpJSON = JSONBuilder.BranchProductProtoToJSON(name,
					null, brand, size, measure, price, barCode);
			JSONObject response = HTTPUtils.doJSONPost(url, branch.toJSON(),
					bpJSON, category.toJSON());
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			BranchProduct bp = JSONParser.parseBranchProduct(response
					.getJSONObject("branchProduct"));
			Product p = JSONParser.parseProduct(response
					.getJSONObject("product"));
			Brand b = JSONParser.parseBrand(response.getJSONObject("brand"));
			DatePrice dp = JSONParser.parseDatePrice(response
					.getJSONObject("datePrice"));

			if (bp == null || p == null || b == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addDatePrice(dp);
			EntityUtils.addBrand(b);
			EntityUtils.addProduct(p);
			bp = EntityUtils.addBranchProduct(bp);
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static ERROR createBranchProduct(Branch branch, String name,
			String brand, String category, double size, String measure,
			int price, long barCode) {
		String suffix = "create_branchproduct3";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject bpJSON = JSONBuilder.BranchProductProtoToJSON(name,
					category, brand, size, measure, price, barCode);
			JSONObject response;
			response = HTTPUtils.doJSONPost(url, branch.toJSON(), bpJSON);
			if (response.length() == 0) {
				return ERROR.SERVER;
			}
			BranchProduct bp = JSONParser.parseBranchProduct(response
					.getJSONObject("branchProduct"));
			Product p = JSONParser.parseProduct(response
					.getJSONObject("product"));
			Brand b = JSONParser.parseBrand(response.getJSONObject("brand"));
			Category c = JSONParser.parseCategory(response
					.getJSONObject("category"));
			DatePrice dp = JSONParser.parseDatePrice(response
					.getJSONObject("datePrice"));

			if (bp == null || p == null || c == null || b == null) {
				return ERROR.SERVER;
			}
			EntityUtils.addDatePrice(dp);
			EntityUtils.addBrand(b);
			EntityUtils.addCategory(c);
			EntityUtils.addProduct(p);
			bp = EntityUtils.addBranchProduct(bp);
			ScanUtils.setBranchProduct(bp);
			return ERROR.SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
			return ERROR.PARSE;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return ERROR.CLIENT;
		} catch (IOException e) {
			e.printStackTrace();
			return ERROR.SERVER;
		}
	}

	public static Product retrieveProduct(long code) {
		String suffix = "get_product";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject pId = JSONBuilder.toJSON("productId", code);
			JSONObject obj = HTTPUtils.doJSONPost(url, pId);
			if (obj.length() == 0) {
				return null;
			}
			return JSONParser.parseProduct(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BranchProduct retrieveBranchProduct(long productId,
			long branchId) {
		String suffix = "get_branchproduct";
		String url = Constants.URL_PREFIX + suffix;
		try {
			JSONObject pId = JSONBuilder.toJSON("productId", productId);
			JSONObject bId = JSONBuilder.toJSON("branchId", branchId);
			JSONObject obj = HTTPUtils.doJSONPost(url, pId, bId);
			if (obj.length() == 0) {
				return null;
			}
			return JSONParser.parseBranchProduct(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
