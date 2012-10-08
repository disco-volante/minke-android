package za.ac.sun.cs.hons.minke.gui.browse;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.gui.utils.BPListAdapter;
import za.ac.sun.cs.hons.minke.utils.BrowseUtils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;


public class BrowseFragment  extends SherlockFragment{
	BPListAdapter bplistAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_browse, container, false);
		initGUI(v);
		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		bplistAdapter.notifyDataSetChanged();
	}
	private void initGUI(View v) {
		bplistAdapter = new BPListAdapter(getActivity(), BrowseUtils.getBranchProducts());
		ListView bplist = (ListView) v.findViewById(R.id.bp_list);
		bplist.setAdapter(bplistAdapter);
		bplistAdapter.notifyDataSetChanged();

	}
	
}