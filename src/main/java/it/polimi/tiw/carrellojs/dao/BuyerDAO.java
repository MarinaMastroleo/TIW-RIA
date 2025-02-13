package it.polimi.tiw.carrellojs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Buyer;

public class BuyerDAO {
	private Connection con;

	public BuyerDAO() {
	}

	public BuyerDAO(Connection con) {
		this.con = con;
	}

	public Buyer checkCredentials(String email, String password) throws SQLException {
		String query = "select  * from buyer  where email = ? and password =?";
		try (PreparedStatement pstatement = con.prepareStatement(query);) {
			pstatement.setString(1, email);
			pstatement.setString(2, password);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return null;
				else {
					result.next();
					Buyer buyer = new Buyer();
					buyer.setIdBuyer(result.getInt("id_buyer"));
					buyer.setName(result.getString("name"));
					buyer.setEmail(result.getString("email"));
					buyer.setPassword(result.getString("password"));
					buyer.setSurname(result.getString("surname"));
					buyer.setAddress(result.getString("address"));
					return buyer;
				}
			}
		}
	}
}
