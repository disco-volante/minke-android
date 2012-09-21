package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.Constants;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
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

import com.markupartist.android.widget.ActionBar;

public class ProductSearchActivity extends Activity {

	private AutoCompleteTextView searchBox;
	private ArrayAdapter<IsEntity> productAdapter;
	private ArrayAdapter<IsEntity> categoryAdapter;
	private ItemListAdapter<IsEntity> productListAdapter;
	private ItemListAdapter<IsEntity> categoryListAdapter;
	private ListView searchList;

	class SearchTask extends ProgressTask {
		private int error;

		public SearchTask() {
			super(ProductSearchActivity.this, 1, "Searching...",
					"Searching for products", true);
		}

		@Override
		protected void onPostExecute(Void v) {
			super.onPostExecute(v);
			if (error == Constants.SUCCESS) {
				BrowseUtils.setBranchProducts(SearchUtils.getSearched());
				BrowseUtils.setStoreBrowse(false);
				startActivity(IntentUtils
						.getBrowseIntent(ProductSearchActivity.this));
			} else {
				Builder dlg = DialogUtils.getErrorDialog(
						ProductSearchActivity.this, error);
				dlg.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								getProducts(null);
								dialog.cancel();
							}
						});
				dlg.show();
				dlg.show();
				startActivity(IntentUtils
						.getHomeIntent(ProductSearchActivity.this));
			}

		}

		@Override
		protected void retrieve(int counter) {
			error = RPCUtils.retrieveBranchProducts(SearchUtils
					.isProductsActive());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_search);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_product_search);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getNextAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
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

		categoryAdapter = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getCategories());
		productAdapter = new ArrayAdapter<IsEntity>(this,
				R.layout.dropdown_item, EntityUtils.getProducts());

	}

	private void initLists() {
		productListAdapter = new ItemListAdapter<IsEntity>(this,
				SearchUtils.getAddedProducts(true));
		categoryListAdapter = new ItemListAdapter<IsEntity>(this,
				SearchUtils.getAddedCategories(true));
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

	protected void addItem(IsEntity entity) {
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
		getMenuInflater().inflate(R.menu.product_search_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getProductSearchIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.next:
			startActivity(IntentUtils.getBrowseIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
