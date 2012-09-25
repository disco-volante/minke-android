package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;

public class BackgroundUpdateDataTask extends BackgroundTask {

	public BackgroundUpdateDataTask() {
		super(5);
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(int error_code) {
		BackgroundUpdateDataTask.this.execute();
	}

	@Override
	protected int retrieve(int counter) {
		int error_code = 0;
		if (counter == 0) {
			error_code = RPCUtils.retrieveBrands();
		} else if (counter == 1) {
			error_code = RPCUtils.retrieveProducts();
		} else if (counter == 2) {
			error_code = RPCUtils.retrieveCategories();
		} else if (counter == 3) {
			error_code = RPCUtils.retrieveLocations();
		} else if (counter == 4) {
			error_code = RPCUtils.retrieveBranches(MapUtils.getLocation()
					.getLatitude(), MapUtils.getLocation().getLongitude());
		}
		return error_code;
	}

	@Override
	protected void showProgress(int task) {

	}
}