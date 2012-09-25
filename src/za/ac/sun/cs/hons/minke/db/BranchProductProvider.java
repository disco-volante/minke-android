package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.BranchProduct;
import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class BranchProductProvider extends Db4oHelper<BranchProduct> {

	public BranchProductProvider(Context context) {
		super(context, BranchProduct.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(BranchProduct.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(BranchProduct.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<BranchProduct> getAll() {
		return super.getAll(new BranchProduct());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new BranchProduct());
	}
}
