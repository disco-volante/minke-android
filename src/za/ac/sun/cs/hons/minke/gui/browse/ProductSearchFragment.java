package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.NAMES;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class ProductSearchFragment extends SherlockFragment {
	private AutoCompleteTextView searchBox;
	private ArrayAdapter<Product> productAdapter;
	private ArrayAdapter<Category> categoryAdapter;
	private ItemListAdapter<Product> productListAdapter;
	private ItemListAdapter<Category> categoryListAdapter;
	private ListView searchList;
	private SearchTask curTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View v = inflater.inflate(R.layout.fragment_product_search, container,
				false);
		initBoxes(v);
		initLists(v);
		setItems(true);
		return v;
	}

	private void initBoxes(View v) {
		searchBox = (AutoCompleteTextView) v.findViewById(R.id.text_search);
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
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				searchBox.dismissDropDown();
				return false;
			}

		});
		categoryAdapter = new ArrayAdapter<Category>(getActivity(),
				R.layout.listitem_default, EntityUtils.getCategories(getActivity().getApplicationContext()));
		productAdapter = new ArrayAdapter<Product>(getActivity(),
				R.layout.listitem_default, EntityUtils.getProducts(getActivity().getApplicationContext()));
		RadioButton productBtn = (RadioButton) v
				.findViewById(R.id.rbtn_product);
		productBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setItems(true);
			}
		});
		RadioButton categoryBtn = (RadioButton) v
				.findViewById(R.id.rbtn_category);
		categoryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setItems(false);
			}
		});
		ImageButton searchButton = (ImageButton) v
				.findViewById(R.id.btn_product_search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getProducts();
			}

		});
	}

	public void getProducts() {
		curTask = new SearchTask(this);
		curTask.execute();
	}

	private void initLists(View v) {
		productListAdapter = new ItemListAdapter<Product>(getActivity(),
				SearchUtils.getAddedProducts()) {
			@Override
			public void removeFromSearch(Product product) {
				SearchUtils.removeProduct(product);
				notifyDataSetChanged();
			}

		};
		categoryListAdapter = new ItemListAdapter<Category>(getActivity(),
				SearchUtils.getAddedCategories()) {
			@Override
			public void removeFromSearch(Category category) {
				SearchUtils.removeCategory(category);
				notifyDataSetChanged();
			}

		};
		searchList = (ListView) v.findViewById(R.id.search_list);
		searchList.setAdapter(productListAdapter);
	}

	protected void addItem(Object entity) {
		if (entity != null
				&& !SearchUtils.getAddedCategories().contains(entity)
				&& !SearchUtils.getAddedProducts().contains(entity)) {
			if (SearchUtils.isProductsActive()) {
				SearchUtils.addProduct((Product) entity);
				productListAdapter.notifyDataSetChanged();
			} else {
				SearchUtils.addCategory((Category) entity);
				categoryListAdapter.notifyDataSetChanged();
			}
			searchBox.setText("");
		} else {
			Toast msg = Toast.makeText(getActivity(),
					getString(R.string.str_added), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL,
					0);
			msg.show();
		}

	}

	public void setItems(boolean products) {
		SearchUtils.setProductsActive(products);
		if (products) {
			searchList.setAdapter(productListAdapter);
			searchBox.setAdapter(productAdapter);
		} else {
			searchList.setAdapter(categoryListAdapter);
			searchBox.setAdapter(categoryAdapter);
		}
	}

	static class SearchTask extends ProgressTask {

		private Fragment fragment;

		public SearchTask(Fragment _fragment) {
			super(_fragment.getActivity(), _fragment.getActivity().getString(
					R.string.searching)
					+ "...", _fragment.getActivity().getString(
					R.string.searching_product_msg));
			fragment = _fragment;
		}

		@Override
		protected void success() {
			BrowseUtils.setBranchProducts(SearchUtils.getSearched());
			BrowseUtils.setStoreBrowse(false);
			((HomeActivity) activity).changeTab(VIEW.BROWSE, NAMES.BROWSE);

		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
			dlg.setPositiveButton(activity.getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							((ProductSearchFragment) fragment).getProducts();
							dialog.cancel();
						}
					});
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			return EntityUtils.retrieveBranchProducts(activity, SearchUtils
					.isProductsActive());
		}
	}

}
