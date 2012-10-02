package za.ac.sun.cs.hons.minke.entities.product;

public class ProductCategory {
	private long id;
	private long productID;
	private long categoryID;
	private Category category;
	private Product product;

	public ProductCategory(long id ,long productID, long categoryID) {
		super();
		this.setId(id);
		this.productID = productID;
		this.categoryID = categoryID;
	}

	public ProductCategory() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
	}

	public long getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(long categoryID) {
		this.categoryID = categoryID;
	}

	public Category getCategory() {
		return category;
	}

	public Product getProduct() {
		return product;
	}

	public void setCategory(Category category) {
		this.category = 		category;
	}

	public void setProduct(Product product) {
		this.product = 		product;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + (int) (categoryID ^ (categoryID >>> 32));
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + (int) (productID ^ (productID >>> 32));
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
		ProductCategory other = (ProductCategory) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (categoryID != other.categoryID)
			return false;
		if (id != other.id)
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (productID != other.productID)
			return false;
		return true;
	}

}
