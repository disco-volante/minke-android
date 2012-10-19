package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.graph.ChartActivity;
import za.ac.sun.cs.hons.minke.gui.maps.google.GoogleMapsActivity;
import za.ac.sun.cs.hons.minke.gui.prefs.SettingsActivity;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import android.content.Context;
import android.content.Intent;

public class IntentUtils {
	public static Intent createShareIntent() {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "Shared from minke.");
		return Intent.createChooser(intent, "Share");
	}

	public static Intent getGraphIntent(Context context) {
		Intent graph = new Intent(context, ChartActivity.class);
		graph.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return graph;
	}

	public static Intent getMapIntent(Context context) {
		Intent map;
		if (Constants.GOOGLE_MAPS) {
			map = new Intent(context, GoogleMapsActivity.class);
		}
		map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return map;
	}

	public static Intent getHomeIntent(Context context) {
		Intent scanIntent = new Intent(context, HomeActivity.class);
		scanIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return scanIntent;
	}
	
	public static Intent getSettingsIntent(Context context) {
		Intent settings = new Intent(context, SettingsActivity.class);
		settings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return settings;
	}

	public static Intent getEmulatorIntent() {
		Intent intent = new Intent();
		intent.putExtra("SCAN_RESULT_FORMAT", "UPC_A");
		intent.putExtra("SCAN_RESULT",
				String.valueOf((long) (100000 * Math.random())));
		return intent;
	}




}
