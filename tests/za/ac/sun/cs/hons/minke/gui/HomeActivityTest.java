package za.ac.sun.cs.hons.minke.gui;

import za.ac.sun.cs.hons.minke.gui.HomeActivity;
import za.ac.sun.cs.hons.minke.gui.utils.TabsAdapter;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.TabHost;

public class HomeActivityTest extends
		ActivityInstrumentationTestCase2<HomeActivity> {
	protected static final int INITIAL_POSITION = 0;
	private static final int TEST_POSITION = 2;
	private HomeActivity mActivity;
	private TabsAdapter mTabManager;
	private TabHost mTabHost;
	private int mPos;
	public HomeActivityTest(String name) {
		super(HomeActivity.class);
		setName(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);

		mActivity = getActivity();
		mTabHost = (TabHost) mActivity.findViewById(android.R.id.tabhost);

		mTabManager = mActivity.mTabManager;

	}
	public void testPreConditions() {
	    assertTrue(mTabHost.getChildCount() > 0);
	    assertTrue(mTabManager.getCurrentTab() != null);
	  }
	public void testTabsUI() {

	    mActivity.runOnUiThread(
	      new Runnable() {
	        public void run() {
	        	mTabHost.requestFocus();
	        	mTabHost.setCurrentTab(INITIAL_POSITION);
	        } // end of run() method definition
	      } // end of anonymous Runnable object instantiation
	    ); // end of invocation of runOnUiThread
	    sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	    for (int i = 1; i <= TEST_POSITION; i++) {
	      sendKeys(KeyEvent.KEYCODE_DPAD_RIGHT);
	    } // end of for loop

	    sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	    mPos = mTabHost.getCurrentTab();
	    
	    assertEquals(mPos,TEST_POSITION);

	  } // end of testSpinnerUI() method definition
}
