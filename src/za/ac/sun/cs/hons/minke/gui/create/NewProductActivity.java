package za.ac.sun.cs.hons.minke.gui.create;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.gui.utils.DialogUtils;
import za.ac.sun.cs.hons.minke.gui.utils.TextErrorWatcher;
import za.ac.sun.cs.hons.minke.tasks.ProgressTask;
import za.ac.sun.cs.hons.minke.tasks.StoreDataTask;
import za.ac.sun.cs.hons.minke.utils.ActionUtils;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.IntentUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.markupartist.android.widget.ActionBar;

public class NewProductActivity extends Activity {

	private EditText nameText, priceText, sizeText;
	private AutoCompleteTextView brandText, categoryText;
	private Spinner unitSpinner;
	private long code;
	protected Branch branch;

	class AddProductTask extends ProgressTask {
		private BranchProduct branchProduct;

		public AddProductTask(BranchProduct branchProduct) {
			super(NewProductActivity.this, 1, "Adding...",
					"Adding product to the database", true);
			this.branchProduct = branchProduct;
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
		protected int retrieve(int counter) {
			return RPCUtils.addBranchProduct(branchProduct, code,
					branch.getID());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGUI();
		showErrorMessage();
	}
	@Override
	public void onPause(){
		super.onPause();
		StoreDataTask task = new StoreDataTask(this);
		task.execute();
	}
	private void initGUI() {
		setContentView(R.layout.new_product);
		nameText = (EditText) findViewById(R.id.nameText);
		nameText.addTextChangedListener(new TextErrorWatcher(nameText, false));
		brandText = (AutoCompleteTextView) findViewById(R.id.brandText);
		ArrayAdapter<Brand> brands = new ArrayAdapter<Brand>(this,
				R.layout.dropdown_item, EntityUtils.getBrands());
		brandText.setAdapter(brands);
		brandText
				.addTextChangedListener(new TextErrorWatcher(brandText, false));
		categoryText = (AutoCompleteTextView) findViewById(R.id.categoryText);
		ArrayAdapter<Category> categories = new ArrayAdapter<Category>(this,
				R.layout.dropdown_item, EntityUtils.getCategories());
		categoryText.setAdapter(categories);
		categoryText.addTextChangedListener(new TextErrorWatcher(categoryText,
				false));
		priceText = (EditText) findViewById(R.id.priceText);
		priceText.addTextChangedListener(new TextErrorWatcher(priceText, true));
		sizeText = (EditText) findViewById(R.id.sizeText);
		sizeText.addTextChangedListener(new TextErrorWatcher(sizeText, true));
		unitSpinner = (Spinner) findViewById(R.id.unitSpinner);
		SpinnerAdapter units = new ArrayAdapter<String>(this,
				R.layout.dropdown_item, getMeasures());
		unitSpinner.setAdapter(units);
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar_newproduct);
		actionBar.setHomeAction(ActionUtils.getHomeAction(this));
		actionBar.addAction(ActionUtils.getRefreshAction(this));
		actionBar.addAction(ActionUtils.getShareAction(this));
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			startActivity(IntentUtils.getNewProductIntent(this));
			return true;
		case R.id.home:
			startActivity(IntentUtils.getHomeIntent(this));
			return true;
		case R.id.settings:
			startActivity(IntentUtils.getSettingsIntent(this));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		if (nameText.getError() != null || brandText.getError() != null
				|| categoryText.getError() != null
				|| sizeText.getError() != null || priceText.getError() != null) {
			return;
		}
		BranchProduct bp = new BranchProduct(nameText.getText().toString(),
				brandText.getText().toString(), null, null,
				Double.parseDouble(priceText.getText().toString()),
				Double.parseDouble(sizeText.getText().toString()), unitSpinner
						.getSelectedItem().toString(), null);
		AddProductTask task = new AddProductTask(bp);
		task.execute();
	}

	private void clear() {
		nameText.setText("");
		brandText.setText("");
		categoryText.setText("");
		priceText.setText("");
		sizeText.setText("");
	}
}
