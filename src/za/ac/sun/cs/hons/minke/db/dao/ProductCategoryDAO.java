package za.ac.sun.cs.hons.minke.db.dao;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.entities.product.ProductCategory;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class ProductCategoryDAO extends BaseDAO<ProductCategory>{

	

	private ProductDAO productDAO;
	private CategoryDAO categoryDAO;

	public ProductCategoryDAO(BaseDBHelper dbHelper, ProductDAO productDAO, CategoryDAO categoryDAO) {
		super(dbHelper, DBConstants.PC_TABLE, DBConstants.PC_COLUMNS);
		this.productDAO = productDAO;
		this.categoryDAO = categoryDAO;
	}


	@Override
	protected ContentValues getContentValues(ProductCategory pc) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.PRODUCT_ID, pc.getProductID());
		cv.put(DBConstants.CATEGORY_ID, pc.getCategoryID());
		cv.put(DBConstants.CLOUD_ID, pc.getId());
		return cv;
	}


	public ArrayList<ProductCategory> getAllByPID(long pid) {
		return super.getAllByParameters(new String[] { DBConstants.PRODUCT_ID }, new String[] { String.valueOf(pid) });

	}

	public ArrayList<ProductCategory> getAllByCID(long cid) {
		return super.getAllByParameters(new String[] { DBConstants.CATEGORY_ID }, new String[] { String.valueOf(cid) });

	}

	@Override
	protected long getID(ProductCategory obj) {
		return obj.getId();
	}

	@Override
	protected ProductCategory parse(Cursor cursor) {
		ProductCategory pc = new ProductCategory();
		pc.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		pc.setProductID(cursor.getLong(cursor.getColumnIndex(DBConstants.PRODUCT_ID)));
		pc.setCategoryID(cursor.getLong(cursor.getColumnIndex(DBConstants.CATEGORY_ID)));
		pc.setCategory(categoryDAO.getByCloudID(pc.getCategoryID()));
		pc.setProduct(productDAO.getByCloudID(pc.getProductID()));
		return pc;
	}


	public ArrayList<Product> getProducts(long id) {
		ArrayList<Product> entries = new ArrayList<Product>();
		Cursor cursor = database.query(table, columns, getSelection(new String[]{DBConstants.CATEGORY_ID}),
				new String[]{String.valueOf(id)}, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ProductCategory entry = parse(cursor);
			entries.add(entry.getProduct());
			cursor.moveToNext();
		}
		cursor.close();
		Log.d("DB", entries + " retrieved");
		return entries;
	}
	
	public ArrayList<Category> getCategories(long id) {
		ArrayList<Category> entries = new ArrayList<Category>();
		Cursor cursor = database.query(table, columns, getSelection(new String[]{DBConstants.PRODUCT_ID}),
				new String[]{String.valueOf(id)}, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ProductCategory entry = parse(cursor);
			entries.add(entry.getCategory());
			cursor.moveToNext();
		}
		cursor.close();
		Log.d("DB", entries + " retrieved");
		return entries;
	}

	
}
