package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.maps.MapsActivity;
import za.ac.sun.cs.hons.minke.gui.maps.Segment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DirectionsListAdapter extends ArrayAdapter<Segment> {
	private MapsActivity activity;
	private int rowType;

	static class ViewHolder {
		public TextView text;

	}

	public DirectionsListAdapter(MapsActivity _activity, ArrayList<Segment> added) {
		super(_activity, R.layout.row_directions, added);
		rowType = R.layout.row_directions;
		activity = _activity;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final Segment item = getItem(position);
		if (rowView == null) {
			rowView = inflate();
		}
		initHolder(item, rowView);
		return rowView;
	}

	protected ViewHolder initHolder(final Segment item, View rowView) {
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(item.toString());
		holder.text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				activity.plotPosition(item.startPoint(),
						activity.getString(R.string.directions),
						item.toString());
			}
		});
		return holder;
	}

	protected View inflate() {
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(rowType, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_dir);
		rowView.setTag(viewHolder);
		return rowView;
	}

}
