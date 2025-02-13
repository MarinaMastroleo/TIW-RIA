package it.polimi.tiw.carrellojs.beans;

public class Item {
	private int id_item;
	private String name_product;
	private String photo;
	private int id_order;

	public int getId_item() {
		return id_item;
	}

	public void setId_item(int id) {
		this.id_item = id;
	}

	public String getName_product() {
		return name_product;
	}

	public void setName_product(String name_product) {
		this.name_product = name_product;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getIdOrder() {
		return id_order;
	}

	public void setIdOrder(int id_order) {
		this.id_order = id_order;
	}

}
