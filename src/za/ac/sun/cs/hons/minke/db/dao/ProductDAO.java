package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Product;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class ProductDAO extends BaseDAO<Product> {

	private BrandDAO brandDAO;

	public ProductDAO(BaseDBHelper dbHelper, BrandDAO brandDAO) {
		super(dbHelper, DBConstants.PRODUCT_TABLE, DBConstants.PRODUCT_COLUMNS);
		this.brandDAO =brandDAO;

	}

	@Override
	protected Product parse(Cursor cursor) {
		Product product = new Product();
		product.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		product.setBrandId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.BRAND_ID)));
		product.setName(cursor.getString(cursor
				.getColumnIndex(DBConstants.NAME)));
		product.setSize(cursor.getDouble(cursor
				.getColumnIndex(DBConstants.SIZE)));
		product.setMeasure(cursor.getString(cursor
				.getColumnIndex(DBConstants.MEASURE)));
		product.setBrand(brandDAO.getByCloudID(product.getBrandId()));
		return product;
	}

	@Override
	protected ContentValues getContentValues(Product item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		System.out.println("id"+item.getId());
		cv.put(DBConstants.BRAND_ID, item.getBrandId());
		cv.put(DBConstants.NAME, item.getName());
		cv.put(DBConstants.SIZE, item.getSize());
		cv.put(DBConstants.MEASURE, item.getMeasure());
		return cv;
	}

	@Override
	protected long getID(Product obj) {
		return obj.getId();
	}


}
