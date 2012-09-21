package za.ac.sun.cs.hons.minke.gui.maps;

import za.ac.sun.cs.hons.minke.utils.Constants;

import com.nutiteq.services.CloudMadeDirections;
import com.nutiteq.services.YourNavigationDirections;

public class RoutingStarter implements Runnable {
	private final NutiteqRouteWaiter nutiteqRouteWaiter;
	private String userId;
	private int routingService;

	public RoutingStarter(final NutiteqRouteWaiter nutiteqRouteWaiter,final String userId, int routingService) {
		this.nutiteqRouteWaiter = nutiteqRouteWaiter;
		this.userId=userId;
		this.routingService = routingService;
	}

	public void run() {
		switch(routingService){
		case Constants.ROUTING_CLOUDMADE:
			new CloudMadeDirections(nutiteqRouteWaiter,nutiteqRouteWaiter.getStartCoordinates(), nutiteqRouteWaiter.getEndCoordinates(), "Car",CloudMadeDirections.ROUTE_TYPE_MODIFIER_SHORTEST ,"222c0ceb31794934a888ed9403a005d8",userId).execute();
			break;
		case Constants.ROUTING_YOURNAVIGATION:
			new YourNavigationDirections(nutiteqRouteWaiter, nutiteqRouteWaiter.getStartCoordinates(),  nutiteqRouteWaiter.getEndCoordinates(), YourNavigationDirections.MOVE_METHOD_CAR, YourNavigationDirections.ROUTE_TYPE_FASTEST).execute();
			break;
			
		}
		
	}
}
