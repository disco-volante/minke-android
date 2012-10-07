package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemListAdapter<T> extends ArrayAdapter<T> {
	private Activity context;
	static class ViewHolder {
		public TextView text;
		public ImageButton btn;
	}

	public ItemListAdapter(Activity context, ArrayList<T> added) {
		super(context, R.layout.row_default, added);
		this.context = context;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final T item = getItem(position);
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.row_default, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_remove);
			viewHolder.btn = (ImageButton) rowView.findViewById(R.id.btn_remove);
			viewHolder.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ItemListAdapter.this.remove(item);
					notifyDataSetChanged();
				}
			});
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(item.toString());
		return rowView;
	}

}
