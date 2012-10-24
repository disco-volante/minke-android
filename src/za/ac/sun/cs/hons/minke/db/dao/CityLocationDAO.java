package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class CityLocationDAO extends BaseDAO<CityLocation> {

	private CityDAO cityDAO;

	public CityLocationDAO(BaseDBHelper dbHelper, CityDAO cityDAO) {
		super(dbHelper, DB.CL_TABLE, DB.CL_COLUMNS);
		this.cityDAO = cityDAO;

	}

	@Override
	protected CityLocation parse(Cursor cursor) {
		CityLocation cl = new CityLocation();
		cl.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		cl.setCityId(cursor.getLong(cursor.getColumnIndex(DB.CITY_ID)));
		cl.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		cl.setLat(cursor.getDouble(cursor.getColumnIndex(DB.LATITUDE)));
		cl.setLon(cursor.getDouble(cursor.getColumnIndex(DB.LONGITUDE)));
		cl.setCity(cityDAO.getByCloudID(cl.getCityId()));
		return cl;
	}

	@Override
	protected ContentValues getContentValues(CityLocation item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.CITY_ID, item.getCityId());
		cv.put(DB.NAME, item.getName());
		cv.put(DB.LATITUDE, item.getLat());
		cv.put(DB.LONGITUDE, item.getLon());
		return cv;
	}

	@Override
	protected long getID(CityLocation obj) {
		return obj.getId();
	}

}
