package it.polimi.tiw.carrellojs.beans;

public class Product {
	private int id_product;
	private String name;
	private String description;
	private String merchandise_category;
	private String photo;
	private int min_unit_price;

	public int getIdProduct() {
		return id_product;
	}

	public void setIdProduct(int id_product) {
		this.id_product = id_product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMerchandise_category() {
		return merchandise_category;
	}

	public void setMerchandise_category(String merchandise_category) {
		this.merchandise_category = merchandise_category;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getMin_unit_price() {
		return min_unit_price;
	}

	public void setMin_unit_price(int price) {
		this.min_unit_price = price;
	}

}
