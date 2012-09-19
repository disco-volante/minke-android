package za.ac.sun.cs.hons.minke.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import android.util.Log;

public class HTTPUtils {
	private static final String LOG_TAG = "HTTPUtils";

	public static String doGetWithResponse(String mUrl,
			List<IsEntity>... entities) {
		mUrl = addAuth(mUrl);
		NameValuePair found = null;
		if (entities.length > 0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			for (List<IsEntity> entity : entities) {
				if ((found = formatEntities(entity)) != null) {
					params.add(found);
				}
			}
			mUrl = addParams(mUrl, params);
		}
		return attempt(mUrl);

	}

	public static String attempt(String mUrl) {
		Log.i(LOG_TAG, "URL= " + mUrl);
		String ret = "failed";
		HttpGet getMethod = new HttpGet(mUrl);
		DefaultHttpClient httpClient = getClient();
		try {
			HttpResponse response = httpClient.execute(getMethod);
			Log.i(LOG_TAG,
					"STATUS CODE: "
							+ String.valueOf(response.getStatusLine()
									.getStatusCode()));
			if (null != response) {
				ret = getResponseBody(response);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	private static String addAuth(String mUrl) {
		return addParams(mUrl,
				Arrays.asList(new NameValuePair[] { new BasicNameValuePair(
						"authority", "c5f4486abc034369842fc16a7b744085") }));
	}

	public static String doGetWithResponse(String url, long code,
			List<IsEntity>... entities) {
		url = addParams(url,
				Arrays.asList(new NameValuePair[] { new BasicNameValuePair(
						"barcode", String.valueOf(code)) }));
		return doGetWithResponse(url, entities);
	}

	@SuppressWarnings("unchecked")
	public static String doGetWithResponse(String url, double latitude,
			double longitude) {
		url = addParams(url,
				Arrays.asList(new NameValuePair[] {
						new BasicNameValuePair("latitude", String
								.valueOf(latitude)),
						new BasicNameValuePair("longitude", String
								.valueOf(longitude)) }));
		return doGetWithResponse(url);
	}

	public static String doPostWithResponse(String mUrl, long id, double price) {
		mUrl = addAuth(mUrl);
		mUrl = addParams(mUrl, Arrays.asList(new NameValuePair[] {
				new BasicNameValuePair("id", String.valueOf(id)),
				new BasicNameValuePair("price", String.valueOf(price)) }));
		return attempt(mUrl);

	}

	public static String doPostWithResponse(String mUrl, long barcode,
			long branchcode, IsEntity entity) {
		mUrl = addAuth(mUrl);
		mUrl = addParams(mUrl,
				Arrays.asList(new NameValuePair[] {
						new BasicNameValuePair("branchcode", String
								.valueOf(branchcode)),
						new BasicNameValuePair("barcode", String
								.valueOf(barcode)) }));
		mUrl = addParams(mUrl, formatEntity(entity));
		return attempt(mUrl);
	}

	private static List<NameValuePair> formatEntity(IsEntity entity) {
		if (entity == null) {
			return null;
		}
		NameValuePair type, name, brand, size, price, measure;
		if (entity instanceof BranchProduct) {
			BranchProduct bp = (BranchProduct) entity;
			type = new BasicNameValuePair("entity_type", "branchproduct");
			name = new BasicNameValuePair("product", bp.getProduct());
			brand = new BasicNameValuePair("brand", bp.getBrand());
			size = new BasicNameValuePair("size", bp.getSize() + "");
			price = new BasicNameValuePair("price", bp.getPrice() + "");
			measure = new BasicNameValuePair("measure", bp.getMeasurement());
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(type);
			nvps.add(name);
			nvps.add(brand);
			nvps.add(size);
			nvps.add(price);
			nvps.add(measure);
			return nvps;
		}
		return null;
	}

	private static NameValuePair formatEntities(
			List<? extends IsEntity> entities) {
		if (entities == null || entities.size() == 0) {
			return null;
		}
		String value = "";
		String key = "";
		IsEntity e = null;
		for (IsEntity ie : entities) {
			if (ie != null) {
				e = ie;
				break;
			}
		}
		if (e instanceof City) {
			key = "city";
		} else if (e instanceof Country) {
			key = "country";
		} else if (e instanceof Province) {
			key = "province";
		} else if (e instanceof Product) {
			key = "product";
		} else if (e instanceof Category) {
			key = "category";
		} else if (e instanceof CityLocation) {
			key = "cityLocation";
		} else if (e instanceof Branch) {
			key = "branch";
		} else {
			return null;
		}
		for (IsEntity entity : entities) {
			if (entity != null) {
				if (value.equals("")) {
					value += entity.getID();
				} else {
					value += "," + entity.getID();
				}
			}
		}
		NameValuePair param = new BasicNameValuePair(key, value);
		return param;
	}

	private static String addParams(String url, List<NameValuePair> params) {
		if (!url.contains("?")) {
			url += "?";
		} else {
			url += "&";
		}
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;
		return url;
	}

	private static String getResponseBody(HttpResponse response) {
		String response_text = null;
		HttpEntity entity = null;

		try {
			entity = response.getEntity();
			response_text = _getResponseBody(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if (entity != null) {
				try {
					entity.consumeContent();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return response_text;
	}

	private static String _getResponseBody(final HttpEntity entity)
			throws IOException, ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();

		if (instream == null) {
			return "";
		}

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(

			"HTTP entity too large to be buffered in memory");
		}

		String charset = getContentCharSet(entity);

		if (charset == null) {

			charset = HTTP.DEFAULT_CONTENT_CHARSET;

		}

		Reader reader = new InputStreamReader(instream, charset);

		StringBuilder buffer = new StringBuilder();

		try {

			char[] tmp = new char[1024];

			int l;

			while ((l = reader.read(tmp)) != -1) {

				buffer.append(tmp, 0, l);

			}

		} finally {

			reader.close();

		}

		return buffer.toString();

	}

	private static String getContentCharSet(final HttpEntity entity)
			throws ParseException {

		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		String charset = null;

		if (entity.getContentType() != null) {

			HeaderElement values[] = entity.getContentType().getElements();

			if (values.length > 0) {

				NameValuePair param = values[0].getParameterByName("charset");

				if (param != null) {

					charset = param.getValue();

				}

			}

		}

		return charset;

	}

	protected static DefaultHttpClient getClient() {
		DefaultHttpClient ret = null;
		HttpParams params = new BasicHttpParams();
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, "utf-8");
		params.setBooleanParameter("http.protocol.expect-continue", false);
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		final SSLSocketFactory sslSocketFactory = SSLSocketFactory
				.getSocketFactory();
		sslSocketFactory
				.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", sslSocketFactory, 443));

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		ret = new DefaultHttpClient(manager, params);
		return ret;
	}

}
