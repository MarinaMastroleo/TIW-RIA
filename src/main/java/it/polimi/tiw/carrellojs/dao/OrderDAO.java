package it.polimi.tiw.carrellojs.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Order;

public class OrderDAO {
	private Connection connection;

	public OrderDAO() {
	}

	public OrderDAO(Connection con) {
		this.connection = con;
	}

	public List<Order> FindListOrders(int id_buyer) throws SQLException {
		List<Order> list = new ArrayList<Order>();

		String query = "select * from orders  where id_buyer = ?  order by time desc;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_buyer);
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Order order = new Order();
					order.setId_order(result.getInt("id_order"));
					order.setName_supplier(result.getString("name_supplier"));
					order.setPrice_tot_products(result.getFloat("price_tot_products")+result.getFloat("delivery_costs"));
					order.setDelivery_date(result.getString("delivery_date"));
					order.setAddress_buyer(result.getString("address_buyer"));
					order.setNum_ordered_articles(result.getInt("num_ordered_articles"));
					list.add(order);
				}
			}
		}
		return list;
	}

	public void sentOrder(String supplier, float price_tot_products, float delivery_costs, int num_ordered_articles,
			int idbuyer, Order order) throws SQLException {

		String query1 = "select address from buyer where id_buyer = ?";
		String query2 = "insert into orders(name_supplier,price_tot_products,delivery_costs,delivery_date,address_buyer,num_ordered_articles,id_buyer,time) "
				+ " values (?, ?, ?,date_add(curdate(), interval 1 day),?,?,?,CURRENT_TIMESTAMP())";
		String address = null;
		try {
			connection.setAutoCommit(false);
			try (PreparedStatement pstatement1 = connection.prepareStatement(query1);
					PreparedStatement pstatement2 = connection.prepareStatement(query2)) {
				// prima query
				pstatement1.setInt(1, idbuyer);
				ResultSet resultSetAd = pstatement1.executeQuery();
				while (resultSetAd.next())
					address = resultSetAd.getString("address");
				// seconda query
				pstatement2.setString(1, supplier);
				pstatement2.setFloat(2, price_tot_products);
				pstatement2.setFloat(3, delivery_costs);
				pstatement2.setString(4, address);
				pstatement2.setInt(5, num_ordered_articles);
				pstatement2.setInt(6, idbuyer);
				pstatement2.executeUpdate();
			}
			connection.commit();
		} catch (SQLException e) {
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
	}
}