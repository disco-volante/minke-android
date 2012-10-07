package za.ac.sun.cs.hons.minke.utils.constants;

public class Constants {
	public final static String URL_BASE = Debug.LOCAL ? "http://10.0.2.2:5000"
			: "http://kraken.shop-minke.appspot.com";
	public final static String REQUEST_BASE = "/entityRequestServlet?type=";
	public final static String URL_PREFIX = URL_BASE + REQUEST_BASE;
	public static final String USER_ID = "pieterj";
	public final static String KEY = "5fd0b37cd7dbbb00f97ba6ce92bf5add503d2db5495ad7.42776752";
	public static final int ROUTING_CLOUDMADE = 0;
	public static final int ROUTING_YOURNAVIGATION = 1;
	public static final String DECIMALS = "([1-9][0-9]*)+(\\.[0-9]{1,2}+)?";
	public static final String INTS = "([1-9][0-9]*)";
	public static final boolean GOOGLE_MAPS = true;
	public static final String EMULATOR_KEY = "0TkWntAlV0iBGVtYwpBCtoTg0fnuESyzoWnBjsg";
	public static final String APPLICATION_KEY = "0TkWntAlV0iAoUcY6-k3tkcMPE48tjyGkM9qVWA";
	public static final int NO_FREQUENCY_SET = -1;
	public static final int STARTUP = 0;
	public static final int HOURLY = 1;
	public static final int DAILY = 2;
	public static final int WEEKLY = 3;
	public static final int NEVER = 4;
	public static final long HOUR = 3600000;
	public static final long DAY = 86400000;
	public static final long WEEK = 604800000;
	public static final String WEBSITE = "https://github.com/pieterjordaan/minke-android";

}
