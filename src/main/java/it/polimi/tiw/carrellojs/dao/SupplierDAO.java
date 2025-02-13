package it.polimi.tiw.carrellojs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Supplier;

public class SupplierDAO {
	private Connection connection;

	public SupplierDAO() {
	}

	public SupplierDAO(Connection con) {
		this.connection = con;
	}

	public Supplier SupplierDetails(int id_supplier) throws SQLException {
		String query = " select id_supplier,name,num_stars,amount  from supplier where id_supplier=?;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_supplier);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;
				else {
					result.next();
					Supplier supplier = new Supplier();
					supplier.setId_supplier(result.getInt("id_supplier"));
					supplier.setName(result.getString("name"));
					supplier.setNumStars(result.getInt("num_stars"));
					supplier.setAmount(result.getFloat("amount"));
					return supplier;
				}
			}
		}
	}
	public boolean CheckExistence(String name) throws SQLException {
		String query = " select id_supplier from supplier where name=?;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, name);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return false;
				else {
					result.next();
					return true;
				}
			}
		}
	}
}