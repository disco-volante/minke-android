package za.ac.sun.cs.hons.minke.gui.create;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class NewProductActivity extends Activity {

	private EditText nameText, priceText, sizeText;
	private AutoCompleteTextView brandBox, categoryBox;
	private Spinner unitSpinner;
	protected Brand brand;
	protected Category category;

	class CreateProductTask extends ProgressTask {
		private String name;
		private Brand brand;
		private Category category;
		private double size;
		private int price;
		private String measure;

		public CreateProductTask(String name, Brand brand, Category category,
				double size, String measure, int price) {
			super(NewProductActivity.this, "Adding...",
					"Adding product to the database");
			this.name = name;
			this.brand = brand;
			this.category = category;
			this.size = size;
			this.price = price;
			this.measure = measure;
		}

		@Override
		protected void success() {
			startActivity(IntentUtils.getBrowseIntent(NewProductActivity.this));
		}

		@Override
		protected void failure(int error_code) {
			Builder dlg = DialogUtils.getErrorDialog(NewProductActivity.this,
					error_code);
			dlg.setPositiveButton("Retry",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							addProduct(null);
							dialog.cancel();
						}
					});
			dlg.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(IntentUtils
									.getHomeIntent(NewProductActivity.this));
							dialog.cancel();
						}
					});
			dlg.show();

		}

		@Override
		protected int retrieve() {
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();
		showErrorMessage();
	}

	private void initGUI() {
		setContentView(R.layout.new_product);
		nameText = (EditText) findViewById(R.id.nameText);
		nameText.addTextChangedListener(new TextErrorWatcher(nameText, false));
		brandBox = (AutoCompleteTextView) findViewById(R.id.brandText);
		final ArrayAdapter<Brand> brands = new ArrayAdapter<Brand>(this,
				R.layout.dropdown_item, EntityUtils.getBrands());
		brandBox.setAdapter(brands);
		brandBox.addTextChangedListener(new TextErrorWatcher(brandBox, false));
		brandBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				brand = brands.getItem(position);
			}

		});
		categoryBox = (AutoCompleteTextView) findViewById(R.id.categoryText);
		final ArrayAdapter<Category> categories = new ArrayAdapter<Category>(
				this, R.layout.dropdown_item, EntityUtils.getCategories());
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
		priceText = (EditText) findViewById(R.id.priceText);
		priceText.addTextChangedListener(new TextErrorWatcher(priceText, true));
		sizeText = (EditText) findViewById(R.id.sizeText);
		sizeText.addTextChangedListener(new TextErrorWatcher(sizeText, true));
		unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
		SpinnerAdapter units = new ArrayAdapter<String>(this,
				R.layout.dropdown_item, getMeasures());
		unitSpinner.setAdapter(units);

		clear();
	}

	private String[] getMeasures() {
		return new String[] { "mg", "g", "kg", "ml", "l", "ea" };
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.default_menu2, menu);
		return true;
	}

	private void showErrorMessage() {

		AlertDialog.Builder failed = new AlertDialog.Builder(this);
		failed.setTitle("Product Not Found");
		failed.setMessage("Product was not found in our database.");
		failed.setPositiveButton("Add product",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						initGUI();
						dialog.cancel();
					}
				});
		failed.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(IntentUtils
								.getHomeIntent(NewProductActivity.this));
						dialog.cancel();
					}
				});
		failed.show();
	}

	public void addProduct(View view) {
		if (nameText.getError() != null || brandBox.getError() != null
				|| categoryBox.getError() != null
				|| sizeText.getError() != null || priceText.getError() != null) {
			AlertDialog.Builder errors = new AlertDialog.Builder(this);
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
			int price = (int) Double.parseDouble(sizeText.getText().toString()) * 100;
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
}
