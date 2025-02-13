package it.polimi.tiw.carrellojs.dao;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Product;
import it.polimi.tiw.carrellojs.beans.Selected_product;

public class ItemDAO {
	private Connection connection;

	public ItemDAO() {
	}

	public ItemDAO(Connection con) {
		this.connection = con;
	}

	public void InsertItems(ArrayList<Selected_product> list) throws SQLException {
	    String query1 = "select max(id_order) as id_order from orders;";
	    String query2 = "select photo from product where id_product=? ";
	    String query3 = "insert into item(photo, id_order) values (?, ?);";
	    int id;

	    try {
	        connection.setAutoCommit(false);

	        try (PreparedStatement pstatement1 = connection.prepareStatement(query1)) {
	            ResultSet result1 = pstatement1.executeQuery();
	            while (result1.next()) {
	                id = result1.getInt("id_order");
	                for (Selected_product s : list) {
	                    try (PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
	                        pstatement2.setInt(1, s.getId_product());
	                        try (ResultSet result2 = pstatement2.executeQuery()) {
	                            while (result2.next()) {
	                                Product p = new Product();
	                                p.setPhoto(result2.getString("photo"));
	                                try (PreparedStatement pstatement3 = connection.prepareStatement(query3)) {
	                                    pstatement3.setString(1, p.getPhoto());
	                                    pstatement3.setInt(2, id);
	                                    pstatement3.executeUpdate();
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }
	      connection.commit();
	    } catch (SQLException e) {
	        connection.rollback();
	        throw e;
	    } finally {
	        connection.setAutoCommit(true);
	    }
	}

	public ArrayList<String> FindPhotos(int id_order) throws SQLException {
		String query = "select photo from item where id_order=?;";
		ArrayList<String> l = new ArrayList<String>();
		try (PreparedStatement pstatement = connection.prepareStatement(query)) {
			pstatement.setInt(1, id_order);
			ResultSet result = pstatement.executeQuery();
			while (result.next()) {
				String str = result.getString("photo");
				l.add(str);
			}
		}
		return l;
	}
}