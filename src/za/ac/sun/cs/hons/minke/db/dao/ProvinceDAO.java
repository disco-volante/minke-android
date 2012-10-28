package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.Province;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class ProvinceDAO extends BaseDAO<Province> {

	private CountryDAO countryDAO;

	public ProvinceDAO(BaseDBHelper dbHelper, CountryDAO countryDAO) {
		super(dbHelper, DB.PROVINCE_TABLE, DB.PROVINCE_COLUMNS);
		this.countryDAO = countryDAO;

	}

	@Override
	protected Province parse(Cursor cursor) {
		Province province = new Province();
		province.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		province.setCountryId(cursor.getLong(cursor
				.getColumnIndex(DB.COUNTRY_ID)));
		province.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		province.setCountry(countryDAO.getByCloudID(province.getCountryId()));
		return province;
	}

	@Override
	protected ContentValues getContentValues(Province item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.COUNTRY_ID, item.getCountryId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Province obj) {
		return obj.getId();
	}
}
