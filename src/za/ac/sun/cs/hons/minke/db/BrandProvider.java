package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.Brand;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class BrandProvider extends Db4oHelper<Brand> {

	public BrandProvider(Context context) {
		super(context, Brand.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Brand.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(Brand.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Brand.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Brand> getAll() {
		return super.getAll(new Brand());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Brand());
	}
}
