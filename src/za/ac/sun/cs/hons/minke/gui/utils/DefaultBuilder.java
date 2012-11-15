package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.R;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class DefaultBuilder extends Builder {

	public DefaultBuilder(Context context, CharSequence title, int icon,
			CharSequence msg) {
		super(context);
		DialogUtils.showing = true;
		setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				DialogUtils.showing = false;
			}
		});
		setNegativeButton(context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		if (title != null) {
			setTitle(title);
		}
		if (icon != -1) {
			setIcon(icon);
		}
		if (msg != null) {
			setMessage(msg);
		}
	}

}
