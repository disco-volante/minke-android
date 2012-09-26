package za.ac.sun.cs.hons.minke.receivers;

import za.ac.sun.cs.hons.minke.services.UpdateService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UpdateServiceReceiver extends BroadcastReceiver{
	@Override
	  public void onReceive(Context context, Intent intent) {
		Log.d("UPDATER", " Updater started");
	    Intent service = new Intent(context, UpdateService.class);
	    context.startService(service);

	  }
}
