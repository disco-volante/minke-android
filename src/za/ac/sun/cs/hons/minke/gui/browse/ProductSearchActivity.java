package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
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


public class ProductSearchActivity extends Activity {

	private AutoCompleteTextView searchBox;
	private ArrayAdapter<Product> productAdapter;
	private ArrayAdapter<Category> categoryAdapter;
	private ItemListAdapter<Product> productListAdapter;
	private ItemListAdapter<Category> categoryListAdapter;
	private ListView searchList;

	class SearchTask extends ProgressTask {

		public SearchTask() {
			super(ProductSearchActivity.this, "Searching...",
					"Searching for products");
		}

		@Override
		protected void success() {
			BrowseUtils.setBranchProducts(SearchUtils.getSearched());
			BrowseUtils.setStoreBrowse(false);
			startActivity(IntentUtils
					.getBrowseIntent(ProductSearchActivity.this));
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(
					ProductSearchActivity.this, error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							getProducts(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(IntentUtils
									.getHomeIntent(ProductSearchActivity.this));
							dialog.cancel();
						}
					});
			dlg.show();

		}

		@Override
		protected int retrieve() {
			return EntityUtils.retrieveBranchProducts(SearchUtils
					.isProductsActive());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_search);
		initBoxes();
		initLists();
		setProducts(this.getCurrentFocus());

	}
	


	private void initBoxes() {
		searchBox = (AutoCompleteTextView) findViewById(R.id.searchBox);
		searchBox.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (SearchUtils.isProductsActive()) {
					addItem(productAdapter.getItem(position));
				} else {
					addItem(categoryAdapter.getItem(position));
				}
			}

		});

		categoryAdapter = new ArrayAdapter<Category>(this,
				R.layout.dropdown_item, EntityUtils.getCategories());
		productAdapter = new ArrayAdapter<Product>(this,
				R.layout.dropdown_item, EntityUtils.getProducts());

	}

	private void initLists() {
		productListAdapter = new ItemListAdapter<Product>(this,
				SearchUtils.getAddedProducts());
		categoryListAdapter = new ItemListAdapter<Category>(this,
				SearchUtils.getAddedCategories());
		searchList = (ListView) findViewById(R.id.search_list);
		searchList.setAdapter(productListAdapter);
		searchList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (SearchUtils.isProductsActive()) {
					SearchUtils.removeProduct(position);
					productListAdapter.notifyDataSetChanged();

				} else {
					SearchUtils.removeCategory(position);
					categoryListAdapter.notifyDataSetChanged();

				}

			}
		});
	}

	protected void addItem(Object entity) {
		if (entity != null) {
			if (SearchUtils.isProductsActive()) {
				SearchUtils.addProduct((Product) entity);
				productListAdapter.notifyDataSetChanged();
			} else {
				SearchUtils.addCategory((Category) entity);
				categoryListAdapter.notifyDataSetChanged();
			}
			searchBox.setText("");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu1, menu);
		return true;
	}


	public void setProducts(View view) {
		SearchUtils.setProductsActive(true);
		searchList.setAdapter(productListAdapter);
		searchBox.setAdapter(productAdapter);
	}

	public void setCategories(View view) {
		SearchUtils.setProductsActive(false);
		searchList.setAdapter(categoryListAdapter);
		searchBox.setAdapter(categoryAdapter);
	}

	public void getProducts(View view) {
		SearchTask task = new SearchTask();
		task.execute();
	}

}
