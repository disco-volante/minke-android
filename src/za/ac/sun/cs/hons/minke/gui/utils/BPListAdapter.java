package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BPListAdapter extends ArrayAdapter<IsEntity> {
	private Activity context;

	static class ViewHolder {
		protected TextView name, price;
	}

	public BPListAdapter(Activity context, ArrayList<IsEntity> bps) {
		super(context, R.layout.bp_rowlayout, bps);
		this.context = context;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final BranchProduct item = (BranchProduct) getItem(position);
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.bp_rowlayout, null);
			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showInfo(item);
				}
			});
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) rowView.findViewById(R.id.name_label);

			viewHolder.price = (TextView) rowView
					.findViewById(R.id.price_label);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.name.setText(item.getProduct().toString());
		holder.price.setText("R " + item.getPrice());
		return rowView;
	}

	protected void showInfo(BranchProduct item) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View infoView = factory.inflate(R.layout.info_view, null);
		final TextView brand = (TextView) infoView
				.findViewById(R.id.brand_text);
		final TextView store = (TextView) infoView
				.findViewById(R.id.store_text);
		final TextView size = (TextView) infoView.findViewById(R.id.size_text);
		final TextView price = (TextView) infoView
				.findViewById(R.id.price_text);
		final TextView date = (TextView) infoView.findViewById(R.id.date_text);
		brand.setText(item.getBrand());
		store.setText(item.getBranch());
		size.setText(item.getSize()+
		item.getMeasurement());
		price.setText("R " + item.getPrice());
		date.setText(item.getDate());
		AlertDialog.Builder infoDialog = new AlertDialog.Builder(context);
		infoDialog.setTitle(item.getProduct());
		infoDialog.setView(infoView);
		infoDialog.setIcon(R.drawable.shop_40);
		infoDialog.setPositiveButton("Close",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		infoDialog.show();
	}

}
