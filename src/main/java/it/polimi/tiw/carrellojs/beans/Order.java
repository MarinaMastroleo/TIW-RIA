package it.polimi.tiw.carrellojs.beans;

import java.util.ArrayList;

public class Order {
	private int id_order;
	private String name_supplier;
	private float price_tot_products;
	private float delivery_costs;
	private String delivery_date;
	private String address_buyer;
	private int num_ordered_articles;
	private int id_buyer;
	private ArrayList<Selected_product> products;
	private ArrayList<String> productsphotos;

	public int getId_order() {
		return id_order;
	}

	public void setId_order(int id) {
		this.id_order = id;
	}

	public String getName_supplier() {
		return name_supplier;
	}

	public void setName_supplier(String name) {
		this.name_supplier = name;
	}

	public float getPrice_tot_products() {
		return price_tot_products;
	}

	public void setPrice_tot_products(float price) {
		this.price_tot_products = price;
	}

	public float getDelivery_costs() {
		return delivery_costs;
	}

	public void setDelivery_costs(float delivery_costs) {
		this.delivery_costs = delivery_costs;
	}

	public String getDelivery_date() {
		return delivery_date;
	}

	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}

	public String getAddress_buyer() {
		return address_buyer;
	}

	public void setAddress_buyer(String address_buyer) {
		this.address_buyer = address_buyer;
	}

	public int getNum_ordered_articles() {
		return num_ordered_articles;
	}

	public void setNum_ordered_articles(int num) {
		this.num_ordered_articles = num;
	}

	public int getId_buyer() {
		return id_buyer;
	}

	public void setId_buyer(int id) {
		this.id_buyer = id;
	}

	public void setProducts(ArrayList<Selected_product> products) {
		this.products = products;
	}

	public ArrayList<Selected_product> getProducts() {
		if (products == null) {
			products = new ArrayList<>();
		}
		return products;
	}

	public void setProductsphotos(ArrayList<String> productsphotos) {
		if (this.productsphotos == null) {
			this.productsphotos = new ArrayList<String>();
		}
		this.productsphotos.addAll(productsphotos);
	}

	public ArrayList<String> getProductsphotos() {
		if (products == null) {
			products = new ArrayList<>();
		}
		return productsphotos;
	}

}
