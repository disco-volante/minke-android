package za.ac.sun.cs.hons.minke.gui.shop;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.ShopList;
import za.ac.sun.cs.hons.minke.gui.utils.ShopListAdapter;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class StoreActivity extends Activity {

	private ShopListAdapter shopListAdapter;
	protected Branch branch;
	private ArrayList<ShopList> shopLists;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu2, menu);
		return true;
	}

	
	private void initGUI() {
		setContentView(R.layout.store);
		shopLists = ShopUtils.getShopLists();
		shopListAdapter = new ShopListAdapter(this, shopLists);
		ListView storeList = (ListView) findViewById(R.id.store_list);
		storeList.setAdapter(shopListAdapter);
		shopListAdapter.notifyDataSetChanged();

	}

	public void showDirections(View view) {
		String[] names = new String[ShopUtils.getBranches().size()];
		MapUtils.setBranches(ShopUtils.getBranches());
		MapUtils.setDestination(0);
		int i = 0;
		for (Branch b : ShopUtils.getBranches()) {
			names[i++] = b.toString();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Directions to?");
		builder.setSingleChoiceItems(names, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int position) {
				MapUtils.setDestination(position);
			}
		});
		builder.setPositiveButton("View Map",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						startActivity(IntentUtils
								.getMapIntent(StoreActivity.this));
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

}
