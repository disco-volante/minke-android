package za.ac.sun.cs.hons.minke.db.dao;


import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import android.content.ContentValues;
import android.database.Cursor;

public class DatePriceDAO extends BaseDAO<DatePrice> {

	public DatePriceDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DBConstants.DP_TABLE, DBConstants.DP_COLUMNS);
	}

	@Override
	protected DatePrice parse(Cursor cursor) {
		DatePrice dp = new DatePrice();
		dp.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		dp.setDate(cursor.getLong(cursor.getColumnIndex(DBConstants.DATE)));
		dp.setBranchProductID(cursor.getLong(cursor
				.getColumnIndex(DBConstants.BRANCH_PRODUCT_ID)));
		dp.setPrice(cursor.getInt(cursor.getColumnIndex(DBConstants.PRICE)));
		return dp;
	}

	@Override
	protected ContentValues getContentValues(DatePrice item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.BRANCH_PRODUCT_ID, item.getBranchProductID());
		cv.put(DBConstants.DATE, item.getDate().getTime());
		cv.put(DBConstants.PRICE, item.getPrice());
		return cv;
	}

	@Override
	protected long getID(DatePrice obj) {
		return obj.getId();
	}

	
}
