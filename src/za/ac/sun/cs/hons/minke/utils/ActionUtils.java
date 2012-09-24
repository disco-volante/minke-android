package za.ac.sun.cs.hons.minke.utils;

import za.ac.sun.cs.hons.minke.HomeActivity;
import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.browse.BrowseActivity;
import za.ac.sun.cs.hons.minke.gui.browse.LocationSearchActivity;
import za.ac.sun.cs.hons.minke.gui.browse.ProductSearchActivity;
import za.ac.sun.cs.hons.minke.gui.create.NewBranchActivity;
import za.ac.sun.cs.hons.minke.gui.create.NewProductActivity;
import za.ac.sun.cs.hons.minke.gui.graph.GraphActivity;
import za.ac.sun.cs.hons.minke.gui.maps.google.GoogleMapsActivity;
import za.ac.sun.cs.hons.minke.gui.maps.nutiteq.NutiteqMapsActivity;
import za.ac.sun.cs.hons.minke.gui.shop.ShopActivity;
import za.ac.sun.cs.hons.minke.gui.shop.StoreActivity;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class ActionUtils {
	private static class ToastAction extends AbstractAction {
		private Context context;

		public ToastAction(Context context) {
			super(R.drawable.ic_title_export_default);
			this.context = context;
		}

		@Override
		public void performAction(View view) {
			Toast.makeText(context, "Example action", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public static ToastAction getToastAction(Context context) {
		return new ToastAction(context);
	}

	public static Action getShareAction(Context context) {
		return new IntentAction(context, IntentUtils.createShareIntent(),
				R.drawable.ic_title_share_default);
	}

	public static Action getHomeAction(Context context) {
		return new IntentAction(context, IntentUtils.getHomeIntent(context),
				R.drawable.ic_launcher_40);
	}

	public static Action getShopAction(Context context) {
		return new IntentAction(context, IntentUtils.getShopIntent(context),
				R.drawable.shop_40);
	}

	public static Action getBrowseAction(Context context) {
		return new IntentAction(context, IntentUtils.getBrowseIntent(context),
				R.drawable.browse_40);
	}

	public static Action getNewProductAction(Context context) {
		return new IntentAction(context, IntentUtils.getNewProductIntent(context),
				R.drawable.scan_40);
	}

	public static Action getLocationSearchAction(Context context) {
		return new IntentAction(context,
				IntentUtils.getLocationSearchIntent(context),
				R.drawable.location_search_40);
	}

	public static Action getProductSearchAction(Context context) {
		return new IntentAction(context,
				IntentUtils.getProductSearchIntent(context),
				R.drawable.product_search_40);
	}

	public static Action getGraphAction(Context context) {
		return new IntentAction(context, IntentUtils.getGraphIntent(context),
				R.drawable.graph_40);
	}

	public static Action getMapAction(Context context) {
		return new IntentAction(context,
				IntentUtils.getMapIntent(context), R.drawable.map_40);
	}

	public static Action getStoreAction(Context context) {
		return new IntentAction(context, IntentUtils.getStoreIntent(context),
				R.drawable.store_40);
	}
	

	public static Action getScanAction(Context context) {
		return new IntentAction(context, IntentUtils.getScanIntent(context),
				R.drawable.scan_40);
	}

	public static Action getRefreshAction(Activity activity) {
		if (activity instanceof BrowseActivity) {
			return new IntentAction(activity,
					IntentUtils.getBrowseIntent(activity),
					R.drawable.refresh_40);
		}
		if (activity instanceof StoreActivity) {
			return new IntentAction(activity,
					IntentUtils.getStoreIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof NewProductActivity) {
			return new IntentAction(activity,
					IntentUtils.getNewProductIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof NewBranchActivity) {
			return new IntentAction(activity,
					IntentUtils.getNewBranchIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof ShopActivity) {
			return new IntentAction(activity,
					IntentUtils.getShopIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof HomeActivity) {
			return new IntentAction(activity,
					IntentUtils.getHomeIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof GraphActivity) {
			return new IntentAction(activity,
					IntentUtils.getGraphIntent(activity), R.drawable.refresh_40);
		}
		if (activity instanceof NutiteqMapsActivity || activity instanceof GoogleMapsActivity) {
			return new IntentAction(activity,
					IntentUtils.getMapIntent(activity),
					R.drawable.refresh_40);
		}
		if (activity instanceof LocationSearchActivity) {
			return new IntentAction(activity,
					IntentUtils.getLocationSearchIntent(activity),
					R.drawable.refresh_40);
		}
		if (activity instanceof ProductSearchActivity) {
			return new IntentAction(activity,
					IntentUtils.getProductSearchIntent(activity),
					R.drawable.refresh_40);
		}
		return null;
	}

	public static Action getNextAction(Activity activity) {
		if (activity instanceof BrowseActivity) {
			return new IntentAction(activity,
					IntentUtils.getGraphIntent(activity), R.drawable.next_40);
		}

		if (activity instanceof ShopActivity) {
			return new IntentAction(activity,
					IntentUtils.getStoreIntent(activity), R.drawable.next_40);
		}
		if (activity instanceof LocationSearchActivity) {
			return new IntentAction(activity,
					IntentUtils.getProductSearchIntent(activity),
					R.drawable.next_40);
		}
		if (activity instanceof ProductSearchActivity) {
			return new IntentAction(activity,
					IntentUtils.getBrowseIntent(activity), R.drawable.next_40);
		}
		return null;
	}



}
