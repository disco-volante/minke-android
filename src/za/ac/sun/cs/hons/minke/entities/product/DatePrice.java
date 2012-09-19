package za.ac.sun.cs.hons.minke.entities.product;

import java.util.Date;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

public class DatePrice extends IsEntity implements Comparable<DatePrice> {
	private Date date;
	private double price;
	private long branchProductID;

	public DatePrice() {
	}

	public DatePrice(Date date, double price, long branchProductID) {
		super();
		setDate(date);
		setPrice(price);
		setBranchProductID(branchProductID);
	}

	public void setDate(Date date) {
		this.date = (Date) date.clone();
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getDate() {
		return (Date) date.clone();
	}
	public double getPrice() {
		return price;
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
		return ((Double) price).compareTo(datePrice.getPrice());
	}

}
