package za.ac.sun.cs.hons.minke.utils.constants;

public class DBConstants {
	public static final String DATABASE_NAME = "minke.db";
	public static final int DATABASE_VERSION = 1;

	public static final String ID = "_id";
	public static final String CLOUD_ID = "_cloud_id";
	public static final String NAME = "_name";
	public static final String PRODUCT_ID = "_product_id";
	public static final String CITY_ID = "_city_id";
	public static final String BRANCH_ID = "_branch_id";
	public static final String DATE_PRICE_ID = "_dp_id";
	public static final String BRANCH_PRODUCT_ID = "_bp_id";
	public static final String PRICE = "_price";
	public static final String DATE = "_date";
	public static final String BRAND_ID = "_brand_id";
	public static final String SIZE = "_size";
	public static final String MEASURE = "_measure";
	public static final String CITY_LOCATION_ID = "_cl_id";
	public static final String STORE_ID = "_store_id";
	public static final String LATITUDE = "_lat";
	public static final String LONGITUDE = "_long";
	public static final String COUNTRY_ID = "_country_id";
	public static final String PROVINCE_ID = "_province_id";
	public static final String CATEGORY_ID = "_category_id";

	public static final String ID_FILTER = ID + " = ?";
	public static final String CLOUD_ID_FILTER = CLOUD_ID + " = ?";
	public static final String AND_FILTER = " = ? AND";
	public static final String FILTER = " = ?";

	public static final String BP_TABLE = "branch_product";
	public static final String BRANCH_TABLE = "branch";
	public static final String DP_TABLE = "date_price";
	public static final String CL_TABLE = "city_location";
	public static final String STORE_TABLE = "store";
	public static final String PRODUCT_TABLE = "product";
	public static final String CATEGORY_TABLE = "category";
	public static final String CITY_TABLE = "city";
	public static final String PROVINCE_TABLE = "province";
	public static final String COUNTRY_TABLE = "country";
	public static final String BRAND_TABLE = "brand";
	public static final String PC_TABLE = "product_category";
	public static final String[] TABLES = new String[] { BP_TABLE,
			BRANCH_TABLE, DP_TABLE, CL_TABLE, STORE_TABLE, PRODUCT_TABLE,
			CATEGORY_TABLE, CITY_TABLE, CITY_TABLE, PROVINCE_TABLE,
			COUNTRY_TABLE, BRAND_TABLE, PC_TABLE };

	public static final String[] BP_COLUMNS = new String[] { ID, CLOUD_ID,
			PRODUCT_ID, BRANCH_ID, DATE_PRICE_ID };
	public static final String[] DP_COLUMNS = new String[] { ID, CLOUD_ID,
			BRANCH_PRODUCT_ID, DATE, PRICE };
	public static final String[] PRODUCT_COLUMNS = new String[] { ID, CLOUD_ID,
			BRAND_ID, NAME, SIZE, MEASURE };
	public static final String[] BRAND_COLUMNS = new String[] { ID, CLOUD_ID,
			NAME };
	public static final String[] CATEGORY_COLUMNS = new String[] { ID,
			CLOUD_ID, NAME };
	public static final String[] BRANCH_COLUMNS = new String[] { ID, CLOUD_ID,
			STORE_ID, CITY_LOCATION_ID, NAME };
	public static final String[] STORE_COLUMNS = new String[] { ID, CLOUD_ID,
			NAME };
	public static final String[] CITY_COLUMNS = new String[] { ID, CLOUD_ID,
			PROVINCE_ID, NAME, LATITUDE, LONGITUDE };
	public static final String[] COUNTRY_COLUMNS = new String[] { ID, CLOUD_ID,
			NAME };
	public static final String[] PROVINCE_COLUMNS = new String[] { ID,
			CLOUD_ID, COUNTRY_ID, NAME };
	public static final String[] CL_COLUMNS = new String[] { ID, CLOUD_ID,
			CITY_ID, NAME, LATITUDE, LONGITUDE };
	public static final String[] PC_COLUMNS = new String[] { ID, CLOUD_ID,
			PRODUCT_ID, CATEGORY_ID };

	public static final String CITY_TABLE_CREATE = "create table " + CITY_TABLE
			+ "(" + ID + " integer primary key autoincrement not null , "
			+ CLOUD_ID + " integer not null, " + PROVINCE_ID
			+ " integer not null, " + NAME + " text, " + LATITUDE
			+ " real not null," + LONGITUDE + " real not null);";
	public static final String PROVINCE_TABLE_CREATE = "create table "
			+ PROVINCE_TABLE + "(" + ID
			+ " integer primary key autoincrement not null , " + CLOUD_ID
			+ " integer not null, " + COUNTRY_ID + " integer not null, " + NAME
			+ " text);";
	public static final String COUNTRY_TABLE_CREATE = "create table "
			+ COUNTRY_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null , " + CLOUD_ID
			+ " integer not null, " + NAME + " text);";
	public static final String BRAND_TABLE_CREATE = "create table "
			+ BRAND_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null , " + CLOUD_ID
			+ " integer not null, " + NAME + " text);";
	public static final String CATEGORY_TABLE_CREATE = "create table "
			+ CATEGORY_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null , " + CLOUD_ID
			+ " integer not null, " + NAME + " text);";
	public static final String STORE_TABLE_CREATE = "create table "
			+ STORE_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null, " + CLOUD_ID
			+ " integer not null, " + NAME + " text);";
	public static final String CL_TABLE_CREATE = "create table " + CL_TABLE
			+ "(" + ID + " integer primary key  autoincrement not null , "
			+ CLOUD_ID + " integer not null, " + CITY_ID
			+ " integer not null, " + NAME + " text, " + LATITUDE
			+ " real not null," + LONGITUDE + " real not null);";
	public static final String DP_TABLE_CREATE = "create table " + DP_TABLE
			+ "(" + ID + " integer primary key  autoincrement not null , "
			+ CLOUD_ID + " integer not null, " + BRANCH_PRODUCT_ID
			+ " integer not null, " + DATE + " integer not null, " + PRICE
			+ " integer not null);";
	public static final String BP_TABLE_CREATE = "create table " + BP_TABLE
			+ "(" + ID + " integer primary key  autoincrement not null , "
			+ CLOUD_ID + " integer not null, " + PRODUCT_ID
			+ " integer not null, " + DATE_PRICE_ID + " integer not null, "
			+ BRANCH_ID + " integer not null);";
	public static final String BRANCH_TABLE_CREATE = "create table "
			+ BRANCH_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null , " + CLOUD_ID
			+ " integer not null, " + STORE_ID + " integer not null, "
			+ CITY_LOCATION_ID + " integer not null, " + NAME
			+ " text not null);";
	public static final String PRODUCT_TABLE_CREATE = "create table "
			+ PRODUCT_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null, " + CLOUD_ID
			+ " integer not null, " + BRAND_ID + " integer not null, " + NAME
			+ " text not null," + SIZE + " real not null," + MEASURE
			+ " text not null);";
	public static final String PRODUCT_CATEGORY_TABLE_CREATE = "create table "
			+ PC_TABLE + "(" + ID
			+ " integer primary key  autoincrement not null, " + CLOUD_ID
			+ " integer not null, " + PRODUCT_ID + " integer not null, "
			+ CATEGORY_ID + " integer not null);";
	public static final String[] CREATORS = new String[] { CITY_TABLE_CREATE,
			PROVINCE_TABLE_CREATE, COUNTRY_TABLE_CREATE, BRAND_TABLE_CREATE,
			CATEGORY_TABLE_CREATE, STORE_TABLE_CREATE, CL_TABLE_CREATE,
			DP_TABLE_CREATE, BP_TABLE_CREATE, BRANCH_TABLE_CREATE,
			PRODUCT_TABLE_CREATE, PRODUCT_CATEGORY_TABLE_CREATE };

}
