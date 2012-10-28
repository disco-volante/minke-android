package za.ac.sun.cs.hons.minke.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.utils.constants.DB;

public class StoreDAO extends BaseDAO<Store> {

	public StoreDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DB.STORE_TABLE, DB.STORE_COLUMNS);

	}

	@Override
	protected Store parse(Cursor cursor) {
		Store store = new Store();
		store.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		store.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		return store;
	}

	@Override
	protected ContentValues getContentValues(Store item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Store obj) {
		return obj.getId();
	}
}
