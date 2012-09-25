package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.Product;
import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class ProductProvider extends Db4oHelper<Product> {

	public ProductProvider(Context context) {
		super(context, Product.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Product.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Product.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Product> getAll() {
		return super.getAll(new Product());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Product());
	}
}
