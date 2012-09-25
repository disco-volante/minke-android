package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.location.Location;
import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class LocationProvider extends Db4oHelper<Location> {

	public LocationProvider(Context context) {
		super(context, Location.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Location.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(Location.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Location.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Location> getAll() {
		return super.getAll(new Location());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Location());
	}
}
