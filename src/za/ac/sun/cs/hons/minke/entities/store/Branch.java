package za.ac.sun.cs.hons.minke.entities.store;

import java.util.ArrayList;

import za.ac.sun.cs.hons.minke.entities.IsEntity;
import za.ac.sun.cs.hons.minke.entities.location.Location;
import za.ac.sun.cs.hons.minke.utils.GPSCoords;

/**
 * DB Entity used to store data about a specific store's branch, e.g. Spar - Die
 * Boord.
 * 
 * @author godfried
 * 
 */

public class Branch extends Location {
	private String store;
	private String city;
	private ArrayList<IsEntity> bps;
	public Branch() {
	}

	public Branch(String name, String store,String city,  GPSCoords coords, ArrayList<IsEntity> branchProducts) {
		super(name, coords);
		setStore(store);
		setCity(city);
		setBranchProducts(branchProducts);
	}


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
			this.city = city;
			
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
			this.store = store;
	}



	@Override
	public String toString() {
		return store + " @ " + getName();
	}

	public ArrayList<IsEntity> getBranchProducts() {
		return bps;
	}

	public void setBranchProducts(ArrayList<IsEntity> bps) {
		this.bps = bps;
	}

}
