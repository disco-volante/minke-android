package za.ac.sun.cs.hons.minke.entities.product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import za.ac.sun.cs.hons.minke.entities.IsEntity;

public class BranchProduct extends IsEntity {
	private String product, brand, branch, measure;
	Date date;
	private double price, size;
	private ArrayList<IsEntity> dps;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

	public BranchProduct() {
	}

	public BranchProduct(String product, String brand, String branch,
			Date date, double price, double size, String measure, ArrayList<IsEntity> dps) {
		super();
		setProduct(product);
		setBrand(brand);
		setBranch(branch);
		setDate(date);
		setPrice(price);
		this.size = size;
		this.measure = measure;
		this.setDatePrices(dps);
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;

	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;

	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getDate() {
		return formatter.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return brand + " " + product;
	}

	public double getSize() {
		return size;
	}

	public String getMeasurement() {
		return measure;
	}

	public ArrayList<IsEntity> getDatePrices() {
		return dps;
	}

	public void setDatePrices(ArrayList<IsEntity> dps) {
		this.dps = dps;
	}

}
