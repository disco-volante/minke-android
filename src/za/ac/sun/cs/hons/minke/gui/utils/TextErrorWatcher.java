package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextErrorWatcher implements TextWatcher {
	TextView view;
	private boolean numeric;
	private Context context;

	public TextErrorWatcher(Context context, TextView view, boolean numeric) {
		this.view = view;
		this.numeric = numeric;
		afterTextChanged(view.getEditableText());
		this.context = context;
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s == null || s.toString() == null || s.toString().length() == 0) {
			view.setError(context.getString(R.string.str_input));
		}
		else if (numeric && !s.toString().matches(Constants.DECIMALS)) {
			view.setError(context.getString(R.string.str_input_numeric));
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

};
