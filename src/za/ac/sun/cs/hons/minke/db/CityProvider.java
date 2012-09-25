package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.location.City;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class CityProvider extends Db4oHelper<City> {

	public CityProvider(Context context) {
		super(context, City.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(City.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(City.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(City.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<City> getAll() {
		return super.getAll(new City());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new City());
	}
}
