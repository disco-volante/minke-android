package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;
import java.util.List;

import za.ac.sun.cs.hons.minke.utils.Constants;
import android.content.Context;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public abstract class Db4oHelper<T> {


	private EmbeddedObjectContainer db;

	public Db4oHelper(Context context, Class<T> clazz) {
		try {
			if (db == null || db.ext().isClosed()) {
				db = Db4oEmbedded
						.openFile(configure(), db4oDBFullPath(context));
			}
		} catch (Exception ie) {
			Log.e(Db4oHelper.class.getName(), ie.toString());
		}

	}

	public abstract EmbeddedConfiguration configure();

	private String db4oDBFullPath(Context ctx) {
		return ctx.getDir("data", Context.MODE_PRIVATE) + "/"
				+ Constants.DATABASE_NAME;
	}

	public void close() {
		if (this.db != null) {
			this.db.close();
		}
	}

	public void emptyDb() {
		ObjectSet<T> result = db.queryByExample(new Object());
		while (result.hasNext()) {
			db.delete(result.next());
		}
	}

	public void add(T obj) {
		db.store(obj);
		db.commit();
		Log.d("DB", obj + " stored");

	}

	public void addAll(List<T> objects) {
		for (T obj : objects) {
			db.store(obj);
		}
		db.commit();
		Log.d("DB", objects + " stored");

	}

	public T get(T obj) {
		ObjectSet<T> result = db.queryByExample(obj);
		if (result.hasNext()) {
			return (T) result.next();
		}
		return null;

	}

	protected ArrayList<T> getAll(T obj) {
		ArrayList<T> list = new ArrayList<T>();
		ObjectSet<T> result = db.queryByExample(obj);
		list.addAll(result);
		return list;
	}

	public abstract ArrayList<T> getAll();

	public boolean update(T old, T _new) {
		T found = null;
		ObjectSet<T> result = db.queryByExample(old);
		if (result.hasNext()) {
			found = result.next();
			found = _new;
			db.store(found);
			db.commit();
			return true;
		}
		return false;

	}

	public boolean delete(T obj) {
		T found = null;
		ObjectSet<T> result = db.queryByExample(obj);
		if (result.hasNext()) {
			found = result.next();
			db.delete(found);
			return true;
		} else {
			return false;
		}
	}

	public void deleteAll(T obj) {
		try {
			ObjectSet<T> items = db.queryByExample(obj);
			for (T item : items) {
				db.delete(item);
			}
			db.commit();
		} catch (Exception e) {
			Log.e(Db4oHelper.class.getName(), e.toString());
		} finally {
			if (db != null) {
				db.close();
			}
		}
	}

	public abstract void deleteAll();

}
