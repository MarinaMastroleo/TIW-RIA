package it.polimi.tiw.carrellojs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.carrellojs.beans.Sales_conditions;

public class Sales_conditionsDAO {
	private Connection connection;

	public Sales_conditionsDAO() {
	}

	public Sales_conditionsDAO(Connection con) {
		this.connection = con;
	}

	public List<Integer> ListSuppliers(int id_product) throws SQLException {
		List<Integer> list = new ArrayList<Integer>();
		String query = "select distinct(id_supplier) from  sales_conditions where  id_product=? ;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_product);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst()) 
					return null;
				else
					while (result.next()) {
						Integer i;
						Sales_conditions s = new Sales_conditions();
						s.setId_supplier(result.getInt("id_supplier"));
						i = s.getId_supplier();
						list.add(i);
					}
			}
		}
		return list;
	}

	public float UnitPrice(int id_product, int id_supplier) throws SQLException {
		float i;
		String query = " select unit_price from sales_conditions  where id_product=? and id_supplier=? ;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_product);
			pstatement.setInt(2, id_supplier);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				Sales_conditions s = new Sales_conditions();
				s.setUnit_price(result.getFloat("unit_price"));
				i = s.getUnit_price();
			}
		}
		return i;
	}

	public int FindQuantityAvailable(int id_product, Integer id_supplier) throws SQLException {
		int q;
		String query = " select quantity from sales_conditions " + " where id_product=? and id_supplier=?;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_product);
			pstatement.setInt(2, id_supplier);
			try (ResultSet result = pstatement.executeQuery();) {
				result.next();
				Sales_conditions s = new Sales_conditions();
				s.setQuantity(result.getInt("quantity"));
				q = s.getQuantity();
			}
		}
		return q;
	}
}
