package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;


public class LocationSearchFragment  extends SherlockFragment {

	private AutoCompleteTextView locationBox;
	private ArrayAdapter<Object> locationAdapter;
	private ItemListAdapter<Object> locationListAdapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_location_search, container, false);
		initBoxes(v);
		initLists(v);
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		SearchUtils.getAddedLocations().clear();
		locationListAdapter.notifyDataSetChanged();
	}


	private void initBoxes(View v) {
		locationBox = (AutoCompleteTextView) v.findViewById(R.id.text_location);
		locationAdapter = new ArrayAdapter<Object>(this.getActivity(),
				R.layout.listitem_default, EntityUtils.getLocations());
		locationBox.setAdapter(locationAdapter);
		locationBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				addItem( locationAdapter.getItem(position));
			}

		});

	}

	private void initLists(View v) {
		locationListAdapter = new ItemListAdapter<Object>(this.getActivity(),
				SearchUtils.getAddedLocations());
		ListView locationList = (ListView) v.findViewById(R.id.location_list);
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

	
}
