package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.location.Province;
import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class ProvinceProvider extends Db4oHelper<Province> {

	public ProvinceProvider(Context context) {
		super(context, Province.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Province.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Province.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Province> getAll() {
		return super.getAll(new Province());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Province());
	}
}
