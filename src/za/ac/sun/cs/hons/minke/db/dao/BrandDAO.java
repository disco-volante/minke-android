package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Brand;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class BrandDAO extends BaseDAO<Brand> {

	public BrandDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DB.BRAND_TABLE, DB.BRAND_COLUMNS);

	}

	@Override
	protected Brand parse(Cursor cursor) {
		Brand brand = new Brand();
		brand.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		brand.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		return brand;
	}

	@Override
	protected ContentValues getContentValues(Brand item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Brand obj) {
		return obj.getId();
	}
}
