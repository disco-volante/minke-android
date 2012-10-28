package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ProductListAdapter extends ItemListAdapter<Product> {

	public ProductListAdapter(Activity activity, ArrayList<Product> added) {
		super(activity, added, R.layout.row_product);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final Product product = getItem(position);
		if (rowView == null) {
			rowView = inflate();
		}
		final ViewHolder holder = initHolder(product, rowView);

		holder.other_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				changeQuantity(holder, product);

			}
		});
		// animateItem(product, rowView);
		return rowView;
	}

	protected void changeQuantity(final ViewHolder holder, final Product product) {
		LayoutInflater factory = LayoutInflater.from(getActivity());

		AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
		View quantityView = factory.inflate(R.layout.dialog_quantity, null);
		final NumberPicker quantityPicker = (NumberPicker) quantityView
				.findViewById(R.id.picker_quantity);
		quantityPicker.setCurrent(1);
		dlg.setTitle(getActivity().getString(R.string.str_quantity));
		dlg.setIcon(R.drawable.settings);
		dlg.setPositiveButton(getActivity().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		dlg.setView(quantityView);
		dlg.setPositiveButton(getActivity().getString(R.string.change),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						product.setQuantity(quantityPicker.getCurrent());
						holder.other_btn.setText(String.valueOf(quantityPicker
								.getCurrent()));
						dialog.cancel();
					}
				});
		dlg.show();

	}

	@Override
	public void removeFromSearch(Product product) {
		ShopUtils.removeProduct(product);
		notifyDataSetChanged();
	}

}
