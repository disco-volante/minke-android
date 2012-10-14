package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogUtils {
	public static Builder getErrorDialog(Context context, ERROR notLoaded) {
		AlertDialog.Builder errorDlg = new AlertDialog.Builder(context);
		errorDlg.setTitle(getErrorTitle(notLoaded, context));
		errorDlg.setIcon(R.drawable.error);
		errorDlg.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		errorDlg.setMessage(getErrorMessage(notLoaded, context));
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
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(Constants.WEBSITE));
						context.startActivity(browserIntent);
						dialog.cancel();
					}
				});
		infoDlg.setMessage(context.getString(R.string.dlg_info));
		return infoDlg;
	}

	public static Builder getChartDialog(Context context) {
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(context.getString(R.string.edit_chart));
		dlg.setIcon(R.drawable.chart);
		dlg.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return dlg;
	}

	public static Builder getDirectionsDialog(Context context) {
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(context.getString(R.string.directions));
		dlg.setIcon(R.drawable.directions);
		dlg.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return dlg;
	}

	public static Builder getProductInfoDialog(Activity context,
			BranchProduct item) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View infoView = factory.inflate(R.layout.dialog_info, null);
		final TextView brand = (TextView) infoView
				.findViewById(R.id.brand_text);
		final TextView store = (TextView) infoView
				.findViewById(R.id.text_store);
		final TextView size = (TextView) infoView.findViewById(R.id.text_size);
		final TextView price = (TextView) infoView
				.findViewById(R.id.text_price);
		final TextView date = (TextView) infoView.findViewById(R.id.text_date);
		brand.setText(item.getProduct().getBrand().getName());
		store.setText(item.getBranch().toString());
		size.setText(item.getProduct().getSize()
				+ item.getProduct().getMeasure());
		price.setText("R " + item.getDatePrice().getActualPrice());
		date.setText(item.getDatePrice().getFormattedDate());
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(item.getProduct().toString());
		dlg.setView(infoView);
		dlg.setIcon(R.drawable.info);
		dlg.setPositiveButton(context.getString(R.string.close),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		return dlg;
	}

	private static CharSequence getErrorMessage(ERROR error, Context context) {
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
		case SUCCESS:
			return context.getString(R.string.err_success_msg);
		}
		return null;
	}

	private static CharSequence getErrorTitle(ERROR error, Context context) {
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
		case SUCCESS:
			return context.getString(R.string.err_success);
		}
		return null;
	}

}
