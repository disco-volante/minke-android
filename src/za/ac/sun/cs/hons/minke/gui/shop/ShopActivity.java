package za.ac.sun.cs.hons.minke.gui.shop;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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

public class ShopActivity extends Activity {
	private ArrayAdapter<IsEntity> productAdapter;
	AutoCompleteTextView shopping;
	ItemListAdapter<IsEntity> shoplistAdapter;

	class FindBranchesTask extends ProgressTask {

		public FindBranchesTask() {
			super(ShopActivity.this, 1, "Searching",
					"Searching for branches...", true);
		}

		@Override
		protected void success() {
			startActivity(IntentUtils.getStoreIntent(ShopActivity.this));

		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(ShopActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							findStores(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(IntentUtils
									.getHomeIntent(ShopActivity.this));
							dialog.cancel();
						}
					});
			dlg.show();
		}

		@Override
		protected int retrieve(int counter) {
			return RPCUtils.retrieveBranches(ShopUtils.getAddedProducts(false));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_shop);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getNextAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
		productAdapter = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getProducts());
		shopping = (AutoCompleteTextView) findViewById(R.id.shoppingAutoComplete);

		shopping.setAdapter(productAdapter);

		shopping.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addItem((Product) productAdapter.getItem(position));
			}

		});
		shoplistAdapter = new ItemListAdapter<IsEntity>(this,
				ShopUtils.getAddedProducts(true));
		ListView shoplist = (ListView) findViewById(R.id.shoppingList);
		shoplist.setAdapter(shoplistAdapter);
		shoplist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ShopUtils.removeProduct(position);
				shoplistAdapter.notifyDataSetChanged();

			}
		});

	}

	private void addItem(Product item) {
		if (item != null) {
			ShopUtils.addProduct(item);
			shoplistAdapter.notifyDataSetChanged();
		}
		shopping.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shop_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getShopIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.next:
			startActivity(IntentUtils.getStoreIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void findStores(View view) {
		FindBranchesTask task = new FindBranchesTask();
		task.execute();
	}

}
