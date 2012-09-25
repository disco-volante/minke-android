package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.Category;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class CategoryProvider extends Db4oHelper<Category> {

	public CategoryProvider(Context context) {
		super(context, Category.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Category.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(Category.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Category.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Category> getAll() {
		return super.getAll(new Category());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Category());
	}
}
