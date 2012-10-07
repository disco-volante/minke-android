package za.ac.sun.cs.hons.minke.tasks;

import android.content.Context;
import za.ac.sun.cs.hons.minke.utils.EntityUtils;
import za.ac.sun.cs.hons.minke.utils.RPCUtils;
import za.ac.sun.cs.hons.minke.utils.constants.ERROR;

public class UpdateDataBGTask extends LoadTask {

	public UpdateDataBGTask(Context context) {
		super(context);
	}

	@Override
	protected void success() {
	}

	@Override
	protected void failure(ERROR error_code) {
		UpdateDataBGTask.this.execute();
	}

	@Override
	protected ERROR retrieve() {
		if(!isNetworkAvailable()){
			return ERROR.CLIENT;
		}
		ERROR error = RPCUtils.retrieveAll(context);
		if(error == ERROR.SUCCESS){
			return EntityUtils.loadAll(context);
		}
		return error;
	}
}