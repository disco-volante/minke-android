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
import za.ac.sun.cs.hons.minke.utils.ScanUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TIME;
import za.ac.sun.cs.hons.minke.utils.constants.VIEW;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class NewProductFragment extends SherlockFragment {

	private EditText nameText, priceText, sizeText;
	private AutoCompleteTextView brandBox, categoryBox;
	private Spinner unitSpinner;
	protected Brand brand;
	protected Category category;
	private String name;
	private double size;
	private int price;
	private String measure;
	private String categoryName;
	private String brandName;
	private TextErrorWatcher nameWatcher, brandWatcher, categoryWatcher, priceWatcher, sizeWatcher;
	private boolean watchers;


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
		brandBox = (AutoCompleteTextView) v.findViewById(R.id.text_brand);
		final ArrayAdapter<Brand> brands = new ArrayAdapter<Brand>(
				getActivity(), R.layout.listitem_default,
				EntityUtils.getBrands());
		brandBox.setAdapter(brands);
		brandBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				brand = brands.getItem(position);
			}

		});
		brandBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				brandBox.dismissDropDown();
				return false;
			}

		});
		categoryBox = (AutoCompleteTextView) v.findViewById(R.id.text_category);
		final ArrayAdapter<Category> categories = new ArrayAdapter<Category>(
				getActivity(), R.layout.listitem_default,
				EntityUtils.getCategories());
		categoryBox.setAdapter(categories);
		categoryBox.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				category = categories.getItem(position);
			}

		});
		categoryBox.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView view, int id, KeyEvent event) {
				categoryBox.dismissDropDown();
				return false;
			}

		});
		priceText = (EditText) v.findViewById(R.id.text_price);
		sizeText = (EditText) v.findViewById(R.id.text_size);
		unitSpinner = (Spinner) v.findViewById(R.id.spinner_unit);
		SpinnerAdapter units = new ArrayAdapter<String>(getActivity(),
				R.layout.listitem_default, getMeasures());
		unitSpinner.setAdapter(units);
		Button btnCreateProduct = (Button) v
				.findViewById(R.id.btn_create_product);
		btnCreateProduct.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkInput();
			}

		});
	}

	private String[] getMeasures() {
		return new String[] { getString(R.string.mg), getString(R.string.g),
				getString(R.string.kg), getString(R.string.ml),
				getString(R.string.l), getString(R.string.ea) };
	}

	private void checkInput() {
		if (!watchers) {
			watchers = true;
			nameWatcher = new TextErrorWatcher(getActivity(), nameText, false);
			brandWatcher = new TextErrorWatcher(getActivity(), brandBox, false);
			categoryWatcher = new TextErrorWatcher(getActivity(), categoryBox, false);
			priceWatcher = new TextErrorWatcher(getActivity(), priceText, true);
			sizeWatcher = new TextErrorWatcher(getActivity(), sizeText, true);
			nameText.addTextChangedListener(nameWatcher);
			brandBox.addTextChangedListener(brandWatcher);
			categoryBox.addTextChangedListener(categoryWatcher);
			priceText.addTextChangedListener(priceWatcher);
			sizeText.addTextChangedListener(sizeWatcher);
		}
		if (nameText.getError() != null || brandBox.getError() != null
				|| categoryBox.getError() != null
				|| sizeText.getError() != null || priceText.getError() != null) {
			return;
		} else {
			name = nameText.getText().toString();
			size = Double.parseDouble(sizeText.getText().toString());
			price = (int) (Double.parseDouble(priceText.getText()
					.toString()) * 100);
			measure =  unitSpinner
					.getSelectedItem().toString();
			categoryName = categoryBox.getText().toString();
			brandName = brandBox.getText().toString();
			clear();
			createProduct();
		}

	}
	
	private void clear() {
		watchers = false;
		nameText.removeTextChangedListener(nameWatcher);
		brandBox.removeTextChangedListener(brandWatcher);
		categoryBox.removeTextChangedListener(categoryWatcher);
		priceText.removeTextChangedListener(priceWatcher);
		sizeText.removeTextChangedListener(sizeWatcher);
		nameText.setText("");	
		sizeText.setText("");
		priceText.setText("");
		categoryBox.setText("");
		brandBox.setText("");
	}

	public void createProduct() {
		final CreateProductTask task = new CreateProductTask();
		task.execute();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (task.getStatus().equals(AsyncTask.Status.RUNNING)) {
					task.cancel(true);
					Builder dlg = DialogUtils.getErrorDialog(
							NewProductFragment.this.getActivity(),
							ERROR.TIME_OUT);
					dlg.setPositiveButton(getString(R.string.retry),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									createProduct();
									dialog.cancel();
								}
							});
					dlg.show();
				}
			}
		}, TIME.TIMEOUT_15);
	}

	class CreateProductTask extends ProgressTask {

		public CreateProductTask() {
			super(getActivity(), getString(R.string.adding) + "...",
					getString(R.string.adding_product));
		}

		@Override
		protected void success() {
			((HomeActivity) getActivity()).changeTab(VIEW.SCAN,
					BrowseFragment.class.getName());
		}

		@Override
		protected void failure(ERROR error_code) {
			Builder dlg = DialogUtils.getErrorDialog(getActivity(), error_code);
			dlg.setPositiveButton(getString(R.string.retry),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							createProduct();
							dialog.cancel();
						}
					});
			dlg.setNegativeButton(getString(R.string.cancel),
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
			if (RPCUtils.startServer() == ERROR.SERVER) {
				return ERROR.SERVER;
			}
			if (brand != null && category != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brand, category, size, measure, price,
						ScanUtils.getBarCode());
			} else if (brand != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brand, categoryName, size,
						measure, price, ScanUtils.getBarCode());
			} else if (category != null) {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brandName, category, size,
						measure, price, ScanUtils.getBarCode());
			} else {
				return RPCUtils.createBranchProduct(MapUtils.getUserBranch(),
						name, brandName, categoryName, size, measure, price,
						ScanUtils.getBarCode());
			}
		}
	}

}
