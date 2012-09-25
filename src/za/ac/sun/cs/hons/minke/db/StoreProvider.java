package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class StoreProvider extends Db4oHelper<String> {

	public StoreProvider(Context context) {
		super(context, String.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(String.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(String.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<String> getAll() {
		return super.getAll(new String());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new String());
	}
}
