package za.ac.sun.cs.hons.minke.entities.location;

import org.json.JSONException;
import org.json.JSONObject;

public class City {
	private long id, provinceId;
	private String name;
	private double lat, lon;
	private Province province;

	public City() {
	}

	public City(long id, long provinceId, String name, double lat, double lon) {
		super();
		this.id = id;
		this.provinceId = provinceId;
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

	public long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(long provinceId) {
		this.provinceId = provinceId;
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


	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	@Override
	public String toString() {
		if(province == null){
			return name;
		}
		return name+", "+province.toString();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "city");
		obj.put("id", id);
		obj.put("name", name);
		obj.put("provinceId", provinceId);
		obj.put("lon", lon);
		obj.put("lat", lat);
		return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = (int) (prime * result + lat);
		result = (int) (prime * result + lon);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result + (int) (provinceId ^ (provinceId >>> 32));
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
		City other = (City) obj;
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
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (provinceId != other.provinceId)
			return false;
		return true;
	}
}
