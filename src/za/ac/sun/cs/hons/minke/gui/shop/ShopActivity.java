package za.ac.sun.cs.hons.minke.gui.shop;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
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


public class ShopActivity extends Activity {
	private ArrayAdapter<Product> productAdapter;
	AutoCompleteTextView shopping;
	ItemListAdapter<Product> shoplistAdapter;

	class FindBranchesTask extends ProgressTask {

		public FindBranchesTask() {
			super(ShopActivity.this, "Searching",
					"Searching for branches...");
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
		protected int retrieve() {
			return EntityUtils.retrieveBranches(ShopUtils.getAddedProducts(false));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop);
		productAdapter = new ArrayAdapter<Product>(this,
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
		shoplistAdapter = new ItemListAdapter<Product>(this,
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
		getMenuInflater().inflate(R.menu.default_menu1, menu);
		return true;
	}

	

	public void findStores(View view) {
		FindBranchesTask task = new FindBranchesTask();
		task.execute();
	}

}
