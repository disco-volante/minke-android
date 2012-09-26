package za.ac.sun.cs.hons.minke.tasks;

import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;

public class BackgroundUpdateTask extends BackgroundTask {

	public BackgroundUpdateTask() {
		super(5);
	}

	@Override
	protected void success() {
		EntityUtils.setLoaded(true);
	}

	@Override
	protected void failure(int error_code) {
		BackgroundUpdateTask.this.execute();
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