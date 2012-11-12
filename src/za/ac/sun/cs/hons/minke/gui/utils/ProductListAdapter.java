package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.utils.PreferencesUtils;
import za.ac.sun.cs.hons.minke.utils.ShopUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import android.app.Activity;
import android.app.AlertDialog;
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

public class ProductListAdapter extends ArrayAdapter<Product> {
	private Activity activity;
	static class ViewHolder {
		public TextView text;
		public ImageButton btn;
		public Button qty_btn;

	}
	public ProductListAdapter(Activity _activity, ArrayList<Product> added) {
		super(_activity, R.layout.row_product, added);
		activity = _activity;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final Product product = getItem(position);
		if (rowView == null) {
			rowView = inflate();
			product.setQuantity(1);
		}
		initHolder(product, rowView);
		if (PreferencesUtils.getAnimationLevel() == Constants.FULL) {
			animateItem(rowView);
		}
		return rowView;
	}
	

	protected void changeQuantity(final ViewHolder holder, final Product product) {
		AlertDialog.Builder dlg = DialogUtils.getQuantityDialog(getActivity(),holder, product);
		dlg.show();

	}

	public void removeFromSearch(Product product) {
		ShopUtils.removeProduct(product);
		notifyDataSetChanged();
	}
	@Override
	public void notifyDataSetChanged(){
		super.notifyDataSetChanged();
	}




	protected void animateItem(View rowView) {
		Animation down = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_down);
		rowView.setVisibility(View.VISIBLE);
		rowView.startAnimation(down);
	}

	protected void initHolder(final Product product, View rowView) {
		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.text.setText(product.toString());
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
							removeFromSearch(product);
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
					removeFromSearch(product);
				}

			}
		});
		holder.qty_btn .setText(String.valueOf(product.getQuantity()));
		holder.qty_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				changeQuantity(holder, product);

			}
		});
	}

	protected View inflate() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View rowView = inflater.inflate(R.layout.row_product, null);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) rowView.findViewById(R.id.lbl_remove);
		viewHolder.btn = (ImageButton) rowView.findViewById(R.id.btn_remove);
		viewHolder.qty_btn = (Button) rowView
					.findViewById(R.id.btn_quantity);
		if (PreferencesUtils.getAnimationLevel() == Constants.FULL) {
			rowView.setVisibility(View.INVISIBLE);
		}
		rowView.setTag(viewHolder);
		return rowView;
	}

	public Activity getActivity() {
		return activity;
	}

}
