package za.ac.sun.cs.hons.minke.services;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.receivers.InfoProvider;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAGS.STATE, "UpdateWidgetService Called");
		City city = getCity(getApplicationContext());
		ArrayList<Branch> branches = getBranches();
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
		BranchProduct bp = EntityUtils.selectBranchProduct(
				getApplicationContext(), branches, city.getId());
		if (bp == null) {
			stopSelf();
			return super.onStartCommand(intent, flags, startId);
		}
		RemoteViews remoteViews = new RemoteViews(getApplicationContext()
				.getPackageName(), R.layout.widget_info);
		updateView(remoteViews, bp, city.getName());
		int[] allWidgetIds = intent
				.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		for (int widgetId : allWidgetIds) {

			Intent clickIntent = new Intent(getApplicationContext(),
					InfoProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					allWidgetIds);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					getApplicationContext(), 0, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.layout_info_widget,
					pendingIntent);
			PendingIntent appIntent = PendingIntent.getActivity(getApplicationContext(), 0, IntentUtils.getHomeIntent(getApplicationContext()), 0);
			remoteViews.setOnClickPendingIntent(R.id.img_header, appIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	private ArrayList<Branch> getBranches() {
		//if(city == null){
			return EntityUtils.getBranches(getApplicationContext());
		//}
		//return EntityUtils.getBranches(getApplicationContext(), city);
	}

	public static City getCity(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		long cityID = prefs.getLong("cityID", -1);
		if (cityID == -1) {
			MapUtils.refreshLocation((LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE));
			return MapUtils.changeCity(context);
		}
		return EntityUtils.getCity(context, cityID);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void updateView(RemoteViews remoteViews, BranchProduct bp, String city) {
		remoteViews.setTextViewText(R.id.text_location, city);
		if (bp.getProduct() != null) {
			if (bp.getProduct().getBrand() != null) {
				remoteViews.setTextViewText(R.id.text_product, bp.getProduct()
						.getBrand().toString()
						+ " " + bp.getProduct().getName());
			} else {
				remoteViews.setTextViewText(R.id.text_product, bp.getProduct()
						.getName());
			}
			if (bp.getProduct().getMeasure() != null) {
				remoteViews.setTextViewText(R.id.text_size, bp.getProduct()
						.getSizeString());
			}
			if (bp.getProduct().getBrand() != null) {
				remoteViews.setTextViewText(R.id.text_brand, bp.getProduct()
						.getBrand().toString());
			}
		}
		if (bp.getBranch() != null) {
			remoteViews.setTextViewText(R.id.text_store,
					String.valueOf(bp.getBranch().toString()));
		}
		if (bp.getDatePrice() != null) {
			remoteViews.setTextViewText(R.id.text_price, bp.getDatePrice()
					.getFormattedPrice());
			remoteViews.setTextViewText(R.id.text_date, bp.getDatePrice()
					.getFormattedDate());
		}
	}
}
