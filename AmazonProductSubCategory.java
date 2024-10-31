package amazonproducts;

public class AmazonProductSubCategory {

	private String subCategoryName;
	private AmazonProductCategory category;

	public AmazonProductSubCategory(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public AmazonProductCategory getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategoryName;
	}

	public void setCategory(AmazonProductCategory category) {
		this.category = category;
	}

	public void setSubCategeory(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	@Override
	public String toString() {
		return subCategoryName;

	}
}
