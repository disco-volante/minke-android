package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;
import java.util.HashSet;

import za.ac.sun.cs.hons.minke.R;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ItemListAdapter<T> extends ArrayAdapter<T> {
	private Activity activity;
	private HashSet<T> items = new HashSet<T>();

	static class ViewHolder {
		public TextView text;
		public ImageButton btn;
	}

	public ItemListAdapter(Activity activity, ArrayList<T> added) {
		super(activity, R.layout.row_default, added);
		this.activity = activity;

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (getCount() == 0) {
			items = new HashSet<T>();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final T item = getItem(position);
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.row_default, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_remove);

			viewHolder.btn = (ImageButton) rowView
					.findViewById(R.id.btn_remove);
			rowView.setVisibility(View.INVISIBLE);
			rowView.setTag(viewHolder);
		}
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
				Animation up = AnimationUtils.loadAnimation(activity,
						R.anim.slide_up);
				up.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
						items.remove(item);
						removeFromSearch(item);
					}


					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationStart(Animation arg0) {
					}

				});
				((View) view.getParent()).startAnimation(up);
			}
		});
		if (!items.contains(item)) {
			Animation down = AnimationUtils.loadAnimation(activity,
					R.anim.slide_down);
			rowView.setVisibility(View.VISIBLE);
			rowView.startAnimation(down);
		}
		items.add(item);
		return rowView;
	}
	
	public abstract void removeFromSearch(T item);
		
}
