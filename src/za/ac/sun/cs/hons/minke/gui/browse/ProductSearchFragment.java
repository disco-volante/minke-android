package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.ItemListAdapter;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.SearchUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.actionbarsherlock.app.SherlockFragment;

public class ProductSearchFragment extends SherlockFragment {
	private AutoCompleteTextView searchBox;
	private ArrayAdapter<Product> productAdapter;
	private ArrayAdapter<Category> categoryAdapter;
	private ItemListAdapter<Product> productListAdapter;
	private ItemListAdapter<Category> categoryListAdapter;
	private ListView searchList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_product_search, container, false);
		initBoxes(v);
		initLists(v);
		setItems(true);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SearchUtils.getAddedCategories().clear();
		SearchUtils.getAddedProducts().clear();
		productAdapter.notifyDataSetChanged();
		categoryAdapter.notifyDataSetChanged();
		BrowseUtils.getBranchProducts().clear();
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
		categoryAdapter = new ArrayAdapter<Category>(getActivity(),
				R.layout.listitem_default, EntityUtils.getCategories());
		productAdapter = new ArrayAdapter<Product>(getActivity(),
				R.layout.listitem_default, EntityUtils.getProducts());
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
	}

	private void initLists(View v) {
		productListAdapter = new ItemListAdapter<Product>(getActivity(),
				SearchUtils.getAddedProducts()){
					@Override
					public void removeFromSearch(Product product) {
						SearchUtils.removeProduct(product);
						notifyDataSetChanged();
					}
			
		};
		categoryListAdapter = new ItemListAdapter<Category>(getActivity(),
				SearchUtils.getAddedCategories()){
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

}
