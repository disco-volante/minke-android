package za.ac.sun.cs.hons.minke.gui;

import org.junit.Assert;

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
	}

	public void testPreferenceIsSaved() throws Exception {
		//solo.se
		//solo.sendKey(Solo.MENU);
		solo.clickOnText("More");
		solo.clickOnText("Preferences");
		solo.clickOnText("Edit File Extensions");
		Assert.assertTrue(solo.searchText("rtf"));

		solo.clickOnText("txt");
		solo.clearEditText(2);
		solo.enterText(2, "robotium");
		solo.clickOnButton("Save");
		solo.goBack();
		solo.clickOnText("Edit File Extensions");
		Assert.assertTrue(solo.searchText("application/robotium"));

	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}