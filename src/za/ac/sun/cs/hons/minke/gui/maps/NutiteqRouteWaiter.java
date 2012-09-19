package za.ac.sun.cs.hons.minke.gui.maps;

import za.ac.sun.cs.hons.minke.utils.MapUtils;
import android.content.Context;
import android.content.Intent;

import com.nutiteq.BasicMapComponent;
import com.nutiteq.components.Line;
import com.nutiteq.components.LineStyle;
import com.nutiteq.components.Place;
import com.nutiteq.components.Route;
import com.nutiteq.components.RouteInstruction;
import com.nutiteq.components.WgsPoint;
import com.nutiteq.log.Log;
import com.nutiteq.services.DirectionsService;
import com.nutiteq.services.DirectionsWaiter;
import com.nutiteq.utils.Utils;
import com.nutiteq.wrappers.Image;


/**
 * Listener for routing results. Shows route info as different icons for each turn
 * @author jaak
 */
public class NutiteqRouteWaiter implements DirectionsWaiter {
	public static NutiteqRouteWaiter instance;
	private WgsPoint startCoordinates;
	private WgsPoint endCoordinates;
	private Thread starter;
	private Context context;
	private int routingService;

    private static Image[] icons = {
	    Utils.createImage("/res/drawable/gps_marker.png"),
	    Utils.createImage("/res/drawable/turn1.png"),
	    Utils.createImage("/res/drawable/turn2.png"),
	    Utils.createImage("/res/drawable/turn3.png")};
	
	public NutiteqRouteWaiter(WgsPoint startCoordinates, WgsPoint endCoordinates, String userId, int routingService, Context context) {
		instance = this;
		this.startCoordinates = startCoordinates;
		this.endCoordinates = endCoordinates;	
		this.context = context;
		this.routingService = routingService;
        starter = new Thread(new RoutingStarter(instance, userId, routingService));
	    starter.start();
	}
	
	@Override
	public void networkError() {
		Log.error("Network error has occurred during directions service execution");
	}
	@Override
	public void routeFound(Route route) {
		Log.info("route Found");
		
		// pass route to Application
		 MapUtils.setRoute(route);

		BasicMapComponent map = MapUtils.getMap();
		
		// add route as line to map
		Line routeLine = route.getRouteLine();
		int[] lineColors = {0xFF0000FF, 0xFF00FF00}; // 0 - blue, 1 - green
		
		routeLine.setStyle(new LineStyle(lineColors[routingService],2)); // set non-default style for the route
	
		map.addLine(routeLine);
        System.out.println("line added");

		Log.info("total distance="+route.getRouteSummary().getDistance().toString());

		final RouteInstruction[] instructions = route.getInstructions();
        Place[] instructionPlaces = new Place[instructions.length];

        for (int i = 0; i < instructions.length; i++) {
          instructionPlaces[i] = new Place(i, instructions[i].getInstruction(), icons[instructions[i].getInstructionType()], instructions[i]
              .getPoint());
        }

        // add route keypoints (instructions) to map
        
        map.addPlaces(instructionPlaces);
        MapUtils.setInstrutionPlaces(instructionPlaces);
        // recenter map to start of the route
        // note that YOURS does not have instructions (at least not in current implementation)
        if(instructionPlaces[0] != null){
        	map.setMiddlePoint(instructionPlaces[0].getWgs()); 
        
	        // start details view
	        Intent i = new Intent(context, RouteList.class);
	        context.startActivity(i);
	        System.out.println("RouteList");
        }
	}
	
	@Override
	public void routingErrors(final int errorCodes) {
	    final StringBuffer errors = new StringBuffer("Errors: ");
	    if ((errorCodes & DirectionsService.ERROR_DESTINATION_ADDRESS_NOT_FOUND) != 0) {
	      errors.append("destination not found,");
	    }
	    if ((errorCodes & DirectionsService.ERROR_FROM_ADDRESS_NOT_FOUND) != 0) {
	      errors.append("from not found,");
	    }
	    if ((errorCodes & DirectionsService.ERROR_FROM_AND_DESTINATION_ADDRESS_SAME) != 0) {
	      errors.append("from and destination same,");
	    }
	    if ((errorCodes & DirectionsService.ERROR_ROUTE_NOT_FOUND) != 0) {
	      errors.append("route not found,");
	    }
	    Log.error(errors.toString());
	}
	@Override
	public void routingParsingError(String arg0) {
		Log.error("false");		
	}
	
	public void setDirectionsService(final DirectionsService directions) {
		directions.execute();		
	}

	public void initialize() {
		Log.info("Routewaiter initialize");
	}

	public WgsPoint getStartCoordinates() {
		return startCoordinates;
	}

	public WgsPoint getEndCoordinates() {
		return endCoordinates;
	}

}


