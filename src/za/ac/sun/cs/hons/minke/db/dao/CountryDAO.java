package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.Country;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class CountryDAO extends BaseDAO<Country> {

	public CountryDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DBConstants.COUNTRY_TABLE, DBConstants.COUNTRY_COLUMNS);

	}

	@Override
	protected Country parse(Cursor cursor) {
		Country country = new Country();
		country.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		country.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NAME)));
		return country;
	}

	@Override
	protected ContentValues getContentValues(Country item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Country obj) {
		return obj.getId();
	}
}
