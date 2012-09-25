package za.ac.sun.cs.hons.minke.db;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.product.DatePrice;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.config.EmbeddedConfiguration;

public class DatePriceProvider extends Db4oHelper<DatePrice> {

	public DatePriceProvider(Context context) {
		super(context, DatePrice.class);
	}

	public EmbeddedConfiguration configure() {
	     EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
	     configuration.common().objectClass(DatePrice.class).cascadeOnUpdate(true);
	     configuration.common().objectClass(DatePrice.class).cascadeOnDelete(true);
	     return configuration;
	}
	@Override
	public ArrayList<DatePrice> getAll() {
		return super.getAll(new DatePrice());
	}

	@Override
	public void deleteAll() {
		super.deleteAll(new DatePrice());
	}
}
