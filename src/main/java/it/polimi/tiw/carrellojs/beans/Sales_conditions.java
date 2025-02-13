package it.polimi.tiw.carrellojs.beans;

public class Sales_conditions {
	private int id_sale;
	private int id_product;
	private int id_supplier;
	private float unit_price;
	private int quantity;

	public int getId_sale() {
		return id_sale;
	}

	public void setId_sale(int id) {
		this.id_sale = id;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
	}

	public int getId_supplier() {
		return id_supplier;
	}

	public void setId_supplier(int id_supplier) {
		this.id_supplier = id_supplier;
	}

	public float getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(float unit_price) {
		this.unit_price = unit_price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
