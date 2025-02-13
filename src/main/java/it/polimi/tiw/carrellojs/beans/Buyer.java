package it.polimi.tiw.carrellojs.beans;

public class Buyer {
	private int id_buyer;
	private String name;
	private String surname;
	private String email;
	private String password;
	private String address;

	public int getIdBuyer() {
		return id_buyer;
	}

	public void setIdBuyer(int id) {
		this.id_buyer = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
