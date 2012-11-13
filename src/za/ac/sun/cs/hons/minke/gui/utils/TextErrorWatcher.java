package za.ac.sun.cs.hons.minke.gui.utils;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.constants.REGEX;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextErrorWatcher implements TextWatcher {
	EditText view;
	private boolean numeric;
	private Context context;

	public TextErrorWatcher(Context _context, EditText _view, boolean _numeric) {
		view = _view;
		numeric = _numeric;
		context = _context;
		afterTextChanged(view.getEditableText());
	}

	@Override
	public void afterTextChanged(Editable s) {
		if (s == null || s.toString() == null || s.toString().length() == 0) {
			view.setError(context.getString(R.string.str_input));
		} else if (numeric && !REGEX.DECIMALS_0.matcher(s.toString()).matches()
				&& !REGEX.DECIMALS_1.matcher(s.toString()).matches()
				&& !REGEX.DECIMALS_2.matcher(s.toString()).matches()) {
			view.setError(context.getString(R.string.str_input_numeric));
		} else if(!numeric && !REGEX.STRING.matcher(s.toString()).matches()){
			view.setError(context.getString(R.string.str_input_chars));
		}else{
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
