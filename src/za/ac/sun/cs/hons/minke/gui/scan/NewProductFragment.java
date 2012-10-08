package za.ac.sun.cs.hons.minke.gui.scan;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.browse.BrowseFragment;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.SherlockFragment;

public class NewProductFragment extends SherlockFragment {

	private EditText nameText, priceText, sizeText;
	private AutoCompleteTextView brandBox, categoryBox;
	private Spinner unitSpinner;
	protected Brand brand;
	protected Category category;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_new_product, container,
				false);
		initGUI(v);
		return v;
	}

	private void initGUI(View v) {
		nameText = (EditText) v.findViewById(R.id.text_name);
		nameText.addTextChangedListener(new TextErrorWatcher(nameText, false));
		brandBox = (AutoCompleteTextView) v.findViewById(R.id.text_brand);
		final ArrayAdapter<Brand> brands = new ArrayAdapter<Brand>(
				getActivity(), R.layout.listitem_default,
				EntityUtils.getBrands());
		brandBox.setAdapter(brands);
		brandBox.addTextChangedListener(new TextErrorWatcher(brandBox, false));
		brandBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				brand = brands.getItem(position);
			}

		});
		categoryBox = (AutoCompleteTextView) v.findViewById(R.id.text_category);
		final ArrayAdapter<Category> categories = new ArrayAdapter<Category>(
				getActivity(), R.layout.listitem_default,
				EntityUtils.getCategories());
		categoryBox.setAdapter(categories);
		categoryBox.addTextChangedListener(new TextErrorWatcher(categoryBox,
				false));
		categoryBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				category = categories.getItem(position);
			}

		});
		priceText = (EditText) v.findViewById(R.id.text_price);
		priceText.addTextChangedListener(new TextErrorWatcher(priceText, true));
		sizeText = (EditText) v.findViewById(R.id.text_size);
		sizeText.addTextChangedListener(new TextErrorWatcher(sizeText, true));
		unitSpinner = (Spinner) v.findViewById(R.id.spinner_unit);
		SpinnerAdapter units = new ArrayAdapter<String>(getActivity(),
				R.layout.listitem_default, getMeasures());
		unitSpinner.setAdapter(units);
		Button btnCreateProduct = (Button) v
				.findViewById(R.id.btn_create_product);
		btnCreateProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				createProduct();
			}

		});
		clear();
	}

	private String[] getMeasures() {
		return new String[] { "mg", "g", "kg", "ml", "l", "ea" };
	}

	private void createProduct() {
		if (nameText.getError() != null || brandBox.getError() != null
				|| categoryBox.getError() != null
				|| sizeText.getError() != null || priceText.getError() != null) {
			AlertDialog.Builder errors = new AlertDialog.Builder(getActivity());
			StringBuilder msg = new StringBuilder();
			msg.append("Please enter a valid: ");
			if (nameText.getError() != null) {
				msg.append("product name, ");
			}
			if (brandBox.getError() != null) {
				msg.append("brand name, ");
			}
			if (categoryBox.getError() != null) {
				msg.append("category name, ");
			}
			if (sizeText.getError() != null) {
				msg.append("product size, ");
			}
			if (priceText.getError() != null) {
				msg.append("product price, ");
			}
			msg.replace(msg.length() - 2, msg.length(), ".");
			errors.setTitle("Invalid input");
			errors.setMessage(msg.toString());
			errors.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			errors.show();
		} else {
			String name = nameText.getText().toString();
			double size = Double.parseDouble(sizeText.getText().toString());
			int price = (int) Double
					.parseDouble(priceText.getText().toString()) * 100;
			CreateProductTask task = new CreateProductTask(name, brand,
					category, size, unitSpinner.getSelectedItem().toString(),
					price);
			task.execute();

		}

	}

	private void clear() {
		nameText.setText("");
		brandBox.setText("");
		categoryBox.setText("");
		priceText.setText("");
		sizeText.setText("");
	}

	class CreateProductTask extends ProgressTask {
		private String name;
		private Brand brand;
		private Category category;
		private double size;
		private int price;
		private String measure;

		public CreateProductTask(String name, Brand brand, Category category,
				double size, String measure, int price) {
			super(getActivity(), "Adding...", "Adding product to the database");
			this.name = name;
			this.brand = brand;
			this.category = category;
			this.size = size;
			this.price = price;
			this.measure = measure;
		}

		@Override
		protected void success() {
			((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
					BrowseFragment.class.getName());
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(getActivity(), error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							createProduct();
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
									ScanFragment.class.getName());
							dialog.cancel();
						}
					});
			dlg.show();

		}

		@Override
		protected ERROR retrieve() {
			if (!isNetworkAvailable()) {
				return ERROR.CLIENT;
			}
			if (brand != null && category != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brand, category, size, measure, price);
			} else if (brand != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brand, categoryBox.getText().toString(), size,
						measure, price);
			} else if (category != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brandBox.getText().toString(), category, size,
						measure, price);
			} else {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brandBox.getText().toString(), categoryBox
								.getText().toString(), size, measure, price);
			}
		}
	}

}
