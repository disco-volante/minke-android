package za.ac.sun.cs.hons.minke.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.util.Log;

public class HTTPUtils {

	public static JSONObject doJSONPost(String mUrl, JSONObject obj)
			throws ClientProtocolException, IOException, JSONException {
		DefaultHttpClient httpClient = getClient();
		HttpPost httpost = new HttpPost(mUrl);
		StringEntity se;
		se = new StringEntity(obj.toString());
		se.setContentType("application/json;charset=UTF-8");
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json;charset=UTF-8"));
		httpost.setEntity(se);
		HttpResponse httpresponse = httpClient.execute(httpost);
		HttpEntity resultentity = httpresponse.getEntity();
		InputStream inputstream = resultentity.getContent();
		String resultstring = convertStreamToString(inputstream);
		inputstream.close();
		if (Debug.ON) {
			Log.v(TAGS.JSON, resultstring);
		}
		return new JSONObject(resultstring);
	}

	public static JSONObject doJSONPost(String url, JSONObject... jsons)
			throws JSONException, ClientProtocolException, IOException {
		JSONObject all = new JSONObject();
		for (JSONObject obj : jsons) {
			all.put(obj.getString("type"), obj);
		}
		return doJSONPost(url, all);
	}

	private static String convertStreamToString(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		return total.toString();
	}

	public static boolean startServer(String mUrl)
			throws ClientProtocolException, IOException {
		Log.v(TAGS.HTTP, mUrl);
		HttpGet getMethod = new HttpGet(mUrl + "start");
		DefaultHttpClient httpClient = getClient();
		HttpResponse httpresponse = httpClient.execute(getMethod);
		HttpEntity resultentity = httpresponse.getEntity();
		InputStream inputstream = resultentity.getContent();
		String resultstring = convertStreamToString(inputstream);
		if (Debug.ON) {
			Log.v(TAGS.JSON, resultstring);
		}
		return resultstring.equals("STARTED");
	}

	public static JSONObject doJSONGet(String mUrl) throws JSONException,
			ClientProtocolException, IOException {
		if (Debug.ON) {
			Log.v(TAGS.HTTP, mUrl);
		}
		HttpGet getMethod = new HttpGet(mUrl);
		DefaultHttpClient httpClient = getClient();
		HttpResponse httpresponse = httpClient.execute(getMethod);
		HttpEntity resultentity = httpresponse.getEntity();
		InputStream inputstream = resultentity.getContent();
		String resultstring = convertStreamToString(inputstream);
		inputstream.close();
		if (Debug.ON) {
			Log.v(TAGS.JSON, resultstring);
		}
		return new JSONObject(resultstring);
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
