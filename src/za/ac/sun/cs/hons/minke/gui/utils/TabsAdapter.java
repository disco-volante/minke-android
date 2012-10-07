package za.ac.sun.cs.hons.minke.gui.utils;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabsAdapter implements TabHost.OnTabChangeListener {
	private final SherlockFragmentActivity mActivity;
	private final TabHost mTabHost;
	private final int mContainerId;
	private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();

	private TabInfo mCurrentTab;
	private Deque<String> tagStack;
	private Deque<String> classStack;

	private boolean backPress;

	static class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabsAdapter(SherlockFragmentActivity activity, TabHost tabHost,
			int containerId) {
		mActivity = activity;
		mTabHost = tabHost;
		mContainerId = containerId;
		mTabHost.setOnTabChangedListener(this);
		tagStack = new LinkedList<String>();
		classStack = new LinkedList<String>();

	}

	public TabInfo getCurrentTab() {
		return mCurrentTab;
	}

	public void addTab(TabHost.TabSpec tabSpec, String className) {
		tabSpec.setContent(new DummyTabFactory(mActivity));
		String tag = tabSpec.getTag();
		TabInfo info = new TabInfo(tag, className);
		info.setFragment(mActivity.getSupportFragmentManager()
				.findFragmentByTag(tag));
		if (info.getFragment() != null && !info.getFragment().isDetached()) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(info.getFragment());
			ft.commit();
		}

		mTabs.put(tag, info);
		mTabHost.addTab(tabSpec);
	}

	public void setBackPress(boolean _backPress) {
		this.backPress = _backPress;
	}

	public boolean isStackEmpty() {
		return tagStack.isEmpty();
	}

	public String popClass() {
		return classStack.pop();
	}

	public String popTag() {
		return tagStack.pop();
	}

	@Override
	public void onTabChanged(String tabId) {
		changeTab(tabId, null);
	}

	public void changeTab(String tabId, String className) {
		TabInfo newTab = mTabs.get(tabId);
		if (mCurrentTab == null || !mCurrentTab.equals(newTab)
				|| className != null) {
			FragmentTransaction ft = mActivity.getSupportFragmentManager()
					.beginTransaction();
			if (mCurrentTab != null) {
				if (mCurrentTab.getFragment() != null) {
					ft.detach(mCurrentTab.getFragment());
				}
			}
			if (mCurrentTab != null && !backPress) {
				tagStack.push(mCurrentTab.getTag());
				classStack.push(mCurrentTab.getClassName());
			}
			if (newTab != null) {
				if (className != null) {
					newTab.setClassName(className);
				}
				if (newTab.getFragment() == null
						|| newTab.getClassName() != null) {
					newTab.setFragment(Fragment.instantiate(mActivity,
							newTab.getClassName(), null));
					ft.add(mContainerId, newTab.getFragment(), newTab.getTag());
				} else {
					ft.attach(newTab.getFragment());
				}
			}
			mCurrentTab = newTab;
			ft.commit();
			mActivity.getSupportFragmentManager().executePendingTransactions();
		}
		backPress = false;

	}

	public void goBack() {
		String newClass = classStack.pop();
		String newTag = tagStack.pop();
		if (newTag.equals(mCurrentTab.getTag())) {
			changeTab(newTag, newClass);
		} else {
			changeTab(newTag, newClass);
			mTabHost.setCurrentTabByTag(newTag);
		}
	}

}