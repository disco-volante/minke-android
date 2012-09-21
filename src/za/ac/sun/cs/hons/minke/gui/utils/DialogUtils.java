package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.utils.Constants;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	public static Builder getErrorDialog(Context context, int error) {
		AlertDialog.Builder errorDlg = new AlertDialog.Builder(context);
		errorDlg.setTitle(getErrorTitle(error));
		errorDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		errorDlg.setMessage(getErrorMessage(error));
		return errorDlg;
	}

	private static CharSequence getErrorMessage(int error) {
		switch (error) {
		case Constants.CLIENT_ERROR:
			return "Please make sure data is enabled.";
		case Constants.SERVER_ERROR:
			return "Unfortunately the server is down, please try again later.";
		case Constants.PARSE_ERROR:
			return "Something went wrong when retrieving data, please notify the developers.";
		case Constants.INPUT_ERROR:
			return "Invalid input entered.";
		case Constants.LOCATION_ERROR:
			return "Your location could not be determined.";
		case Constants.SUCCESS:
			return "Action successfully completed.";
		}
		return null;
	}

	private static CharSequence getErrorTitle(int error) {
		switch (error) {
		case Constants.CLIENT_ERROR:
			return "Network Error";
		case Constants.SERVER_ERROR:
			return "Server Error";
		case Constants.PARSE_ERROR:
			return "Parsing Error";
		case Constants.INPUT_ERROR:
			return "Input Error";
		case Constants.LOCATION_ERROR:
			return "Location Error";
		case Constants.SUCCESS:
			return "Success";
		}
		return null;
	}

}
