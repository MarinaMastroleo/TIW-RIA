package it.polimi.tiw.carrellojs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Selected_product;

public class Selected_productDAO {
	private Connection connection;

	public Selected_productDAO() {
	}

	public Selected_productDAO(Connection con) {
		this.connection = con;
	}

	// viene chiamata nella servlet SearchDetails.java
	// infatti, prima di trovare i dettagli del prodotto selezionato, aggiungo
	// nel DB il selectd_product
	public Selected_product AddSelProduct(int id_buyer, int id_product, String photo) throws SQLException {
		String query = "insert into selected_product (views_time,id_buyer,id_product) values (CURRENT_TIMESTAMP(),?,?) ;";

		try {
			connection.setAutoCommit(false);
			try (PreparedStatement pstatement = connection.prepareStatement(query);) {
				pstatement.setInt(1, id_buyer);
				pstatement.setInt(2, id_product);
				int rowsAffected = pstatement.executeUpdate();
				if (rowsAffected > 0) {
					Selected_product s = new Selected_product();
					s.setId_buyer(id_buyer);
					s.setId_product(id_product);
					s.setPhoto(photo);
					return s;
				}

			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return null;
	}

	// viene chiamato nella servlet CaricaCarrello
		//per salvare, associato al selected_product, tutte le altre informazioni necessarie
		public Selected_product AddSelectedProduct(int id_buyer, int id_product, String photo, int quantity,
			float unitprice, int id_range) throws SQLException {
		String query1 = "insert into selected_product (id_buyer,views_time,quantity_selected,unit_price,id_product,id_range) "
				+ " values (?,CURRENT_TIMESTAMP(),?,?,?,? );";
		String query2 = "select id_sel_prod from selected_product where id_sel_prod =(select max(id_sel_prod) from selected_product );";

		try {
			connection.setAutoCommit(false);

			try (PreparedStatement pstatement1 = connection.prepareStatement(query1);
					PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
				pstatement1.setInt(1, id_buyer);
				pstatement1.setInt(2, quantity);
				pstatement1.setFloat(3, unitprice);
				pstatement1.setInt(4, id_product);
				pstatement1.setFloat(5, id_range);
				int rowsAffected = pstatement1.executeUpdate();
				if (rowsAffected > 0) {
					Selected_product s = new Selected_product();
					s.setId_buyer(id_buyer);
					s.setId_product(id_product);
					s.setPhoto(photo);
					s.setQuantity_selected(quantity);
					s.setUnit_price(unitprice);
					s.setId_ranges(id_range);
					ResultSet result = pstatement2.executeQuery();
					if (result.next())
						s.setId__sel_product(result.getInt("id_sel_prod"));
					return s;
				}
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return null;
	}

}