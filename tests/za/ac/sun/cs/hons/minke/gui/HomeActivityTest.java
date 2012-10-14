package za.ac.sun.cs.hons.minke.gui;

import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.TabsAdapter;
import android.test.ActivityInstrumentationTestCase2;

public class HomeActivityTest extends
		ActivityInstrumentationTestCase2<HomeActivity> {
	private HomeActivity mActivity;
	private TabsAdapter tabs;

	public HomeActivityTest(Class<HomeActivity> activityClass) {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		mActivity = getActivity();
		tabs = mActivity.mTabManager;

	}
	public void testSpinnerUI() {

	    mActivity.runOnUiThread(
	      new Runnable() {
	        public void run() {
	          
	        } // end of run() method definition
	      } // end of anonymous Runnable object instantiation
	    );
	}
}
