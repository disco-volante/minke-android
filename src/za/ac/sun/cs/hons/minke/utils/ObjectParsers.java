package za.ac.sun.cs.hons.minke.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;

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

	private static List<IsEntity> parseProductResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bproduct, bbrandName, bsize, bmeasure;
			String brandName, measure;
			double size;

			@Override
			protected void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("PRODUCT")) {
					bproduct = !bproduct;
					if (!bproduct) {
						Product p = new Product(name, brandName, size, measure);
						p.setID(id);
						entities.add(p);
					}
				} else if (qName.equalsIgnoreCase("BRANDNAME")) {
					bbrandName = !bbrandName;
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
				} else if (bbrandName) {
					brandName = cur;
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

	private static List<IsEntity> parseCategoryResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bcategory;

			@Override
			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("CATEGORY")) {
					bcategory = !bcategory;
					if (!bcategory) {
						Category c = new Category(name);
						c.setID(id);
						entities.add(c);
					}
				}
			}
		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	private static List<IsEntity> parseLocationResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bcity, bprovince, bcountry, blatitude, blongitude,
					bprovinceName, bcountryName;
			String provinceName, countryName;
			double latitude, longitude;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("CITY")) {
					bcity = !bcity;
					if (!bcity) {
						City c = new City(name, provinceName, countryName,
								new GPSArea(latitude, longitude));
						c.setID(id);
						entities.add(c);
					}
				} else if (qName.equalsIgnoreCase("PROVINCE")) {
					bprovince = !bprovince;
					if (!bprovince) {
						Province p = new Province(name, countryName,
								null);
						p.setID(id);
						entities.add(p);
					}
				} else if (qName.equalsIgnoreCase("COUNTRY")) {
					bcountry = !bcountry;
					if (!bcountry) {
						Country c = new Country(name, null);
						c.setID(id);
						entities.add(c);
					}
				} else if (qName.equalsIgnoreCase("LONGITUDE")) {
					blongitude = !blongitude;
				} else if (qName.equalsIgnoreCase("LATITUDE")) {
					blatitude = !blatitude;
				} else if (qName.equalsIgnoreCase("PROVINCENAME")) {
					bprovinceName = !bprovinceName;
				} else if (qName.equalsIgnoreCase("COUNTRYNAME")) {
					bcountryName = !bcountryName;
				}

			}

			@Override
			public boolean parseValue(String cur) {
				if (super.parseValue(cur)) {
					return true;
				} else if (bprovinceName) {
					provinceName = cur;
				} else if (bcountryName) {
					countryName = cur;
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

	private static List<IsEntity> parseBranchResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bbranchProduct, bbranch, blatitude, blongitude,
					bproductName, bbrandName, bbranchName, bsize, bmeasure,
					bprice, bdate, bstoreName, bcityName, bdatePrice;
			String productName, branchName, brandName, measure, storeName,
					cityName;
			Date date;
			double latitude, longitude, size, price;
			ArrayList<IsEntity> bps, dps;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRANCH")) {
					bbranch = !bbranch;
					if (!bbranch) {
						Branch b = new Branch(name, storeName, cityName,
								new GPSArea(latitude, longitude), bps);
						b.setID(id);
						entities.add(b);
					} else {
						bps = new ArrayList<IsEntity>();
					}
				} else if (qName.equalsIgnoreCase("BRANCHPRODUCT")) {
					bbranchProduct = !bbranchProduct;
					if (!bbranchProduct) {
						BranchProduct bp = new BranchProduct(productName,
								brandName, branchName, date, price, size,
								measure, dps);
						bp.setID(id);
						bps.add(bp);
					} else {
						dps = new ArrayList<IsEntity>();
					}
				} else if (qName.equalsIgnoreCase("DATEPRICE")) {
					bdatePrice = !bdatePrice;
					if (!bdatePrice) {
						DatePrice dp = new DatePrice(date, price, 0);
						dp.setID(id);
						dps.add(dp);
					}
				} else if (qName.equalsIgnoreCase("LONGITUDE")) {
					blongitude = !blongitude;
				} else if (qName.equalsIgnoreCase("LATITUDE")) {
					blatitude = !blatitude;
				} else if (qName.equalsIgnoreCase("CITYNAME")) {
					bcityName = !bcityName;
				} else if (qName.equalsIgnoreCase("STORENAME")) {
					bstoreName = !bstoreName;
				} else if (qName.equalsIgnoreCase("BRANDNAME")) {
					bbrandName = !bbrandName;
				} else if (qName.equalsIgnoreCase("BRANCHNAME")) {
					bbranchName = !bbranchName;
				} else if (qName.equalsIgnoreCase("PRODUCTNAME")) {
					bproductName = !bproductName;
				} else if (qName.equalsIgnoreCase("SIZE")) {
					bsize = !bsize;
				} else if (qName.equalsIgnoreCase("MEASURE")) {
					bmeasure = !bmeasure;
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
				} else if (bcityName) {
					cityName = cur;
				} else if (bstoreName) {
					storeName = cur;
				} else if (blongitude) {
					longitude = Double.parseDouble(cur);
				} else if (blatitude) {
					latitude = Double.parseDouble(cur);
				} else if (bbrandName) {
					brandName = cur;
				} else if (bbranchName) {
					branchName = cur;
				} else if (bproductName) {
					productName = cur;
				} else if (bsize) {
					size = Double.parseDouble(cur);
				} else if (bmeasure) {
					measure = cur;
				} else if (bdate) {
					date = new Date(Long.parseLong(cur));
				} else if (bprice) {
					price = Double.parseDouble(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	private static List<IsEntity> parseBranchProductResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bbranchProduct, bproductName, bbrandName, bbranchName,
					bsize, bmeasure, bprice, bdate, bdatePrice;
			String branchName, productName, brandName, measure;
			Date date;
			double size, price;
			ArrayList<IsEntity> dps;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRANCHPRODUCT")) {
					bbranchProduct = !bbranchProduct;
					if (!bbranchProduct) {
						BranchProduct bp = new BranchProduct(productName,
								brandName, branchName, date, price, size,
								measure, dps);
						bp.setID(id);
						entities.add(bp);

					} else {
						dps = new ArrayList<IsEntity>();
					}
				} else if (qName.equalsIgnoreCase("DATEPRICE")) {
					bdatePrice = !bdatePrice;
					if (!bdatePrice) {
						DatePrice dp = new DatePrice(date, price, 0);
						dp.setID(id);
						dps.add(dp);
					}
				} else if (qName.equalsIgnoreCase("BRANDNAME")) {
					bbrandName = !bbrandName;
				} else if (qName.equalsIgnoreCase("BRANCHNAME")) {
					bbranchName = !bbranchName;
				} else if (qName.equalsIgnoreCase("PRODUCTNAME")) {
					bproductName = !bproductName;
				} else if (qName.equalsIgnoreCase("SIZE")) {
					bsize = !bsize;
				} else if (qName.equalsIgnoreCase("MEASURE")) {
					bmeasure = !bmeasure;
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
				} else if (bbrandName) {
					brandName = cur;
				} else if (bbranchName) {
					branchName = cur;
				} else if (bproductName) {
					productName = cur;
				} else if (bsize) {
					size = Double.parseDouble(cur);
				} else if (bmeasure) {
					measure = cur;
				} else if (bdate) {
					date = new Date(Long.parseLong(cur));
				} else if (bprice) {
					price = Double.parseDouble(cur);
				} else {
					return false;
				}
				return true;

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	private static List<IsEntity> parseBrandResponse(String response)
			throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();
		final List<IsEntity> entities = new ArrayList<IsEntity>();
		EntityHandler handler = new EntityHandler() {
			boolean bbrand;

			public void changeElement(String qName) {
				super.changeElement(qName);
				if (qName.equalsIgnoreCase("BRAND")) {
					bbrand = !bbrand;
					if (!bbrand) {
						Brand b = new Brand(name);
						b.setID(id);
						entities.add(b);
					}
				}

			}

		};
		saxParser.parse(new InputSource(new StringReader(response)), handler);
		return entities;
	}

	public static List<IsEntity> parseResponse(String response, String type)
			throws SAXException, IOException, ParserConfigurationException {
		Log.v("RESPONSE", response);
		if (type.endsWith("locations")) {
			return parseLocationResponse(response);
		} else if (type.endsWith("branchproducts")
				|| type.endsWith("branchproduct")) {
			return parseBranchProductResponse(response);
		} else if (type.endsWith("products") || type.endsWith("product")) {
			return parseProductResponse(response);
		} else if (type.endsWith("categories") || type.endsWith("category")) {
			return parseCategoryResponse(response);
		} else if (type.endsWith("branches") || (type.endsWith("branch"))) {
			return parseBranchResponse(response);
		} else if (type.endsWith("brands")) {
			return parseBrandResponse(response);
		}
		return null;
	}
}
