package za.ac.sun.cs.hons.minke.db.dao;


import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.DatePrice;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class DatePriceDAO extends BaseDAO<DatePrice> {

	public DatePriceDAO(BaseDBHelper dbHelper) {
		super(dbHelper, DB.DP_TABLE, DB.DP_COLUMNS);
	}

	@Override
	protected DatePrice parse(Cursor cursor) {
		DatePrice dp = new DatePrice();
		dp.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		dp.setDate(cursor.getLong(cursor.getColumnIndex(DB.DATE)));
		dp.setBranchProductID(cursor.getLong(cursor
				.getColumnIndex(DB.BRANCH_PRODUCT_ID)));
		dp.setPrice(cursor.getInt(cursor.getColumnIndex(DB.PRICE)));
		return dp;
	}

	@Override
	protected ContentValues getContentValues(DatePrice item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.BRANCH_PRODUCT_ID, item.getBranchProductID());
		cv.put(DB.DATE, item.getDate().getTime());
		cv.put(DB.PRICE, item.getPrice());
		return cv;
	}

	@Override
	protected long getID(DatePrice obj) {
		return obj.getId();
	}

	
}
