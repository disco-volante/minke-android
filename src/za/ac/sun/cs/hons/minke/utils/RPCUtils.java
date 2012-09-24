package za.ac.sun.cs.hons.minke.utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class RPCUtils {

	@SuppressWarnings("unchecked")
	public static int retrieveLocations() {
		String suffix = "_locations";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		} else {
			try {
				EntityUtils.setLocations(ObjectParsers.parseResponse(response,
						suffix));
				return Constants.SUCCESS;
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	public static int retrieveBrands() {
		String suffix = "_brands";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		} else {
			try {
				EntityUtils.setBrands(ObjectParsers.parseResponse(response,
						suffix));
				return Constants.SUCCESS;
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	public static int retrieveCategories() {
		String suffix = "_categories";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		} else {
			try {
				EntityUtils.setCategories(ObjectParsers.parseResponse(response,
						suffix));
				return Constants.SUCCESS;

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	public static int retrieveProducts() {
		String suffix = "_products";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		} else {
			try {
				EntityUtils.setProducts(ObjectParsers.parseResponse(response,
						suffix));
				return Constants.SUCCESS;

			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	private static int retrieveBranchProductsC() {
		String suffix = "locationcategory_branchproducts";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url,
				SearchUtils.getAddedCities(), SearchUtils.getAddedProvinces(),
				SearchUtils.getAddedCountries(),
				SearchUtils.getAddedCategories(false));
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> searched = ObjectParsers.parseResponse(response,
					suffix);
			SearchUtils.setSearched(searched);
			EntityUtils.setBranchProducts(searched);
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	private static int retrieveBranchProductsP() {
		String suffix = "locationproduct_branchproducts";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url,
				SearchUtils.getAddedCities(), SearchUtils.getAddedProvinces(),
				SearchUtils.getAddedCountries(),
				SearchUtils.getAddedProducts(false));
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> searched = ObjectParsers.parseResponse(response,
					suffix);
			SearchUtils.setSearched(searched);
			EntityUtils.setBranchProducts(searched);
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}
		return Constants.PARSE_ERROR;

	}

	public static int retrieveBranchProducts(boolean productsActive) {
		if (productsActive) {
			return retrieveBranchProductsP();
		} else {
			return retrieveBranchProductsC();

		}
	}

	@SuppressWarnings("unchecked")
	public static int retrieveBranches(List<IsEntity> products) {
		String suffix = "product_branches";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url, products);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> branches = ObjectParsers.parseResponse(response,
					suffix);
			EntityUtils.setBranches(branches);
			MapUtils.setBranches(EntityUtils.getBranches());
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

	public static int retrieveBranches(double latitude, double longitude) {
		String suffix = "coords_branches";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doGetWithResponse(url, latitude, longitude);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			EntityUtils.setBranches(ObjectParsers.parseResponse(response,
					suffix));
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

	@SuppressWarnings("unchecked")
	public static int getBranchProduct(long code, Branch branch) {
		String suffix = "branch_branchproduct";
		String url = Constants.URL_PREFIX + suffix;
		List<IsEntity> holder = Arrays.asList(new IsEntity[] { branch });
		String response = HTTPUtils.doGetWithResponse(url, code, holder);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> scanned = ObjectParsers.parseResponse(response,
					suffix);
			if (scanned != null && scanned.size() > 0) {
				EntityUtils.setScanned(scanned.get(0));
				return Constants.SUCCESS;

			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

	public static int retrieveBranchProducts(Branch branch) {
		String suffix = "branch_branchproducts";
		String url = Constants.URL_PREFIX + suffix;
		List<IsEntity> holder = Arrays.asList(new IsEntity[] { branch });
		@SuppressWarnings("unchecked")
		String response = HTTPUtils.doGetWithResponse(url, holder);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			EntityUtils.setBranchProducts(ObjectParsers.parseResponse(response,
					suffix));
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

	public static int addBranchProduct(BranchProduct bp, long barcode,
			long branchcode) {
		String suffix = "branchproduct_branchproduct";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doPostWithResponse(url, bp, barcode,
				branchcode);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> holder = ObjectParsers.parseResponse(response,
					suffix);
			EntityUtils.setBranchProducts(holder);
			BrowseUtils.setBranchProducts(holder);
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

	public static int updateBranchProduct(BranchProduct bp) {
		String suffix = "price_branchproduct";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doPostWithResponse(url, bp.getID(),
				bp.getPrice());
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> bps = ObjectParsers.parseResponse(response,
					suffix);
			EntityUtils.setBranchProducts(bps);
			BrowseUtils.setBranchProducts(bps);
			return Constants.SUCCESS;

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		}
		return Constants.PARSE_ERROR;

	}

	public static int addBranch(Branch branch, String province, String country) {
		String suffix = "branch_branch";
		String url = Constants.URL_PREFIX + suffix;
		String response = HTTPUtils.doPostWithResponse(url, branch, province,
				country);
		if (response.equals(Constants.CLIENT)) {
			return Constants.CLIENT_ERROR;
		} else if (response.equals(Constants.SERVER)) {
			return Constants.SERVER_ERROR;
		}
		try {
			List<IsEntity> holder = ObjectParsers.parseResponse(response,
					suffix);
			if (holder != null && holder.size() > 0) {
				EntityUtils.setUserBranch((Branch) holder.get(0));
				EntityUtils.addBranch( holder.get(0));
				return Constants.SUCCESS;
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return Constants.PARSE_ERROR;

	}

}
