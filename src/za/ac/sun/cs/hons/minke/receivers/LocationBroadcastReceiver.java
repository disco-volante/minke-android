package za.ac.sun.cs.hons.minke.receivers;

import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.constants.Debug;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.littlefluffytoys.littlefluffylocationlibrary.LocationLibraryConstants;

public class LocationBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Debug.ON) {
			Log.d("LocationBroadcastReceiver",
					"onReceive: received location update");
		}
		final LocationInfo locationInfo = (LocationInfo) intent
				.getSerializableExtra(LocationLibraryConstants.LOCATION_BROADCAST_EXTRA_LOCATIONINFO);
		MapUtils.setLocation(locationInfo.lastLat, locationInfo.lastLong);

	}

}
