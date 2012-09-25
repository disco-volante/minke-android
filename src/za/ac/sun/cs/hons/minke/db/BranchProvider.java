package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.store.Branch;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class BranchProvider extends Db4oHelper<Branch> {

	public BranchProvider(Context context) {
		super(context, Branch.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(Branch.class).objectField(
	               "item").indexed(true);
	     configuration.common().objectClass(Branch.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(Branch.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<Branch> getAll() {
		return super.getAll(new Branch());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new Branch());
	}
}
