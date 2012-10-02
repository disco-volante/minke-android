package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class ProvinceDAO extends BaseDAO<Province> {

	private CountryDAO countryDAO;

	public ProvinceDAO(BaseDBHelper dbHelper, CountryDAO countryDAO) {
		super(dbHelper, DBConstants.PROVINCE_TABLE,
				DBConstants.PROVINCE_COLUMNS);
		this.countryDAO = countryDAO;

	}

	@Override
	protected Province parse(Cursor cursor) {
		Province province = new Province();
		province.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		province.setCountryId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.COUNTRY_ID)));
		province.setName(cursor.getString(cursor
				.getColumnIndex(DBConstants.NAME)));
		province.setCountry(countryDAO.getByCloudID(province.getCountryId()));
		return province;
	}

	@Override
	protected ContentValues getContentValues(Province item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.COUNTRY_ID, item.getCountryId());
		cv.put(DBConstants.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Province obj) {
		return obj.getId();
	}
}
