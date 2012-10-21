package za.ac.sun.cs.hons.minke.gui.shop;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.utils.ProductListAdapter;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class ShopFragment extends SherlockFragment {
	private ArrayAdapter<Product> productAdapter;
	AutoCompleteTextView shopping;
	ProductListAdapter shoplistAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_shop, container, false);
		initGUI(v);
		return v;
	}

	private void initGUI(View v) {
		productAdapter = new ArrayAdapter<Product>(getActivity(),
				R.layout.listitem_default, EntityUtils.getProducts());
		shopping = (AutoCompleteTextView) v.findViewById(R.id.text_shops);
		shopping.setAdapter(productAdapter);
		shopping.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addItem((Product) productAdapter.getItem(position));
			}

		});
		shopping.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				shopping.dismissDropDown();
				return false;
			}

		});
		shoplistAdapter = new ProductListAdapter(getActivity(),
				ShopUtils.getAddedProducts());
		ListView shoplist = (ListView) v.findViewById(R.id.list_shopping);
		shoplist.setAdapter(shoplistAdapter);
	}

	private void addItem(Product item) {
		if (item != null && !ShopUtils.getAddedProducts().contains(item)) {
			ShopUtils.addProduct(item);
			shoplistAdapter.notifyDataSetChanged();
		} else {
			Toast msg = Toast.makeText(this.getActivity(),
					getString(R.string.str_added), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL,
					0);
			msg.show();
		}

		shopping.setText("");
	}
}
