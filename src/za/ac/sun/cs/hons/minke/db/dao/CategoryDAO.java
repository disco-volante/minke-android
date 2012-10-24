package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.Category;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class CategoryDAO extends BaseDAO<Category> {

	public CategoryDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DB.CATEGORY_TABLE, DB.CATEGORY_COLUMNS);

	}

	@Override
	protected Category parse(Cursor cursor) {
		Category category = new Category();
		category.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		category.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		return category;
	}

	@Override
	protected ContentValues getContentValues(Category item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Category obj) {
		return obj.getId();
	}
}
