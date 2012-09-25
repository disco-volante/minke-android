package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import android.content.Context;

public class StoreDataTask extends BackgroundTask {

	private Context context;

	public StoreDataTask(Context context) {
		super(1);
		this.context = context;
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(int error_code) {
		StoreDataTask.this.execute();
	}

	@Override
	protected int retrieve(int counter) {
		return EntityUtils.storeData(context);
	}

	@Override
	protected void showProgress(int task) {

	}
}