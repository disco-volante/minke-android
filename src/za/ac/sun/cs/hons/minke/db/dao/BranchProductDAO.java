package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;

import android.content.ContentValues;
import android.database.Cursor;

public class BranchProductDAO extends BaseDAO<BranchProduct> {

	private DatePriceDAO datePriceDAO;
	private ProductDAO productDAO;
	private BranchDAO branchDAO;

	public BranchProductDAO(BaseDBHelper dbHelper, DatePriceDAO datePriceDAO,
			ProductDAO productDAO, BranchDAO branchDAO) {
		super(dbHelper, DBConstants.BP_TABLE, DBConstants.BP_COLUMNS);
		this.datePriceDAO = datePriceDAO;
		this.productDAO = productDAO;
		this.branchDAO = branchDAO;
	}

	@Override
	protected BranchProduct parse(Cursor cursor) {
		BranchProduct bp = new BranchProduct();
		bp.setId(cursor.getLong(cursor.getColumnIndex(DBConstants.CLOUD_ID)));
		bp.setBranchId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.BRANCH_ID)));
		bp.setProductId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.PRODUCT_ID)));
		bp.setDatePriceId(cursor.getLong(cursor
				.getColumnIndex(DBConstants.DATE_PRICE_ID)));
		bp.setDatePrice(datePriceDAO.getByCloudID(bp.getDatePriceId()));
		bp.setBranch(branchDAO.getByCloudID(bp.getBranchId()));
		bp.setProduct(productDAO.getByCloudID(bp.getProductId()));
		return bp;
	}

	@Override
	protected ContentValues getContentValues(BranchProduct item) {
		ContentValues cv = new ContentValues();
		cv.put(DBConstants.CLOUD_ID, item.getId());
		cv.put(DBConstants.BRANCH_ID, item.getBranchId());
		cv.put(DBConstants.PRODUCT_ID, item.getProductId());
		cv.put(DBConstants.DATE_PRICE_ID, item.getDatePriceId());
		return cv;
	}

	@Override
	protected long getID(BranchProduct obj) {
		return obj.getId();
	}

}
