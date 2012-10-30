package za.ac.sun.cs.hons.minke.db.dao;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import za.ac.sun.cs.hons.minke.utils.constants.DB;
import android.content.ContentValues;
import android.database.Cursor;

public class BranchProductDAO extends BaseDAO<BranchProduct> {

	private DatePriceDAO datePriceDAO;
	private ProductDAO productDAO;
	private BranchDAO branchDAO;

	public BranchProductDAO(BaseDBHelper dbHelper, DatePriceDAO datePriceDAO,
			ProductDAO productDAO, BranchDAO branchDAO) {
		super(dbHelper, DB.BP_TABLE, DB.BP_COLUMNS);
		this.datePriceDAO = datePriceDAO;
		this.productDAO = productDAO;
		this.branchDAO = branchDAO;
	}

	@Override
	protected BranchProduct parse(Cursor cursor) {
		BranchProduct bp = new BranchProduct();
		bp.setId(cursor.getLong(cursor.getColumnIndex(DB.CLOUD_ID)));
		bp.setBranchId(cursor.getLong(cursor.getColumnIndex(DB.BRANCH_ID)));
		bp.setProductId(cursor.getLong(cursor.getColumnIndex(DB.PRODUCT_ID)));
		bp.setDatePriceId(cursor.getLong(cursor
				.getColumnIndex(DB.DATE_PRICE_ID)));
		bp.setDatePrice(datePriceDAO.getByCloudID(bp.getDatePriceId()));
		bp.setBranch(branchDAO.getByCloudID(bp.getBranchId()));
		bp.setProduct(productDAO.getByCloudID(bp.getProductId()));
		return bp;
	}

	@Override
	protected ContentValues getContentValues(BranchProduct item) {
		ContentValues cv = new ContentValues();
		cv.put(DB.CLOUD_ID, item.getId());
		cv.put(DB.BRANCH_ID, item.getBranchId());
		cv.put(DB.PRODUCT_ID, item.getProductId());
		cv.put(DB.DATE_PRICE_ID, item.getDatePriceId());
		return cv;
	}

	@Override
	protected long getID(BranchProduct obj) {
		return obj.getId();
	}

}
