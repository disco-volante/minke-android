package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BPListAdapter extends ArrayAdapter<BranchProduct> {
	private Activity activity;

	static class ViewHolder {
		protected TextView name, price;
	}

	public BPListAdapter(Activity _activity, ArrayList<BranchProduct> bps) {
		super(_activity, R.layout.row_branchproduct, bps);
		activity = _activity;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final BranchProduct item = getItem(position);
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.row_branchproduct, null);
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
		holder.price.setText(item.getDatePrice().getFormattedPrice());
		return rowView;
	}

	protected void showInfo(final BranchProduct item) {
		BrowseUtils.setCurrent(null);
		Builder dlg = DialogUtils.getProductInfoDialog(activity, item);
		if(dlg == null){
			return;
		}
		dlg.setNeutralButton(R.string.share,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						shareItem(item);
						dialog.cancel();
					}

				});
		dlg.setPositiveButton(R.string.product_location,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						showMap(item);
						dialog.cancel();
					}

				});
		dlg.show();

	}

	protected void showMap(BranchProduct item) {
		BrowseUtils.setCurrent(item);
		MapUtils.refreshLocation((LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE));		
		MapUtils.setDestination(item.getBranch().getCityLocation());
		activity.startActivity(IntentUtils
				.getMapIntent(activity, false));
	}

	protected void shareItem(BranchProduct item) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		intent.putExtra(Intent.EXTRA_SUBJECT, R.string.found_deal);
		intent.putExtra(Intent.EXTRA_TEXT, item.getProduct().toString() + "\n "
				+ item.getBranch().toString() + "\n"
				+ item.getDatePrice().getFormattedPrice());
		activity.startActivity(Intent.createChooser(intent,
				activity.getString(R.string.how_share)));

	}

}
