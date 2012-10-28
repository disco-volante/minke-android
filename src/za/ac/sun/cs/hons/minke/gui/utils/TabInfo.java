package za.ac.sun.cs.hons.minke.gui.utils;

import android.support.v4.app.Fragment;

public class TabInfo {
	private final String tag;
	private String className;
	private Fragment fragment;

	public TabInfo(String _tag, String _className) {
		tag = _tag;
		className = _className;
	}

	public String getTag() {
		return tag;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String _className) {
		className = _className;
	}
}
