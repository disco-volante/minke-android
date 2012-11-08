package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.chart.ChartActivity;
import za.ac.sun.cs.hons.minke.gui.maps.MapsActivity;
import za.ac.sun.cs.hons.minke.gui.prefs.SettingsActivity;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.NAMES;
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

	public static Intent getMapIntent(Context context, boolean shop) {
		Intent map = new Intent(context, MapsActivity.class);
		map.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (shop) {
			map.putExtra(NAMES.SHOP, true);
		} else {
			map.putExtra(NAMES.BROWSE, true);
		}
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
		if (DEBUG.EMULATOR) {
			intent.putExtra("SCAN_RESULT",
					String.valueOf(ScanUtils.getDummyCode()));
		}
		return intent;
	}

}
