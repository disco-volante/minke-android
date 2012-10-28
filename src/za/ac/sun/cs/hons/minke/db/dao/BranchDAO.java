package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.store.Branch;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class BranchDAO extends BaseDAO<Branch> {

	private StoreDAO storeDAO;
	private CityLocationDAO clDAO;

	public BranchDAO(BaseDBHelper dbHelper, StoreDAO storeDAO,
			CityLocationDAO clDAO) {
		super(dbHelper, DB.BRANCH_TABLE, DB.BRANCH_COLUMNS);
		this.storeDAO = storeDAO;
		this.clDAO = clDAO;

	}

	@Override
	protected Branch parse(Cursor cursor) {
		Branch branch = new Branch();
		branch.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		branch.setCityLocationId(cursor.getLong(cursor
				.getColumnIndex(DB.CITY_LOCATION_ID)));
		branch.setStoreId(cursor.getLong(cursor.getColumnIndex(DB.STORE_ID)));
		branch.setName(cursor.getString(cursor.getColumnIndex(DB.NAME)));
		branch.setStore(storeDAO.getByCloudID(branch.getStoreId()));
		branch.setCityLocation(clDAO.getByCloudID(branch.getCityLocationId()));
		return branch;
	}

	@Override
	protected ContentValues getContentValues(Branch item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.CITY_LOCATION_ID, item.getCityLocationId());
		cv.put(DB.STORE_ID, item.getStoreId());
		cv.put(DB.NAME, item.getName());
		return cv;
	}

	@Override
	protected long getID(Branch obj) {
		return obj.getId();
	}

}
