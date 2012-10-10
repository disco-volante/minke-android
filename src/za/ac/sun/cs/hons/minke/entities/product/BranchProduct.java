package za.ac.sun.cs.hons.minke.entities.product;

import org.json.JSONException;
import org.json.JSONObject;

import za.ac.sun.cs.hons.minke.entities.store.Branch;

public class BranchProduct {
	private long id, productId, branchId, datePriceId;
	private Product product;
	private DatePrice datePrice;
	private Branch branch;
	private int quantity = 1;

	public BranchProduct(long id, long productId, long branchId,
			long datePriceId) {
		super();
		this.id = id;
		this.productId = productId;
		this.branchId = branchId;
		this.datePriceId = datePriceId;
	}

	public BranchProduct() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getDatePriceId() {
		return datePriceId;
	}

	public void setDatePriceId(long datePriceId) {
		this.datePriceId = datePriceId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public DatePrice getDatePrice() {
		return datePrice;
	}

	public void setDatePrice(DatePrice datePrice) {
		this.datePrice = datePrice;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	@Override
	public String toString() {
		if (product == null) {
			return String.valueOf(id);
		}
		return product.toString();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("type", "branchProduct");
		obj.put("id", id);
		obj.put("productId", productId);
		obj.put("branchId", branchId);
		obj.put("datePriceId", datePriceId);
		return obj;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + (int) (branchId ^ (branchId >>> 32));
		result = prime * result
				+ ((datePrice == null) ? 0 : datePrice.hashCode());
		result = prime * result + (int) (datePriceId ^ (datePriceId >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (int) (productId ^ (productId >>> 32));
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
		BranchProduct other = (BranchProduct) obj;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (branchId != other.branchId)
			return false;
		if (datePrice == null) {
			if (other.datePrice != null)
				return false;
		} else if (!datePrice.equals(other.datePrice))
			return false;
		if (datePriceId != other.datePriceId)
			return false;
		if (id != other.id)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productId != other.productId)
			return false;
		return true;
	}

	public void setQuantity(int _quantity) {
		this.quantity  = _quantity;
	}

	public int getQuantity() {
		return quantity;
	}
}
