package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.utils.Constants;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public class TextErrorWatcher implements TextWatcher {
	TextView view;
	private boolean numeric;

	public TextErrorWatcher(TextView view, boolean numeric) {
		this.view = view;
		this.numeric = numeric;
		afterTextChanged(view.getEditableText());
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s == null || s.toString() == null || s.toString().length() == 0) {
			view.setError("Input is required");
		}
		else if (numeric && !s.toString().matches(Constants.DECIMALS)) {
			view.setError("Input must be numeric, up to 2 decimal places and greater than 0.");
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
