package za.ac.sun.cs.hons.minke.gui.maps;

import za.ac.sun.cs.hons.minke.R;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nutiteq.components.RouteInstruction;

public class RouteList extends ListActivity {
	
	private Drawable[] icons = new Drawable[4];
	  
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    	// load icons for instructions
	    	Resources res = getResources();
	    	icons[0] = res.getDrawable(R.drawable.gps_marker);
	    	icons[1] = res.getDrawable(R.drawable.turn1);
	    	icons[2] = res.getDrawable(R.drawable.turn2);
	    	icons[3] = res.getDrawable(R.drawable.turn3);

	    	// we assume that Route is populated when this activity starts
	        
	        setListAdapter(new RouteListAdapter(this, MapUtils.getRoute().getInstructions()));
	    }
	 
	    @Override
	    protected void onListItemClick(ListView l, View v, int position, long id) {
		  super.onListItemClick(l, v, position, id);

		  MapUtils.getMap().setMiddlePoint(MapUtils.getRoute().getInstructions()[position].getPoint());
		  MapUtils.getMap().focusOnPlace(MapUtils.getInstructionPlace(position), true);
		  finish();
	    
	    }

		/**
	     * A sample ListAdapter that presents route instructions
	     * 
	     */
	    private class RouteListAdapter extends BaseAdapter {
	        private RouteInstruction[] mRoute;

			public RouteListAdapter(Context context, RouteInstruction[] routeInstructions) {
	            mContext = context;
	            mRoute = routeInstructions;
	        }

	        /**
	         * The number of items in the list is determined by the number of instructions
	         * in our array.
	         * 
	         * @see android.widget.ListAdapter#getCount()
	         */
	        public int getCount() {
	            return mRoute.length;
	        }

	        /**
	         * Since the data comes from an array, just returning the index is
	         * sufficient to get at the data. If we were using a more complex data
	         * structure, we would return whatever object represents one row in the
	         * list.
	         * 
	         * @see android.widget.ListAdapter#getItem(int)
	         */
	        public Object getItem(int position) {
	            return position;
	        }

	        /**
	         * Use the array index as a unique id.
	         * 
	         * @see android.widget.ListAdapter#getItemId(int)
	         */
	        public long getItemId(int position) {
	            return position;
	        }

	        /**
	         * Make a RouteInstructionView to hold each row.
	         * 
	         * @see android.widget.ListAdapter#getView(int, android.view.View,
	         *      android.view.ViewGroup)
	         */
	        public View getView(int position, View convertView, ViewGroup parent) {
	            RouteInstructionView sv;
	            if (convertView == null) {
	                sv = new RouteInstructionView(mContext, mRoute[position].getInstruction(),
	                        icons[mRoute[position].getInstructionType()]);
	            } else {
	                sv = (RouteInstructionView) convertView;
	                sv.setTitle(mRoute[position].getInstructionNumber()+". "+mRoute[position].getInstruction());
	                sv.setIcon(icons[mRoute[position].getInstructionType()]);
	            }

	            return sv;
	        }

	        /**
	         * Remember our context so we can use it when constructing views.
	         */
	        private Context mContext;
	    }
	    /**
	     * We will use a RouteInstructionView to display route instruction. It's just a LinearLayout
	     * with two text fields.
	     *
	     */
	    private class RouteInstructionView extends LinearLayout {
	        private TextView mTitle;
			private ImageView mIcon;

	        public RouteInstructionView(Context context, String title, Drawable icon) {
	            super(context);

	            this.setOrientation(HORIZONTAL);

	            // Here we build the child views in code. They could also have
	            // been specified in an XML file.

	            mIcon = new ImageView(context);
	            mIcon.setImageDrawable(icon);
	            addView(mIcon, new LinearLayout.LayoutParams(
	                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	            mIcon.setPadding(1, 1, 5, 1);
	            mTitle = new TextView(context);
	            mTitle.setText(title);
	            addView(mTitle, new LinearLayout.LayoutParams(
	                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	            
	            // TODO: also distance and duration could be made visible here
	        }

	        /**
	         * Convenience method to set the title of a RouteInstructionView
	         */
	        public void setTitle(String title) {
	            mTitle.setText(title);
	        }

	        /**
	         * Convenience method to set the icon of a RouteInstructionView
	         */
	        public void setIcon(Drawable icon) {
	        	mIcon.setImageDrawable(icon);
	        }

	    }

}
