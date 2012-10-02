package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class BrandDAO extends BaseDAO<Brand> {

	public BrandDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DBConstants.BRAND_TABLE, DBConstants.BRAND_COLUMNS);

	}

	@Override
	protected Brand parse(Cursor cursor) {
		Brand brand = new Brand();
		brand.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		brand.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NAME)));
		return brand;
	}

	@Override
	protected ContentValues getContentValues(Brand item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Brand obj) {
		return obj.getId();
	}
}
