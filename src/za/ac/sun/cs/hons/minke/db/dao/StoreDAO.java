package za.ac.sun.cs.hons.minke.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.store.Store;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;

public class StoreDAO extends BaseDAO<Store> {

	public StoreDAO(BaseDBHelper dbHelper) {
		super( dbHelper, DBConstants.STORE_TABLE, DBConstants.STORE_COLUMNS);

	}

	@Override
	protected Store parse(Cursor cursor) {
		Store store = new Store();
		store.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		store.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NAME)));
		return store;
	}

	@Override
	protected ContentValues getContentValues(Store item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Store obj) {
		return obj.getId();
	}
}
