package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class CityLocationDAO extends BaseDAO<CityLocation> {

	private CityDAO cityDAO;

	public CityLocationDAO(BaseDBHelper dbHelper, CityDAO cityDAO) {
		super(dbHelper, DBConstants.CL_TABLE, DBConstants.CL_COLUMNS);
		this.cityDAO = cityDAO;

	}

	@Override
	protected CityLocation parse(Cursor cursor) {
		CityLocation cl = new CityLocation();
		cl.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		cl.setCityId(cursor.getLong(cursor.getColumnIndex(DBConstants.CITY_ID)));
		cl.setName(cursor.getString(cursor.getColumnIndex(DBConstants.NAME)));
		cl.setLat(cursor.getDouble(cursor.getColumnIndex(DBConstants.LATITUDE)));
		cl.setLon(cursor.getDouble(cursor.getColumnIndex(DBConstants.LONGITUDE)));
		cl.setCity(cityDAO.getByCloudID(cl.getCityId()));
		return cl;
	}

	@Override
	protected ContentValues getContentValues(CityLocation item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.CITY_ID, item.getCityId());
		cv.put(DBConstants.NAME, item.getName());
		cv.put(DBConstants.LATITUDE, item.getLat());
		cv.put(DBConstants.LONGITUDE, item.getLon());
		return cv;
	}

	@Override
	protected long getID(CityLocation obj) {
		return obj.getId();
	}

}
