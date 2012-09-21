package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BranchListAdapter extends ArrayAdapter<IsEntity> {
	private Activity context;

	static class ViewHolder {
		protected TextView store, total;
	}

	public BranchListAdapter(Activity context, ArrayList<IsEntity> branches) {
		super(context, R.layout.branch_rowlayout, branches);
		this.context = context;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final Branch item = (Branch) getItem(position);
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.branch_rowlayout, null);
			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					showInfo(item);
				}
			});
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.store = (TextView) rowView
					.findViewById(R.id.store_label);
			viewHolder.total = (TextView) rowView
					.findViewById(R.id.total_label);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.store.setText(item.toString());
		holder.total.setText("R "
				+ ShopUtils.getTotal(item.getBranchProducts()));
		return rowView;
	}

	protected void showInfo(Branch item) {
		BrowseUtils.setBranchProducts(item.getBranchProducts());
		BrowseUtils.setStoreBrowse(true);
		context.startActivity(IntentUtils.getBrowseIntent(context));
	}

}
