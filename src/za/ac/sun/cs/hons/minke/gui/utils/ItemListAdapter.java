package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public abstract class ItemListAdapter<T> extends ArrayAdapter<T> {
	private Activity activity;
	private int rowType;

	static class ViewHolder {
		public TextView text;
		public ImageButton btn;
		public Button other_btn;

	}

	public ItemListAdapter(Activity activity, ArrayList<T> added, int _rowType) {
		super(activity, _rowType, added);
		this.setActivity(activity);
		rowType = _rowType;

	}

	public ItemListAdapter(Activity activity, ArrayList<T> added) {
		super(activity, R.layout.row_removable, added);
		this.setActivity(activity);
		rowType = R.layout.row_removable;

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final T item = getItem(position);
		if (rowView == null) {
			rowView = inflate(rowView);
		}
		initHolder(item, rowView);
		if (PreferencesUtils.getAnimationLevel() == Constants.FULL) {
			animateItem(item, rowView);
		}
		return rowView;
	}

	protected void animateItem(T item, View rowView) {
		Animation down = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_down);
		rowView.setVisibility(View.VISIBLE);
		rowView.startAnimation(down);
	}

	protected ViewHolder initHolder(final T item, View rowView) {
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(item.toString());
		holder.text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast msg = Toast.makeText(getActivity(),
						holder.text.getText(), Toast.LENGTH_LONG);
				msg.setGravity(Gravity.CENTER_VERTICAL,
						Gravity.CENTER_HORIZONTAL, 0);
				msg.show();
			}
		});
		holder.btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (PreferencesUtils.getAnimationLevel() == Constants.FULL) {
					Animation up = AnimationUtils.loadAnimation(getActivity(),
							R.anim.slide_up);
					up.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation arg0) {
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

				} else {
					removeFromSearch(item);
				}

			}
		});
		return holder;
	}

	protected View inflate(View rowView) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		rowView = inflater.inflate(rowType, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_remove);
		viewHolder.btn = (ImageButton) rowView.findViewById(R.id.btn_remove);
		if (rowType == R.layout.row_product) {
			viewHolder.other_btn = (Button) rowView
					.findViewById(R.id.btn_quantity);
		}
		if (PreferencesUtils.getAnimationLevel() == Constants.FULL) {
			rowView.setVisibility(View.INVISIBLE);
		}
		rowView.setTag(viewHolder);
		return rowView;
	}

	public abstract void removeFromSearch(T item);

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}
