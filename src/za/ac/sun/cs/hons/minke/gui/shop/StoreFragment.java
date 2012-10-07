package za.ac.sun.cs.hons.minke.gui.shop;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.ShopList;
import za.ac.sun.cs.hons.minke.gui.utils.ShopListAdapter;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class StoreFragment extends SherlockFragment {

	private ShopListAdapter shopListAdapter;
	protected Branch branch;
	private ArrayList<ShopList> shopLists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_store, container, false);
		initGUI(v);
		return v;
	}

	private void initGUI(View v) {
		shopLists = ShopUtils.getShopLists();
		shopListAdapter = new ShopListAdapter((HomeActivity) getActivity(), shopLists);
		ListView storeList = (ListView) v.findViewById(R.id.list_stores);
		storeList.setAdapter(shopListAdapter);
		shopListAdapter.notifyDataSetChanged();

	}

}
