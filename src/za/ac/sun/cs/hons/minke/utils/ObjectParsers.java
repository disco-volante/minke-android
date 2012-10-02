package za.ac.sun.cs.hons.minke.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.entities.store.Store;

public class ObjectParsers {
	static class EntityHandler extends DefaultHandler {
		boolean bname, bid;
		String name;
		long id;

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			changeElement(qName);
		}

		protected void changeElement(String qName) {
			if (qName.equalsIgnoreCase("NAME")) {
				bname = !bname;
			} else if (qName.equalsIgnoreCase("ID")) {
				bid = !bid;
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			changeElement(qName);
		}

		public void characters(char ch[], int start, int length)
				throws SAXException {
			parseValue(new String(ch, start, length));

		}

		protected boolean parseValue(String cur) {
			if (bname) {
				name = cur;
			} else if (bid) {
				id = Long.parseLong(cur);
			} else {
				return false;
			}
			return true;
		}

	};

	public static ArrayList<Product> parseProductResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Product> entities = new ArrayList<Product>();
		EntityHandler handler = new EntityHandler() {
			boolean bproduct, bbrandId, bsize, bmeasure;
			String measure;
			double size;
			long brandId;

			@Override
			protected void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("PRODUCT")) {
					bproduct = !bproduct;
					if (!bproduct) {
						Product p = new Product(id, name, brandId, size,
								measure);
						entities.add(p);
					}
				} else if (qName.equalsIgnoreCase("BRANDID")) {
					bbrandId = !bbrandId;
				} else if (qName.equalsIgnoreCase("SIZE")) {
					bsize = !bsize;
				} else if (qName.equalsIgnoreCase("MEASURE")) {
					bmeasure = !bmeasure;
				}

			}

			@Override
			protected boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bbrandId) {
					brandId = Long.parseLong(cur);
				} else if (bsize) {
					size = Double.parseDouble(cur);
				} else if (bmeasure) {
					measure = cur;
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Category> parseCategoryResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Category> entities = new ArrayList<Category>();
		EntityHandler handler = new EntityHandler() {
			boolean bcategory;

			@Override
			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("CATEGORY")) {
					bcategory = !bcategory;
					if (!bcategory) {
						Category c = new Category(id, name);
						entities.add(c);
					}
				}
			}
		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Country> parseCountryResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Country> entities = new ArrayList<Country>();
		EntityHandler handler = new EntityHandler() {
			boolean bcountry;

			@Override
			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("COUNTRY")) {
					bcountry = !bcountry;
					if (!bcountry) {
						Country c = new Country(id, name);
						entities.add(c);
					}
				}
			}
		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Province> parseProvinceResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Province> entities = new ArrayList<Province>();
		EntityHandler handler = new EntityHandler() {
			boolean bprovince, bcountryId;
			long countryId;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("PROVINCE")) {
					bprovince = !bprovince;
					if (!bprovince) {
						Province p = new Province(id, countryId, name);
						entities.add(p);
					}
				} else if (qName.equalsIgnoreCase("COUNTRYID")) {
					bcountryId = !bcountryId;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bcountryId) {
					countryId = Long.parseLong(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<City> parseCityResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<City> entities = new ArrayList<City>();
		EntityHandler handler = new EntityHandler() {
			boolean bcity, blatitude, blongitude, bprovinceId;
			long provinceId;
			double latitude, longitude;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("CITY")) {
					bcity = !bcity;
					if (!bcity) {
						
					}
				} else if (qName.equalsIgnoreCase("LONGITUDE")) {
					blongitude = !blongitude;
				} else if (qName.equalsIgnoreCase("LATITUDE")) {
					blatitude = !blatitude;
				} else if (qName.equalsIgnoreCase("PROVINCEID")) {
					bprovinceId = !bprovinceId;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bprovinceId) {
					provinceId = Long.parseLong(cur);
				} else if (blongitude) {
					longitude = Double.parseDouble(cur);
				} else if (blatitude) {
					latitude = Double.parseDouble(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Branch> parseBranchResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Branch> entities = new ArrayList<Branch>();
		EntityHandler handler = new EntityHandler() {
			boolean bbranch, bstoreId, bclId;
			long storeId, clId;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRANCH")) {
					bbranch = !bbranch;
					if (!bbranch) {
						Branch b = new Branch(id, storeId, clId, name);
						entities.add(b);
					}
				} else if (qName.equalsIgnoreCase("STOREID")) {
					bstoreId = !bstoreId;
				} else if (qName.equalsIgnoreCase("CITYLOCATIONID")) {
					bclId = !bclId;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bstoreId) {
					storeId = Long.parseLong(cur);
				} else if (bclId) {
					clId = Long.parseLong(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<BranchProduct> parseBranchProductResponse(
			String response) throws SAXException, IOException,
			ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<BranchProduct> entities = new ArrayList<BranchProduct>();
		EntityHandler handler = new EntityHandler() {
			boolean bbranchProduct, bproductId, bbranchId, bdpId;
			long productId, branchId, dpId;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRANCHPRODUCT")) {
					bbranchProduct = !bbranchProduct;
					if (!bbranchProduct) {
						BranchProduct bp = new BranchProduct(id, productId,
								branchId, dpId);
						entities.add(bp);

					}
				} else if (qName.equalsIgnoreCase("BRANCHID")) {
					bbranchId = !bbranchId;
				} else if (qName.equalsIgnoreCase("PRODUCTID")) {
					bproductId = !bproductId;
				} else if (qName.equalsIgnoreCase("DATEPRICEID")) {
					bdpId = !bdpId;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bbranchId) {
					branchId = Long.parseLong(cur);
				} else if (bproductId) {
					productId = Long.parseLong(cur);
				} else if (bdpId) {
					dpId = Long.parseLong(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<DatePrice> parseDatePriceResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<DatePrice> entities = new ArrayList<DatePrice>();
		EntityHandler handler = new EntityHandler() {
			boolean bprice, bdate, bdatePrice;
			Date date;
			int price;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("DATEPRICE")) {
					bdatePrice = !bdatePrice;
					if (!bdatePrice) {
						DatePrice dp = new DatePrice(id, date, price, 0);
						entities.add(dp);
					}
				} else if (qName.equalsIgnoreCase("DATE")) {
					bdate = !bdate;
				} else if (qName.equalsIgnoreCase("PRICE")) {
					bprice = !bprice;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bdate) {
					date = new Date(Long.parseLong(cur));
				} else if (bprice) {
					price = Integer.parseInt(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Brand> parseBrandResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Brand> entities = new ArrayList<Brand>();
		EntityHandler handler = new EntityHandler() {
			boolean bbrand;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRAND")) {
					bbrand = !bbrand;
					if (!bbrand) {
						Brand b = new Brand(id, name);
						entities.add(b);
					}
				}

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static ArrayList<Store> parseStoreResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final ArrayList<Store> entities = new ArrayList<Store>();
		EntityHandler handler = new EntityHandler() {
			boolean bstore;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("STORE")) {
					bstore = !bstore;
					if (!bstore) {
						Store store = new Store(id, name);
						entities.add(store);
					}
				}

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}
}
