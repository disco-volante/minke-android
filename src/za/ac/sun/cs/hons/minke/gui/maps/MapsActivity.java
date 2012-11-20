package za.ac.sun.cs.hons.minke.gui.maps;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.DirectionsListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import za.ac.sun.cs.hons.minke.utils.constants.DEBUG;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.NAMES;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.routing.overlay.RouteOverlay;
import com.routing.route.Route;

public class MapsActivity extends SherlockMapActivity {
	private MapView mapView;
	private MapController mapController;
	private AlertDialog dirDlg;
	private ProgressTask curTask;
	private boolean shop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		determineActions();

	}

	private void determineActions() {
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
			home();
		}
		shop = extras.getBoolean(NAMES.SHOP);
		if (!shop && !extras.getBoolean(NAMES.BROWSE)) {
			home();
		}
		if (shop
				&& (ShopUtils.getShopLists() == null || ShopUtils
						.getShopLists().size() == 0)) {
			home();
		} else if (!shop && (BrowseUtils.getCurrent() == null)) {
			home();
		} else {
			changeDestination(false);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		determineActions();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_directions:
			showDirections();
			return true;
		case R.id.menu_item_user:
			mapController.animateTo(MapUtils.getUserLocation());
			return true;
		case R.id.menu_item_destination:
			mapController.animateTo(MapUtils.getDestination());
			return true;
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_item_edit:
			changeDestination(true);
			return true;
		}
		return false;
	}

	private void changeDestination(boolean edit) {
		MapUtils.refreshLocation((LocationManager) getSystemService(Context.LOCATION_SERVICE));
		if (!shop || ShopUtils.getShopLists().size() == 1) {
			if (shop) {
				MapUtils.setDestination(ShopUtils.getShopLists().get(0)
						.getBranch().getCityLocation());
			}
			buildMap();
		} else {
			Builder builder = DialogUtils.getMapBranchesDialog(this);
			if (builder != null) {
				if (!edit) {
					builder.setOnCancelListener(new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							DialogUtils.showing = false;
							if (MapUtils.getDestination() == null) {
								MapUtils.setDestination(ShopUtils
										.getShopLists().get(0).getBranch()
										.getCityLocation());
							}
							buildMap();
						}
					});
				}
				builder.show();
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle args) {
	}

	@Override
	public void onRestoreInstanceState(Bundle args) {
	}

	private void addOverlay(Overlay routeOverlay) {
		mapView.getOverlays().add(routeOverlay);
		mapController.animateTo(MapUtils.getDestination());
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (curTask == null || curTask.getStatus().equals(Status.FINISHED)) {
			return null;
		}
		curTask.detach();
		return curTask;

	}

	public void home() {
		startActivity(IntentUtils.getHomeIntent(getApplicationContext()));
		finish();
	}

	@SuppressWarnings("deprecation")
	public void buildMap() {
		ERROR error = null;
		if (MapUtils.getUserLocation() == null) {
			error = MapUtils.refreshLocation((LocationManager) getApplication()
					.getSystemService(Context.LOCATION_SERVICE));

		}
		if (error != null && !error.equals(ERROR.SUCCESS)) {
			Builder dialog = DialogUtils.getErrorDialog(this, error);
			if (dialog != null) {
				dialog.setNegativeButton(R.string.ok, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						home();
					}
				});
				dialog.show();
			}
		} else {
			if (mapView == null) {
				createMap();
			} else {
				mapView.getOverlays().clear();
			}
			mapController = mapView.getController();
			mapController.zoomToSpan(
					Math.abs(MapUtils.getUserLocation().getLatitudeE6()
							- MapUtils.getDestination().getLatitudeE6()),
					Math.abs(MapUtils.getUserLocation().getLongitudeE6()
							- MapUtils.getDestination().getLongitudeE6()));
			mapController.setCenter(MapUtils.getUserLocation());
			buildOverlays();
			if (getLastNonConfigurationInstance() != null) {
				curTask = (BuildRouteTask) getLastNonConfigurationInstance();
				if (!curTask.getStatus().equals(Status.FINISHED)) {
					curTask.attach(this);
				}
			} else {
				getDirections();
			}
		}

	}

	private void buildOverlays() {
		BasicOverlay itemOverlay;
		if (shop) {
			itemOverlay = new BasicOverlay(this.getResources().getDrawable(
					R.drawable.shop), this);
			for (ShopList sl : ShopUtils.getShopLists()) {
				itemOverlay.addOverlay(new OverlayItem(sl.getBranch()
						.getCityLocation().getGeoPoint(), sl.toString(), sl
						.getBranch().getCityLocation().toString()));
			}
		} else {
			itemOverlay = new BasicOverlay(this.getResources().getDrawable(
					R.drawable.product), this);
			itemOverlay.addOverlay(new OverlayItem(BrowseUtils.getCurrent()
					.getBranch().getCityLocation().getGeoPoint(), BrowseUtils
					.getCurrent().toString(), BrowseUtils.getCurrent()
					.getBranch().toString()
					+ "\n"
					+ BrowseUtils.getCurrent().getBranch().getCityLocation()
							.getCity().toString()));
		}
		BasicOverlay iconOverlay = new BasicOverlay(this.getResources()
				.getDrawable(R.drawable.user), this);
		iconOverlay.addOverlay(new OverlayItem(MapUtils.getUserLocation(),
				getString(R.string.you), getString(R.string.str_you)));
		mapView.getOverlays().add(itemOverlay);
		mapView.getOverlays().add(iconOverlay);
	}

	private void createMap() {
		setContentView(R.layout.activity_maps);
		RelativeLayout holder = (RelativeLayout) findViewById(R.id.map_holder);
		if (!DEBUG.SIGNED) {
			mapView = new MapView(this, Constants.DEBUG_KEY);
		} else {
			mapView = new MapView(this, Constants.APPLICATION_KEY);
		}
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		holder.addView(mapView);
	}

	private void getDirections() {
		curTask = new BuildRouteTask(this);
		curTask.execute();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	public void showDirections() {
		if (MapUtils.directions != null && MapUtils.directions.size() > 0) {
			Builder dlg = DialogUtils.getDirectionsDialog(this);
			if (dlg == null) {
				return;
			}
			LayoutInflater factory = LayoutInflater.from(this);
			final View directionsView = factory.inflate(
					R.layout.dialog_directions, null);
			final ListView dirList = (ListView) directionsView
					.findViewById(R.id.list_directions);
			DirectionsListAdapter adapter = new DirectionsListAdapter(this,
					MapUtils.directions);
			dirList.setAdapter(adapter);
			dlg.setView(directionsView);
			dirDlg = dlg.show();
		}

	}

	public void plotPosition(GeoPoint point, String title, String message) {
		if (mapView.getOverlays().size() > 3) {
			mapView.getOverlays().remove(mapView.getOverlays().size() - 1);
		}
		BasicOverlay iconOverlay = new BasicOverlay(this.getResources()
				.getDrawable(R.drawable.pin), this);
		iconOverlay.addOverlay(new OverlayItem(point, title, message));
		mapView.getOverlays().add(iconOverlay);
		mapController.animateTo(point);
		if (dirDlg != null && dirDlg.isShowing()) {
			dirDlg.cancel();
		}

	}

	static class BuildRouteTask extends ProgressTask {
		private RouteOverlay routeOverlay;

		public BuildRouteTask(MapsActivity activity) {
			super(activity, activity.getString(R.string.retrieving) + "...",
					activity.getString(R.string.retrieving_msg));
		}

		@Override
		protected void success() {
			((MapsActivity) activity).addOverlay(routeOverlay);

		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
			if (dlg == null) {
				return;
			}
			if (error_code.equals(ERROR.DIRECTIONS)) {
				dlg.setPositiveButton(activity.getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
			} else {
				dlg.setPositiveButton(activity.getString(R.string.retry),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								((MapsActivity) activity).getDirections();
							}
						});
				dlg.setNegativeButton(activity.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
			}
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.DIRECTIONS;
			}
			try {
				Route route = MapUtils.directions();
				routeOverlay = new RouteOverlay(route, Color.BLUE);
				return ERROR.SUCCESS;
			} catch (Exception e) {
				if (e != null && e.getMessage() != null) {
					if (DEBUG.ON) {
						Log.v(TAGS.MAP, e.getMessage());
						e.printStackTrace();
					}
				}
				return ERROR.MAP;
			}
		}
	}

}
