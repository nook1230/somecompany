package com.somecompany.model.oe;

import java.util.List;

public class Product {
	//////////////////////////////////////////////////
	// Fields
	
	// product_information table
	private int productId = 0;
	private String productName = "";
	private String productDescription = "";
	private short categoryId = 0;
	private short weightClass = 0;
	private String warrantyPeriod = "";
	private int supplierId = 0;
	private String productStatus = "";
	private double listPrice = 0.0;
	private double minPrice = 0.0;
	private String catalogUrl = "";
	
	// product_descriptions table
	private String languageId = "";
	private String translatedName = "";
	private String translatedDescription = "";
	
	// category_tab table
	private String categoryName = "";
	private String categorydescription = "";
	private int parentCategoryId = 0;
	
	// Inventory list
	private List<Inventory> inventories = null;
	
	private long qtySum = 0;
	
	//////////////////////////////////////////////////
	// Constructor
	public Product() { }
	
	public Product(int productId) {
		this.productId = productId;
	}
	
	//////////////////////////////////////////////////
	// Getters and setters

	// 제품 번호
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	// 제품명
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	// 제품 설명
	public String getProductDescription() {
		return productDescription;
	}
	
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	// 카테고리 번호: 이하 category_tab 테이블과 관계된 4개의 필드
	public short getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(short categoryId) {
		this.categoryId = categoryId;
	}
	
	// 카테고리명
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	// 카테고리 설명
	public String getCategorydescription() {
		return categorydescription;
	}

	public void setCategorydescription(String categorydescription) {
		this.categorydescription = categorydescription;
	}
	
	// 상위 카테고리 번호
	public int getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(int parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	// 무게 분류(여기서는 사용되지 않음)
	public short getWeightClass() {
		return weightClass;
	}
	
	public void setWeightClass(short weightClass) {
		this.weightClass = weightClass;
	}
	
	// 보증기간(table 자료형은 interval year)
	// 가공해서 사용해야 한다
	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}
	
	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	
	// 공급자 번호
	public int getSupplierId() {
		return supplierId;
	}
	
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	
	// 제품 상태(문자열로 표시되어 있다)
	// orderable이 일반적으로 주문 가능한 제품
	public String getProductStatus() {
		return productStatus;
	}
	
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}
	
	// 리스트 가격
	public double getListPrice() {
		return listPrice;
	}
	
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	
	// 최소 가격
	public double getMinPrice() {
		return minPrice;
	}
	
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	
	// 카달로그 페이지 url
	public String getCatalogUrl() {
		return catalogUrl;
	}
	
	public void setCatalogUrl(String catalogUrl) {
		this.catalogUrl = catalogUrl;
	}
	
	//////////////////////////////////////////////////
	
	// 설명 언어(예: 영어 - US)
	public String getLanguageId() {
		return languageId;
	}
	
	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}
	
	// 언어에 따라 번역된 이름
	public String getTranslatedName() {
		return translatedName;
	}
	
	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}
	
	// 언어에 따라 번역된 설명
	public String getTranslatedDescription() {
		return translatedDescription;
	}
	
	public void setTranslatedDescription(String translatedDescription) {
		this.translatedDescription = translatedDescription;
	}
	
	// 재고 리스트
	public List<Inventory> getInventories() {
		return inventories;
	}

	public void setInventories(List<Inventory> inventories) {
		this.inventories = inventories;
	}

	public long getQtySum() {
		return qtySum;
	}

	public void setQtySum(long qtySum) {
		this.qtySum = qtySum;
	}
}
