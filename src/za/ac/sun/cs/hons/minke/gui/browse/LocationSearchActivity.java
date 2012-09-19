package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.Location;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.markupartist.android.widget.ActionBar;

public class LocationSearchActivity extends Activity {

	private AutoCompleteTextView locationBox;
	private ArrayAdapter<IsEntity> locationAdapter;
	private ItemListAdapter<IsEntity> locationListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_search);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_location_search);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getNextAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
		initBoxes();
		initLists();
	}

	private void initBoxes() {
		locationBox = (AutoCompleteTextView) findViewById(R.id.locationBox);
		locationBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				addItem((Location) locationAdapter.getItem(position));
			}

		});
		locationAdapter = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getLocations());
		locationBox.setAdapter(locationAdapter);
	}

	private void initLists() {
		locationListAdapter = new ItemListAdapter<IsEntity>(this,
				SearchUtils.getAddedLocations(true));

		ListView locationList = (ListView) findViewById(R.id.location_list);
		locationList.setAdapter(locationListAdapter);
		locationList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SearchUtils.removeLocation(position);
				locationListAdapter.notifyDataSetChanged();

			}
		});
	}

	protected void addItem(Location location) {
		if (location != null) {
			SearchUtils.addLocation(location);
			locationListAdapter.notifyDataSetChanged();
			locationBox.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.location_search_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getLocationSearchIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.next:
			startActivity(IntentUtils.getProductSearchIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void getProductSearch(View view) {
		startActivity(IntentUtils.getProductSearchIntent(this));
	}

}
