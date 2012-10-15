package za.ac.sun.cs.hons.minke.tasks;

import android.content.Context;
import android.util.Log;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;

public class UpdateDataBGTask extends LoadTask {

	public UpdateDataBGTask(Context context) {
		super(context);
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(ERROR error_code) {
		if (Debug.ON) {
			Log.v(TAGS.HTTP, "ERROR");
		}
	}

	@Override
	protected ERROR retrieve() {
		if (!isNetworkAvailable()) {
			return ERROR.CLIENT;
		}
		if (RPCUtils.startServer() == ERROR.SERVER) {
			return ERROR.SERVER;
		}
		ERROR error = RPCUtils.retrieveAll(context);
		if (!error.equals(ERROR.SUCCESS)) {
			error = RPCUtils.retrieveAll(context);
		}
		if (error == ERROR.SUCCESS) {
			return EntityUtils.loadAll(context);
		}
		return error;
	}
}