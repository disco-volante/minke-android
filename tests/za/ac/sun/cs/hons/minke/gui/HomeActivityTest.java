package za.ac.sun.cs.hons.minke.gui;

import org.junit.Assert;

import za.ac.sun.cs.hons.minke.R;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class HomeActivityTest extends
		ActivityInstrumentationTestCase2<HomeActivity> {
	private Solo solo;

	public HomeActivityTest() {
		super(HomeActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		solo.waitForDialogToClose(30000);
	}

	public void testSettingsClick() throws Exception {
		solo.clickOnActionBarItem(R.id.menu_item_settings);
		solo.assertCurrentActivity("Expected Settings activity",
				"SettingsActivity");

	}

	public void testInfoClick() throws Exception {
		solo.clickOnActionBarItem(R.id.menu_item_info);
		Assert.assertTrue(solo.waitForText(getActivity().getString(R.string.website)));
		Assert.assertTrue(solo.searchText(getActivity().getString(
				R.string.website)));

	}
	
	public void testScanClick() throws Exception{
		solo.clickOnButton(getActivity().getString(R.id.btn_scan));
		solo.clickInList(0);
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}