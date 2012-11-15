package za.ac.sun.cs.hons.minke.gui.shop;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.ProductListAdapter;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
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
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

public class ShopFragment extends SherlockFragment {
	private ArrayAdapter<Product> productAdapter;
	AutoCompleteTextView shopping;
	ProductListAdapter shoplistAdapter;
	private FindBranchesTask curTask;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View v = inflater.inflate(R.layout.fragment_shop, container, false);
		initGUI(v);
		return v;
	}

	private void initGUI(View v) {
		productAdapter = new ArrayAdapter<Product>(getActivity(),
				R.layout.listitem_default, EntityUtils.getProducts(getActivity().getApplicationContext()));
		shopping = (AutoCompleteTextView) v.findViewById(R.id.text_shops);
		shopping.setAdapter(productAdapter);
		shopping.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addItem(productAdapter.getItem(position));
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
		ImageButton searchButton = (ImageButton) v
				.findViewById(R.id.btn_stores);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				findStores();
			}

		});
	}

	private void addItem(Product item) {
		if (item != null && !ShopUtils.getAddedProducts().contains(item)) {
			ShopUtils.addProduct(item);
			shoplistAdapter.notifyDataSetChanged();
		} else {
			Toast msg = Toast.makeText(getActivity(),
					getString(R.string.str_added), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL,
					0);
			msg.show();
		}

		shopping.setText("");
	}

	public void findStores() {
		curTask = new FindBranchesTask(this);
		curTask.execute();
	}

	static class FindBranchesTask extends ProgressTask {

		private Fragment fragment;

		public FindBranchesTask(Fragment fragment) {
			super(fragment.getActivity(), fragment.getActivity().getString(
					R.string.searching)
					+ "...", fragment.getActivity().getString(
					R.string.searching_branch_msg));
			this.fragment = fragment;
		}

		@Override
		protected void success() {
			((HomeActivity) activity).changeTab(VIEW.SHOP, NAMES.STORE);

		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(activity, error_code);
			if(dlg == null){
				return;
			}
			dlg.setPositiveButton(activity.getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							((ShopFragment) fragment).findStores();
						}
					});
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			return EntityUtils.retrieveBranches(activity.getApplicationContext(), ShopUtils.getAddedProducts());
		}
	}
}
