package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.maps.google.GoogleMapsActivity;
import za.ac.sun.cs.hons.minke.gui.maps.google.Segment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DirectionsListAdapter extends ArrayAdapter<Segment> {
	private GoogleMapsActivity activity;
	private int rowType;

	static class ViewHolder {
		public TextView text;

	}

	public DirectionsListAdapter(GoogleMapsActivity activity,
			ArrayList<Segment> added) {
		super(activity, R.layout.row_directions, added);
		rowType = R.layout.row_directions;
		this.activity = activity;
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
