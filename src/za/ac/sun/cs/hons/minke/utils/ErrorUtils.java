package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.content.Context;

public class ErrorUtils {
	public static CharSequence getErrorMessage(ERROR error, Context context) {
		switch (error) {
		case CLIENT:
			return context.getString(R.string.err_client_msg);
		case SERVER:
			return context.getString(R.string.err_server_msg);
		case PARSE:
			return context.getString(R.string.err_parse_msg);
		case INPUT:
			return context.getString(R.string.err_input_msg);
		case LOCATION:
			return context.getString(R.string.err_location_msg);
		case MAP:
			return context.getString(R.string.err_map_msg);
		case DB:
			return context.getString(R.string.err_db_msg);
		case NOT_LOADED:
			return context.getString(R.string.err_not_loaded_msg);
		case NOT_FOUND:
			return context.getString(R.string.err_not_found_msg);
		case DIRECTIONS:
			return context.getString(R.string.err_directions_msg);
		case SCAN:
			return context.getString(R.string.err_scan_msg);
		case TIME_OUT:
			return context.getString(R.string.err_time_out_msg);
		case SUCCESS:
			return context.getString(R.string.err_success_msg);
		}
		return null;
	}

	public static CharSequence getErrorTitle(ERROR error, Context context) {
		switch (error) {
		case CLIENT:
			return context.getString(R.string.err_client);
		case SERVER:
			return context.getString(R.string.err_server);
		case PARSE:
			return context.getString(R.string.err_parse);
		case INPUT:
			return context.getString(R.string.err_input);
		case LOCATION:
			return context.getString(R.string.err_location);
		case MAP:
			return context.getString(R.string.err_map);
		case DB:
			return context.getString(R.string.err_db);
		case NOT_LOADED:
			return context.getString(R.string.err_not_loaded);
		case NOT_FOUND:
			return context.getString(R.string.err_not_found);
		case DIRECTIONS:
			return context.getString(R.string.err_directions);
		case SCAN:
			return context.getString(R.string.err_scan);
		case TIME_OUT:
			return context.getString(R.string.err_time_out);
		case SUCCESS:
			return context.getString(R.string.err_success);
		}
		return null;
	}

}
