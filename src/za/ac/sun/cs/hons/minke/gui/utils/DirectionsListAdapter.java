package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.maps.google.GoogleMapsActivity;
import za.ac.sun.cs.hons.minke.gui.maps.google.Segment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DirectionsListAdapter extends ArrayAdapter<Segment> {
	private GoogleMapsActivity activity;
	private int rowType;

	static class ViewHolder {
		public TextView text;
		public ImageButton btn;

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
			rowView = inflate(rowView);
		}
		initHolder(item, rowView);
		return rowView;
	}

	protected ViewHolder initHolder(final Segment item, View rowView) {
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(item.toString());
		holder.text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast msg = Toast.makeText(activity, holder.text.getText(),
						Toast.LENGTH_LONG);
				msg.setGravity(Gravity.CENTER_VERTICAL,
						Gravity.CENTER_HORIZONTAL, 0);
				msg.show();
			}
		});
		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				activity.plotPosition(item.startPoint(),
						activity.getString(R.string.directions),
						item.toString());

			}
		});
		return holder;
	}

	protected View inflate(View rowView) {
		LayoutInflater inflater = activity.getLayoutInflater();
		rowView = inflater.inflate(rowType, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_dir);
		viewHolder.btn = (ImageButton) rowView.findViewById(R.id.btn_dir);
		rowView.setTag(viewHolder);
		return rowView;
	}

}
