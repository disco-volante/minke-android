package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
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


public class LocationSearchActivity extends Activity {

	private AutoCompleteTextView locationBox;
	private ArrayAdapter<Object> locationAdapter;
	private ItemListAdapter<Object> locationListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_search);
		initBoxes();
		initLists();
	}
	


	private void initBoxes() {
		locationBox = (AutoCompleteTextView) findViewById(R.id.locationBox);
		locationBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				addItem( locationAdapter.getItem(position));
			}

		});
		locationAdapter = new ArrayAdapter<Object>(this,
				R.layout.dropdown_item, EntityUtils.getLocations());
		locationBox.setAdapter(locationAdapter);
	}

	private void initLists() {
		locationListAdapter = new ItemListAdapter<Object>(this,
				SearchUtils.getAddedLocations());

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

	protected void addItem(Object location) {
		if (location != null) {
			SearchUtils.addLocation(location);
			locationListAdapter.notifyDataSetChanged();
			locationBox.setText("");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu1, menu);
		return true;
	}


	public void getProductSearch(View view) {
		startActivity(IntentUtils.getProductSearchIntent(this));
	}

}
