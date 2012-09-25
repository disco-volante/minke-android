package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.location.Country;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class CountryProvider extends Db4oHelper<Country> {

	public CountryProvider(Context context) {
		super(context, Country.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Country.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(Country.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Country.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Country> getAll() {
		return super.getAll(new Country());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Country());
	}
}
