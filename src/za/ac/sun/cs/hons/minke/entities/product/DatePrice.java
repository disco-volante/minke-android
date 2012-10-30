package za.ac.sun.cs.hons.minke.entities.product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class DatePrice implements Comparable<DatePrice> {
	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");
	private Date date;
	private int price;
	private long id, branchProductID;

	public DatePrice() {
	}

	public DatePrice(long id, Date date, int price, long branchProductID) {
		this.setId(id);
		setDate(date);
		setPrice(price);
		setBranchProductID(branchProductID);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = (Date) date.clone();
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Date getDate() {
		return (Date) date.clone();
	}

	public String getFormattedDate() {
		return formatter.format(date);
	}

	public int getPrice() {
		return price;
	}

	public String getFormattedPrice() {
		return "R " + _getFormattedPrice();
	}

	public double getActualPrice() {
		return ((double) price) / 100;
	}

	public String _getFormattedPrice() {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return twoDForm.format(getActualPrice()).replace(',', '.');
	}

	public void setBranchProductID(long branchProductID) {
		this.branchProductID = branchProductID;
	}

	public long getBranchProductID() {
		return branchProductID;
	}

	@Override
	public int compareTo(DatePrice datePrice) {
		if (datePrice == null) {
			return 1;
		} else if (date == null) {
			return -1;
		}
		int cd;
		if ((cd = date.compareTo(datePrice.getDate())) != 0) {
			return cd;
		}
		return ((Integer) price).compareTo(datePrice.getPrice());
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject dp = new JSONObject();
		dp.put("type", "datePrice");
		dp.put("id", id);
		dp.put("date", date.getTime());
		dp.put("price", price);
		dp.put("branchProductID", branchProductID);
		return dp;
	}

	public void setDate(long time) {
		date = new Date(time);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (branchProductID ^ (branchProductID >>> 32));
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((formatter == null) ? 0 : formatter.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + price;
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
		DatePrice other = (DatePrice) obj;
		if (branchProductID != other.branchProductID)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (formatter == null) {
			if (other.formatter != null)
				return false;
		} else if (!formatter.equals(other.formatter))
			return false;
		if (id != other.id)
			return false;
		if (price != other.price)
			return false;
		return true;
	}

}
