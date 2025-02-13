package it.polimi.tiw.carrellojs.beans;

public class Ranges {
	private int id_range;
	private int num_min_articles;
	private String num_max_articles;
	private float shipping_costs;
	private int id_supplier;

	public int getId_range() {
		return id_range;
	}

	public void setId_range(int id) {
		this.id_range = id;
	}

	public int getNum_min_articles() {
		return num_min_articles;
	}

	public void setNum_min_articles(int num) {
		this.num_min_articles = num;
	}

	public String getNum_max_articles() {
		return num_max_articles;
	}

	public void setNum_max_articles(String num) {
		this.num_max_articles = num;
	}

	public float getShipping_costs() {
		return shipping_costs;
	}

	public void setShipping_costs(float shipping_costs) {
		this.shipping_costs = shipping_costs;
	}

	public int getId_supplier() {
		return id_supplier;
	}

	public void setId_supplier(int id) {
		this.id_supplier = id;
	}
}
