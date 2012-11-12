package za.ac.sun.cs.hons.minke.gui.utils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.scan.NewBranchFragment;
import za.ac.sun.cs.hons.minke.gui.scan.ScanFragment;
import za.ac.sun.cs.hons.minke.gui.utils.ProductListAdapter.ViewHolder;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.ErrorUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
	private static boolean showing = false;

	public static Builder getErrorDialog(Context context, ERROR notLoaded) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder errorDlg = new AlertDialog.Builder(context);
		errorDlg.setTitle(ErrorUtils.getErrorTitle(notLoaded, context));
		errorDlg.setIcon(R.drawable.error);
		errorDlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		errorDlg.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		errorDlg.setMessage(ErrorUtils.getErrorMessage(notLoaded, context));
		showing = true;
		return errorDlg;
	}

	public static Builder getInfoDialog(final Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder infoDlg = new AlertDialog.Builder(context);
		infoDlg.setTitle(context.getString(R.string.app_name));
		infoDlg.setIcon(R.drawable.info);
		infoDlg.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		infoDlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		infoDlg.setNegativeButton(context.getString(R.string.website),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						Intent browserIntent = new Intent(Intent.ACTION_VIEW,
								Uri.parse(Constants.WEBSITE));
						context.startActivity(browserIntent);
						dialog.cancel();
					}
				});
		infoDlg.setMessage(context.getString(R.string.dlg_info));
		showing = true;
		return infoDlg;
	}

	public static Builder getChartDialog(Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(context.getString(R.string.edit_chart));
		dlg.setIcon(R.drawable.chart);
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		dlg.setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		showing = true;
		return dlg;
	}

	public static Builder getDirectionsDialog(Context context) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
		dlg.setTitle(context.getString(R.string.directions));
		dlg.setIcon(R.drawable.directions);
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		dlg.setPositiveButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		showing = true;
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
		AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(item.getProduct().toString());
		dlg.setView(infoView);
		dlg.setIcon(R.drawable.info);
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		dlg.setNegativeButton(activity.getString(R.string.close),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		showing = true;
		return dlg;
	}

	public static Builder getBranchesDialog(Activity activity) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder location = new AlertDialog.Builder(activity);
		location.setTitle(activity.getString(R.string.confirm_location));
		location.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
		return location;

	}

	public static Builder getUpdateDialog(final Activity activity,
			final BranchProduct found, Product product) {
		if (showing) {
			return null;
		}
		LayoutInflater factory = LayoutInflater.from(activity);
		final View updateView = factory.inflate(R.layout.dialog_update, null);
		final TextView productText = (TextView) updateView
				.findViewById(R.id.lbl_product);
		final EditText updatePriceText = (EditText) updateView
				.findViewById(R.id.text_price);
		if (found != null && found.getDatePrice() != null) {
			updatePriceText.setText(found.getDatePrice()._getFormattedPrice());
		}
		updatePriceText.addTextChangedListener(new TextErrorWatcher(activity,
				updatePriceText, true));
		productText.setText(product.toString());
		AlertDialog.Builder update = new AlertDialog.Builder(activity);
		update.setTitle(activity.getString(R.string.product_found));
		update.setView(updateView);
		update.setPositiveButton(activity.getString(R.string.update),
				new DialogInterface.OnClickListener() {
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
		update.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
		return update;

	}

	public static Builder getOverlayDialog(Context mContext, OverlayItem item) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
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
		AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(activity.getString(R.string.str_province));
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
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		dlg.setNegativeButton(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						((HomeActivity) activity).changeTab(VIEW.SCAN,
								ScanFragment.class.getName());
					}
				});
		showing = true;
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
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(activity.getString(R.string.str_directions));
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
						activity.startActivity(IntentUtils.getMapIntent(
								activity.getApplicationContext(), true));
					}
				});
		builder.setNegativeButton(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
		return builder;
	}

	public static Builder getQuantityDialog(Activity activity, final ViewHolder holder, final Product product) {
		if (showing) {
			return null;
		}
		LayoutInflater factory = LayoutInflater.from(activity);

		AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		View quantityView = factory.inflate(R.layout.dialog_quantity, null);
		final NumberPicker quantityPicker = (NumberPicker) quantityView
				.findViewById(R.id.picker_quantity);
		quantityPicker.setCurrent(1);
		dlg.setTitle(activity.getString(R.string.str_quantity));
		dlg.setIcon(R.drawable.settings);
		dlg.setPositiveButton(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
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
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
		return dlg;
	}

	public static Builder getGraphDialog(Activity activity, String product,
			Date date, double price) {
		if (showing) {
			return null;
		}
		AlertDialog.Builder dlg = new AlertDialog.Builder(activity);
		dlg.setTitle(product);
		dlg.setMessage(activity.getString(R.string.date)+ ": "+ DateFormat.getDateInstance(
						DateFormat.MEDIUM).format(date)
				+ "\n"
				+ activity.getString(R.string.price)
				+ ": R " + price);
		dlg.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				showing = false;
			}
		});
		showing = true;
		return dlg;
	}

}
