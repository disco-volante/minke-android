package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.List;

import za.ac.sun.cs.hons.minke.gui.maps.google.Segment;
import za.ac.sun.cs.hons.minke.utils.Constants;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {
	public static Builder getErrorDialog(Context context, int error) {
		AlertDialog.Builder errorDlg = new AlertDialog.Builder(context);
		errorDlg.setTitle(getErrorTitle(error));
		errorDlg.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		errorDlg.setMessage(getErrorMessage(error));
		return errorDlg;
	}

	public static Builder getDirectionsDialog(Context context,
			List<Segment> items, String title) {
		StringBuilder msg = new StringBuilder();
		int i = 0;
		for (Segment s : items) {
			i++;
			msg.append(i);
			msg.append(") ");
			msg.append(s.getDistance());
			msg.append(" km -> ");
			msg.append(s.getInstruction());
			msg.append(" for ");
			msg.append(s.getLength());
			msg.append(" m .\n");
		}
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(title);
		dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		dlg.setMessage(msg.toString());
		return dlg;
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
		case Constants.MAP_ERROR:
			return "Something went wrong while creating map.";
		case Constants.DB_ERROR:
			return "An error occurred when accessing your local database.";
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
		case Constants.MAP_ERROR:
			return "Map Error";
		case Constants.DB_ERROR:
			return "Database Error.";
		case Constants.SUCCESS:
			return "Success";
		}
		return null;
	}

}
