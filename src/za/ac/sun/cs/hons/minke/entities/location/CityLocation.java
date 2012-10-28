package za.ac.sun.cs.hons.minke.entities.location;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

public class CityLocation {
	private long id, cityId;
	private String name;
	private double lat, lon;
	private City city;

	public CityLocation() {
	}

	public CityLocation(long id, long cityId, String name, double lat,
			double lon) {
		this.id = id;
		this.setCityId(cityId);
		this.name = name;
		this.lat = lat;
		this.lon = lon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public String toString() {
		if (city == null) {
			return name;
		}
		return name + ", " + city.toString();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "cityLocation");
		obj.put("id", id);
		obj.put("name", name);
		obj.put("cityId", cityId);
		obj.put("lon", lon);
		obj.put("lat", lat);
		return obj;
	}

	public GeoPoint getGeoPoint() {
		return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + (int) (cityId ^ (cityId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = (int) (prime * result + lat);
		result = (int) (prime * result + lon);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CityLocation other = (CityLocation) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (cityId != other.cityId)
			return false;
		if (id != other.id)
			return false;
		if (lat != other.lat)
			return false;
		if (lon != other.lon)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
