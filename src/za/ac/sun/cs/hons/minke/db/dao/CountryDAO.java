package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class CountryDAO extends BaseDAO<Country> {

	public CountryDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DB.COUNTRY_TABLE, DB.COUNTRY_COLUMNS);

	}

	@Override
	protected Country parse(Cursor cursor) {
		Country country = new Country();
		country.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		country.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		return country;
	}

	@Override
	protected ContentValues getContentValues(Country item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Country obj) {
		return obj.getId();
	}
}
