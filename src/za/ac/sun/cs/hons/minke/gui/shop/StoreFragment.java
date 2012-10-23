package za.ac.sun.cs.hons.minke.gui.shop;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.ShopListAdapter;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ShopList;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class StoreFragment extends SherlockFragment {

	private ShopListAdapter shopListAdapter;
	protected Branch branch;
	private ArrayList<ShopList> shopLists;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View v = inflater.inflate(R.layout.fragment_store, container, false);
		initGUI(v);
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		shopListAdapter.notifyDataSetChanged();
	}

	private void initGUI(View v) {
		shopLists = ShopUtils.getShopLists();
		shopListAdapter = new ShopListAdapter((HomeActivity) getActivity(),
				shopLists);
		ListView storeList = (ListView) v.findViewById(R.id.list_stores);
		storeList.setAdapter(shopListAdapter);
		shopListAdapter.notifyDataSetChanged();
		ImageButton mapButton = (ImageButton) v.findViewById(R.id.btn_map);
		mapButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDirections();
			}

		});

	}

	public void showDirections() {
		String[] names = new String[ShopUtils.getShopLists().size()];
		MapUtils.setDestination(ShopUtils.getShopLists().get(0));
		int i = 0;
		for (ShopList sl : ShopUtils.getShopLists()) {
			names[i++] = sl.toString();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.str_directions));
		builder.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				MapUtils.setDestination(ShopUtils.getShopLists().get(position));
			}
		});
		builder.setPositiveButton(getString(R.string.view_map),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(IntentUtils
								.getMapIntent(StoreFragment.this.getActivity()));
					}
				});
		builder.setNegativeButton(getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

}
