package za.ac.sun.cs.hons.minke.db.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import za.ac.sun.cs.hons.minke.db.helper.BaseDBHelper;
import za.ac.sun.cs.hons.minke.utils.constants.DBConstants;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class BaseDAO<T> {

	protected SQLiteDatabase database;
	protected String table;
	protected String[] columns;

	public BaseDAO(BaseDBHelper dbHelper, String table, String[] columns) {
		this.database = dbHelper.getWritableDatabase();
		this.table = table;
		this.columns = columns;
	}

	public long add(T obj) {
		if (obj == null || getByCloudID(getID(obj)) != null) {
			return -1;
		}
		Log.d(TAGS.DB,
				"adding " + obj + " to " + table + " with "
						+ Arrays.asList(columns));
		return database.insert(table, null, getContentValues(obj));

	}

	public void addAll(List<T> objects) {
		Log.d(TAGS.DB, "adding " + objects);
		if (objects == null) {
			return;
		}
		for (T obj : objects) {
			if (add(obj) == -1) {
				updateByCloudID(obj, getID(obj));
			}
		}
	}

	protected abstract long getID(T obj);

	protected String getSelection(String[] params) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (String s : params) {
			count++;
			sb.append(s);
			if (count < params.length) {
				sb.append(DBConstants.AND_FILTER);
			} else {
				sb.append(DBConstants.FILTER);
			}
		}
		return sb.toString();
	}

	public T getByParameters(String[] params, String[] vals) {
		Cursor cursor = database.query(table, columns, getSelection(params),
				vals, null, null, null);
		cursor.moveToFirst();
		T ret =  parse(cursor);
		cursor.close();
		return ret;
	}

	public ArrayList<T> getAllByParameters(String[] params, String[] vals) {
		ArrayList<T> entries = new ArrayList<T>();
		Cursor cursor = database.query(table, columns, getSelection(params),
				vals, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T entry = parse(cursor);
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		Log.d("DB", entries + " retrieved");
		return entries;
	}

	public T getByID(long id) {
		Cursor cursor = database.query(table, columns, DBConstants.ID_FILTER,
				new String[] { String.valueOf(id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		return parse(cursor);
	}

	public T getByCloudID(long id) {
		Cursor cursor = database.query(table, columns,
				DBConstants.CLOUD_ID_FILTER,
				new String[] { String.valueOf(id) }, null, null, null);
		if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		}
		T ret =  parse(cursor);
		cursor.close();
		return ret;
	}

	public ArrayList<T> getAll() {
		ArrayList<T> entries = new ArrayList<T>();
		Cursor cursor = database.query(table, columns, null, null, null, null,
				null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			T entry = parse(cursor);
			entries.add(entry);
			cursor.moveToNext();
		}
		cursor.close();
		Log.d(TAGS.DB, entries + " retrieved");
		return entries;

	}

	protected abstract T parse(Cursor cursor);

	protected abstract ContentValues getContentValues(T item);

	public boolean update(T _new, long id) {
		if (_new == null) {
			return false;
		}
		return database.update(table, getContentValues(_new),
				DBConstants.ID_FILTER, new String[] { String.valueOf(id) }) > 0;
	}

	public boolean updateByCloudID(T _new, long id) {
		if (_new == null) {
			return false;
		}
		return database.update(table, getContentValues(_new),
				DBConstants.CLOUD_ID_FILTER,
				new String[] { String.valueOf(id) }) > 0;
	}

	public boolean delete(long id) {
		return database.delete(table, DBConstants.ID_FILTER,
				new String[] { String.valueOf(id) }) > 0;
	}

	public void deleteAll() {
		database.delete(table, null, null);
	}

}
