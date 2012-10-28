package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class ProductDAO extends BaseDAO<Product> {

	private BrandDAO brandDAO;

	public ProductDAO(BaseDBHelper dbHelper, BrandDAO brandDAO) {
		super(dbHelper, DB.PRODUCT_TABLE, DB.PRODUCT_COLUMNS);
		this.brandDAO = brandDAO;

	}

	@Override
	protected Product parse(Cursor cursor) {
		Product product = new Product();
		product.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		product.setBrandId(cursor.getLong(cursor.getColumnIndex(DB.BRAND_ID)));
		product.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		product.setSize(cursor.getDouble(cursor.getColumnIndex(DB.SIZE)));
		product.setMeasure(cursor.getString(cursor.getColumnIndex(DB.MEASURE)));
		product.setBrand(brandDAO.getByCloudID(product.getBrandId()));
		return product;
	}

	@Override
	protected ContentValues getContentValues(Product item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.BRAND_ID, item.getBrandId());
		cv.put(DB.NAME, item.getName());
		cv.put(DB.SIZE, item.getSize());
		cv.put(DB.MEASURE, item.getMeasure());
		return cv;
	}

	@Override
	protected long getID(Product obj) {
		return obj.getId();
	}

}
