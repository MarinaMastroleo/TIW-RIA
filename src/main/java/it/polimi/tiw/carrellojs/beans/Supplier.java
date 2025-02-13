package it.polimi.tiw.carrellojs.beans;

import java.util.ArrayList;

public class Supplier {
	private int id_supplier;
	private String name;
	private int num_stars;
	private float amount;
	private int numProdsInCart;
	private float priceProdsInCart;
	private ArrayList<Product> overlaylist;

	public int getId_supplier() {
		return id_supplier;
	}

	public void setId_supplier(int id) {
		this.id_supplier = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumStars() {
		return num_stars;
	}

	public void setNumStars(int num_stars) {
		this.num_stars = num_stars;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public int getNumProdsInCart() {
		return numProdsInCart;
	}

	public void setNumProdsInCart(int num) {
		this.numProdsInCart = num;
	}

	public float getPriceProdsInCart() {
		return priceProdsInCart;
	}

	public void setPriceProdsInCart(float priceProdsInCart) {
		this.priceProdsInCart = priceProdsInCart;
	}

	public void setOverlaylist(ArrayList<Product> overlaylist) {
		this.overlaylist = overlaylist;
	}

	public ArrayList<Product> getOverlaylist() {
		if (overlaylist == null) {
			overlaylist = new ArrayList<>();
		}
		return overlaylist;
	}
}
