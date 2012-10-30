package za.ac.sun.cs.hons.minke.entities.store;

import org.json.JSONException;
import org.json.JSONObject;

import za.ac.sun.cs.hons.minke.entities.location.CityLocation;
import za.ac.sun.cs.hons.minke.utils.MapUtils;
import za.ac.sun.cs.hons.minke.utils.constants.TAGS;
import android.util.Log;

/**
 * DB Entity used to store data about a specific store's branch, e.g. Spar - Die
 * Boord.
 * 
 * @author godfried
 * 
 */

public class Branch implements Comparable<Branch> {
	private long id, storeId, cityLocationId;
	private String name;
	private Store store;
	private CityLocation cityLocation;

	public Branch() {
	}

	public Branch(long id, long storeId, long cityLocationId, String name) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.cityLocationId = cityLocationId;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getCityLocationId() {
		return cityLocationId;
	}

	public void setCityLocationId(long cityLocationId) {
		this.cityLocationId = cityLocationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String string) {
		this.name = string;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public CityLocation getCityLocation() {
		return cityLocation;
	}

	public void setCityLocation(CityLocation cityLocation) {
		this.cityLocation = cityLocation;
	}

	@Override
	public String toString() {
		if (store == null) {
			return name;
		}
		return store.toString() + " @ " + name;
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "branch");
		obj.put("id", id);
		obj.put("name", name);
		obj.put("storeId", storeId);
		obj.put("cityLocationId", cityLocationId);
		return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cityLocation == null) ? 0 : cityLocation.hashCode());
		result = prime * result
				+ (int) (cityLocationId ^ (cityLocationId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((store == null) ? 0 : store.hashCode());
		result = prime * result + (int) (storeId ^ (storeId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Branch other = (Branch) obj;
		if (cityLocation == null) {
			if (other.cityLocation != null)
				return false;
		} else if (!cityLocation.equals(other.cityLocation))
			return false;
		if (cityLocationId != other.cityLocationId)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (store == null) {
			if (other.store != null)
				return false;
		} else if (!store.equals(other.store))
			return false;
		if (storeId != other.storeId)
			return false;
		return true;
	}

	@Override
	public int compareTo(Branch branch) {
		Log.v(TAGS.ENTITY, getName() + " " + branch);
		if (branch == null || branch.getCityLocation() == null) {
			return 1;
		}
		if (cityLocation == null) {
			return -1;
		}
		if (MapUtils.getUserLocation() == null) {
			return 0;
		}
		double dis0 = MapUtils.dist(MapUtils.getUserLat(),
				MapUtils.getUserLon(), getCityLocation().getLat(),
				getCityLocation().getLon());
		double dis1 = MapUtils.dist(MapUtils.getUserLat(),
				MapUtils.getUserLon(), branch.getCityLocation().getLat(),
				branch.getCityLocation().getLon());
		Log.v(TAGS.ENTITY,
				getName() + " distance " + dis0 + "\n vs " + branch.getName()
						+ " distance " + dis1);
		if (dis0 > dis1) {
			return 1;
		} else if (dis0 < dis1) {
			return -1;
		}
		return 0;
	}
}
