package za.ac.sun.cs.hons.minke.gui.utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.maps.MapsActivity;
import za.ac.sun.cs.hons.minke.gui.scan.NewBranchFragment;
import za.ac.sun.cs.hons.minke.gui.scan.ScanFragment;
import za.ac.sun.cs.hons.minke.gui.utils.ProductListAdapter.ViewHolder;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.ErrorUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.NAMES;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;

public class DialogUtils {
	static boolean showing = false;

	public static Builder getErrorDialog(Context context, ERROR notLoaded) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder errorDlg = new DefaultBuilder(context,
				ErrorUtils.getErrorTitle(notLoaded, context), R.drawable.error,
				ErrorUtils.getErrorMessage(notLoaded, context));
		return errorDlg;
	}

	public static Builder getInfoDialog(final Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder infoDlg = new DefaultBuilder(context,
				context.getString(R.string.app_name), R.drawable.info,
				context.getString(R.string.dlg_info));
		infoDlg.setPositiveButton(context.getString(R.string.website),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(Constants.WEBSITE));
						context.startActivity(browserIntent);
						dialog.cancel();
					}
				});
		return infoDlg;
	}

	public static Builder getChartDialog(Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dlg = new DefaultBuilder(context,
				context.getString(R.string.edit_chart), R.drawable.chart, null);
		return dlg;
	}

	public static Builder getDirectionsDialog(Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dlg = new DefaultBuilder(context,
				context.getString(R.string.directions), R.drawable.directions,
				null);
		return dlg;
	}

	public static Builder getProductInfoDialog(Activity activity,
			BranchProduct item) {
		if (showing) {
			return null;
		}
		LayoutInflater factory = LayoutInflater.from(activity);
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
		price.setText(item.getDatePrice().getFormattedPrice());
		date.setText(item.getDatePrice().getFormattedDate());
		AlertDialog.Builder dlg = new DefaultBuilder(activity, item
				.getProduct().toString(), R.drawable.product, null);
		dlg.setView(infoView);
		return dlg;
	}

	public static Builder getBranchesDialog(Activity activity) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder location = new DefaultBuilder(activity,
				activity.getString(R.string.confirm_location),
				R.drawable.directions, null);
		return location;

	}

	public static Builder getUpdateDialog(final Activity activity,
			final BranchProduct found, Product product) {
		if (showing) {
			return null;
		}

		AlertDialog.Builder update = new DefaultBuilder(activity,
				activity.getString(R.string.product_found),
				R.drawable.settings, null);
		LayoutInflater factory = LayoutInflater.from(activity);
		final View updateView = factory.inflate(R.layout.dialog_update, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.lbl_product);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.text_price);
		if (found != null && found.getDatePrice() != null) {
			updatePriceText.setText(found.getDatePrice()._getFormattedPrice());
			update.setNegativeButton(activity.getString(R.string.skip),
					new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							((HomeActivity) activity).changeTab(VIEW.SCAN,
									NAMES.BROWSE);
							dialog.cancel();
						}
					});
		}
		updatePriceText.addTextChangedListener(new TextErrorWatcher(activity,
				updatePriceText, true));
		productText.setText(product.toString());
		update.setView(updateView);
		update.setPositiveButton(activity.getString(R.string.update),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						if (updatePriceText.getError() == null) {
							int price = (int) (Double
									.parseDouble(updatePriceText.getText()
											.toString()) * 100);
							((HomeActivity) activity).updateProduct(found,
									price);
							dialog.cancel();
						}
					}
				});
		return update;

	}

	public static Builder getOverlayDialog(Context mContext, OverlayItem item) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dialog = new DefaultBuilder(mContext,
				item.getTitle(), -1, item.getSnippet());
		return dialog;
	}

	public static Builder getProvincesDialog(final Activity activity,
			final Fragment fragment) {
		if (showing) {
			return null;
		}
		final ArrayList<Province> provinces = EntityUtils.getProvinces(activity
				.getApplicationContext());
		if (provinces == null) {
			return null;
		}
		ScanUtils.province = provinces.get(0);
		final int size = Math.min(provinces.size(), 10);
		final String[] names = new String[size];
		int i = 0;
		for (Province p : provinces) {
			if (i == size) {
				break;
			}
			names[i++] = p.toString();
		}
		AlertDialog.Builder dlg = new DefaultBuilder(activity,
				activity.getString(R.string.str_province),
				R.drawable.directions, null);
		dlg.setSingleChoiceItems(names, 0,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int position) {
						ScanUtils.province = provinces.get(position);
					}
				});
		dlg.setPositiveButton(
				activity.getString(R.string.add) + " "
						+ activity.getString(R.string.branch),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						((NewBranchFragment) fragment).createBranch();
						dialog.cancel();
					}
				});
		dlg.setNegativeButton(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						((HomeActivity) activity).changeTab(VIEW.SCAN,
								ScanFragment.class.getName());
					}
				});
		return dlg;
	}

	public static Builder getMapBranchesDialog(final Activity activity) {
		if (showing) {
			return null;
		}
		String[] names = new String[ShopUtils.getShopLists().size()];
		MapUtils.setDestination(ShopUtils.getShopLists().get(0).getBranch()
				.getCityLocation());
		int i = 0;
		for (ShopList sl : ShopUtils.getShopLists()) {
			names[i++] = sl.toString();
		}
		AlertDialog.Builder builder = new DefaultBuilder(activity,
				activity.getString(R.string.str_directions),
				R.drawable.directions, null);
		builder.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				MapUtils.setDestination(ShopUtils.getShopLists().get(position)
						.getBranch().getCityLocation());
			}
		});
		builder.setPositiveButton(activity.getString(R.string.view_map),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						((MapsActivity) activity).buildMap();
					}
				});
		return builder;
	}

	public static Builder getQuantityDialog(Activity activity,
			final ViewHolder holder, final Product product) {
		if (showing) {
			return null;
		}
		LayoutInflater factory = LayoutInflater.from(activity);

		AlertDialog.Builder dlg = new DefaultBuilder(activity,
				activity.getString(R.string.str_quantity), R.drawable.settings,
				null);
		View quantityView = factory.inflate(R.layout.dialog_quantity, null);
		final NumberPicker quantityPicker = (NumberPicker) quantityView
				.findViewById(R.id.picker_quantity);
		quantityPicker.setCurrent(1);
		dlg.setView(quantityView);
		dlg.setPositiveButton(activity.getString(R.string.change),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						product.setQuantity(quantityPicker.getCurrent());
						holder.qty_btn.setText(String.valueOf(quantityPicker
								.getCurrent()));
						dialog.cancel();
					}
				});
		return dlg;
	}

	public static Builder getGraphDialog(Activity activity, String product,
			Date date, double price) {
		if (showing) {
			return null;
		}
		String msg = activity.getString(R.string.date) + ": "
				+ DateFormat.getDateInstance(DateFormat.MEDIUM).format(date)
				+ "\n" + activity.getString(R.string.price) + ": R " + price;
		AlertDialog.Builder dlg = new DefaultBuilder(activity, product,
				R.drawable.chart, msg);
		return dlg;
	}

}
