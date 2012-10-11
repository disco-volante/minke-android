package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.constants.Constants;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextErrorWatcher implements TextWatcher {
	EditText view;
	private boolean numeric;
	private Context context;

	public TextErrorWatcher(Context context, EditText view, boolean numeric) {
		this.view = view;
		this.numeric = numeric;
		this.context = context;
		afterTextChanged(view.getEditableText());
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s == null || s.toString() == null || s.toString().length() == 0) {
			view.setError(context.getString(R.string.str_input));
		}
		else if (numeric && !s.toString().matches(Constants.DECIMALS)) {
			view.setError(context.getString(R.string.str_input_numeric));
		}
		else{
			view.setError(null);
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
