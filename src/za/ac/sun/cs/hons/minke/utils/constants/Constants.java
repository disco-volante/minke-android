package za.ac.sun.cs.hons.minke.utils.constants;

public class Constants {
	public final static String URL_BASE = DEBUG.LOCAL ? "http://10.0.2.2:5000"
			: "http://kraken.shop-minke.appspot.com";
	public final static String REQUEST_BASE = "/entityRequestServlet?type=";
	public final static String URL_PREFIX = URL_BASE + REQUEST_BASE;
	public static final boolean GOOGLE_MAPS = true;
	public static final String DEBUG_KEY = "0TkWntAlV0iBGVtYwpBCtoTg0fnuESyzoWnBjsg";
	public static final String APPLICATION_KEY = "0TkWntAlV0iAoUcY6-k3tkcMPE48tjyGkM9qVWA";
	public static final int NO_FREQUENCY_SET = -1;
	public static final int STARTUP = 0;
	public static final int HOURLY = 1;
	public static final int DAILY = 2;
	public static final int WEEKLY = 3;
	public static final int NEVER = 4;
	public static final String WEBSITE = "https://github.com/pieterjordaan/minke-android";
	public static final int FULL = 0;
	public static final int STANDARD = 1;
	public static final int NONE = 2;

}
