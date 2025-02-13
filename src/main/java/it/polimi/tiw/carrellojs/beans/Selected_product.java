package it.polimi.tiw.carrellojs.beans;

public class Selected_product {
	private int id__sel_product;
	private String views_time;
	private int quantity_selected;
	private float unit_price;
	private int id_buyer;

	private int id_product;
	private int id_ranges;
	private String photo;

	public int getId__sel_product() {
		return id__sel_product;
	}

	public void setId__sel_product(int id__sel_product) {
		this.id__sel_product = id__sel_product;
	}

	public String getViews_time() {
		return views_time;
	}

	public void setViews_time(String views_time) {
		this.views_time = views_time;

	}

	public int getQuantity_selected() {
		return quantity_selected;
	}

	public void setQuantity_selected(int quantity) {
		this.quantity_selected = quantity;

	}

	public float getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(float unit_price) {
		this.unit_price = unit_price;
	}

	public int getId_buyer() {
		return id_buyer;
	}

	public void setId_buyer(int id_buyer) {
		this.id_buyer = id_buyer;

	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;

	}

	public int getId_ranges() {
		return id_ranges;
	}

	public void setId_ranges(int id_ranges) {
		this.id_ranges = id_ranges;

	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
