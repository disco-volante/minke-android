package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.City;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class CityDAO extends BaseDAO<City> {

	private ProvinceDAO provinceDAO;

	public CityDAO(BaseDBHelper dbHelper, ProvinceDAO provinceDAO) {
		super(dbHelper, DB.CITY_TABLE, DB.CITY_COLUMNS);
		this.provinceDAO = provinceDAO;

	}

	@Override
	protected City parse(Cursor cursor) {
		City city = new City();
		city.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		city.setProvinceId(cursor.getLong(cursor.getColumnIndex(DB.PROVINCE_ID)));
		city.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		city.setLat(cursor.getDouble(cursor.getColumnIndex(DB.LATITUDE)));
		city.setLon(cursor.getDouble(cursor.getColumnIndex(DB.LONGITUDE)));
		city.setProvince(provinceDAO.getByCloudID(city.getProvinceId()));
		return city;
	}

	@Override
	protected ContentValues getContentValues(City item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.PROVINCE_ID, item.getProvinceId());
		cv.put(DB.NAME, item.getName());
		cv.put(DB.LATITUDE, item.getLat());
		cv.put(DB.LONGITUDE, item.getLon());
		return cv;
	}

	@Override
	protected long getID(City obj) {
		return obj.getId();
	}

}
