package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.List;

import za.ac.sun.cs.hons.minke.gui.maps.google.Segment;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
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
		case ERROR.CLIENT:
			return "Please make sure data is enabled.";
		case ERROR.SERVER:
			return "Unfortunately the server is down, please try again later.";
		case ERROR.PARSE:
			return "Something went wrong when retrieving data, please notify the developers.";
		case ERROR.INPUT:
			return "Invalid input entered.";
		case ERROR.LOCATION:
			return "Your location could not be determined.";
		case ERROR.MAP:
			return "Something went wrong while creating map.";
		case ERROR.DB:
			return "An error occurred when accessing your local database.";
		case ERROR.NOT_LOADED:
			return "No data is currently available for this action, please wait for it to load.";
		case ERROR.SUCCESS:
			return "Action successfully completed.";
		}
		return null;
	}

	private static CharSequence getErrorTitle(int error) {
		switch (error) {
		case ERROR.CLIENT:
			return "Network Error";
		case ERROR.SERVER:
			return "Server Error";
		case ERROR.PARSE:
			return "Parsing Error";
		case ERROR.INPUT:
			return "Input Error";
		case ERROR.LOCATION:
			return "Location Error";
		case ERROR.MAP:
			return "Map Error";
		case ERROR.DB:
			return "Database Error.";
		case ERROR.NOT_LOADED:
			return "Data Unavailable.";
		case ERROR.SUCCESS:
			return "Success";
		}
		return null;
	}

}
