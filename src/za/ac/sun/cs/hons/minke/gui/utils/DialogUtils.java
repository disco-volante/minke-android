package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.List;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.maps.google.Segment;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class DialogUtils {
	public static Builder getErrorDialog(Context context, ERROR notLoaded) {
		AlertDialog.Builder errorDlg = new AlertDialog.Builder(context);
		errorDlg.setTitle(getErrorTitle(notLoaded));
		errorDlg.setIcon(R.drawable.error);
		errorDlg.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		errorDlg.setMessage(getErrorMessage(notLoaded));
		return errorDlg;
	}
	public static Builder getInfoDialog(final Context context) {
		AlertDialog.Builder infoDlg = new AlertDialog.Builder(context);
		infoDlg.setTitle(context.getString(R.string.app_name));
		infoDlg.setIcon(R.drawable.info);
		infoDlg.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		infoDlg.setNegativeButton(context.getString(R.string.website),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WEBSITE));
						context.startActivity(browserIntent);
						dialog.cancel();
					}
				});
		infoDlg.setMessage(context.getString(R.string.info));
		return infoDlg;
	}
	public static Builder getDirectionsDialog(Context context,
			List<Segment> items, String title) {
		StringBuilder msg = new StringBuilder();
		int i = 0;
		double prev = 0.0;
		for (Segment s : items) {
			i++;
			msg.append(i);
			msg.append(") ");
			msg.append(prev+" -> "+s.getDistance());
			msg.append(" km : ");
			msg.append(s.getInstruction());
			msg.append(context.getString(R.string.str_continue));
			msg.append(getDist(s.getLength()));
			prev = s.getDistance();
		}
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(title);
		dlg.setIcon(R.drawable.directions);
		dlg.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		dlg.setMessage(msg.toString());
		return dlg;
	}

	private static String getDist(int length) {
		if(length > 1000){
			double actual = ((double)length)/1000;
			return actual+"km.\n";
		}
		else{
			return length+"m.\n";
		}
	}

	private static CharSequence getErrorMessage(ERROR error) {
		switch (error) {
		case CLIENT:
			return "Could not connect to the server, please make sure data is enabled.";
		case SERVER:
			return "Unfortunately the server is down, please try again later.";
		case PARSE:
			return "Something went wrong when retrieving data, please notify the developers.";
		case INPUT:
			return "Invalid input entered.";
		case LOCATION:
			return "Your location could not be determined.";
		case MAP:
			return "Something went wrong while creating map.";
		case DB:
			return "An error occurred when accessing your local database.";
		case NOT_LOADED:
			return "No data is currently available for this action, please wait for it to load.";
		case NOT_FOUND:
			return "Product was not found. Create product?";
		case SUCCESS:
			return "Action successfully completed.";
		}
		return null;
	}

	private static CharSequence getErrorTitle(ERROR error) {
		switch (error) {
		case CLIENT:
			return "Network Error";
		case SERVER:
			return "Server Error";
		case PARSE:
			return "Parsing Error";
		case INPUT:
			return "Input Error";
		case LOCATION:
			return "Location Error";
		case MAP:
			return "Map Error";
		case DB:
			return "Database Error.";
		case NOT_LOADED:
			return "Data Unavailable.";
		case NOT_FOUND:
			return "Not found.";
		case SUCCESS:
			return "Success";
		}
		return null;
	}



}
