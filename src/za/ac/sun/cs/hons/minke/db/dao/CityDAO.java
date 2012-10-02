package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class CityDAO extends BaseDAO<City> {

	private ProvinceDAO provinceDAO;

	public CityDAO(BaseDBHelper dbHelper, ProvinceDAO provinceDAO) {
		super(dbHelper, DBConstants.CITY_TABLE, DBConstants.CITY_COLUMNS);
		this.provinceDAO = provinceDAO;

	}

	@Override
	protected City parse(Cursor cursor) {
		City city = new City();
		city.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		city.setProvinceId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.PROVINCE_ID)));
		city.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NAME)));
		city.setLat(cursor.getDouble(cursor
				.getColumnIndex(DBConstants.LATITUDE)));
		city.setLon(cursor.getDouble(cursor
				.getColumnIndex(DBConstants.LONGITUDE)));
		city.setProvince(provinceDAO.getByCloudID(city.getProvinceId()));
		return city;
	}

	@Override
	protected ContentValues getContentValues(City item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.PROVINCE_ID, item.getProvinceId());
		cv.put(DBConstants.NAME, item.getName());
		cv.put(DBConstants.LATITUDE, item.getLat());
		cv.put(DBConstants.LONGITUDE, item.getLon());
		return cv;
	}

	@Override
	protected long getID(City obj) {
		return obj.getId();
	}

}
