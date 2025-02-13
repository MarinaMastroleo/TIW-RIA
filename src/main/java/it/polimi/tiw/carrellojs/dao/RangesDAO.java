package it.polimi.tiw.carrellojs.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.carrellojs.beans.Ranges;

public class RangesDAO {
	private Connection connection;

	public RangesDAO() {
	}

	public RangesDAO(Connection con) {
		this.connection = con;
	}

	public List<Ranges> FindRanges(int id_supplier) throws SQLException {
		List<Ranges> list = new ArrayList<Ranges>();
		String query = " select num_min_articles,num_max_articles,shipping_costs from ranges where id_supplier=?;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_supplier);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return null;
				else
					while (result.next()) {
						Ranges ranges = new Ranges();
						String max;
						ranges.setNum_min_articles(result.getInt("num_min_articles"));
						max = result.getString("num_max_articles");
						if (max == null) {
							ranges.setNum_max_articles("infinito");
						} else {
							ranges.setNum_max_articles(result.getString("num_max_articles"));
						}
						ranges.setShipping_costs(result.getFloat("shipping_costs"));
						list.add(ranges);
					}
			}
		}
		return list;
	}

	public Integer FindIdRange(int id_supplier, int quantity) throws SQLException {
		int id;
		String query = " select  R.id_range  " + " from ranges R, supplier P "
				+ " where P.id_supplier=? and P.id_supplier=R.id_supplier and num_max_articles>= ? and "
				+ " num_min_articles<= ? and num_max_articles is not null " + " union all " + " select  A.id_range "
				+ " from ranges A, supplier B "
				+ " where B.id_supplier=? and B.id_supplier=A.id_supplier and  num_min_articles<= ? and num_max_articles is null;  ";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_supplier);
			pstatement.setInt(2, quantity);
			pstatement.setInt(3, quantity);
			pstatement.setInt(4, id_supplier);
			pstatement.setInt(5, quantity);

			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return null;
				else {
					result.next();
					id = result.getInt("id_range");
				}
			}
		}
		return id;
	}

	public Float FindShippingCosts(int id_supplier, int quantity) throws SQLException {

		String query = " select R.shipping_costs " + " from ranges R, supplier P "
				+ " where P.id_supplier=? and P.id_supplier=R.id_supplier and R.num_max_articles >= ? and "
				+ " num_min_articles <= ? and R.num_max_articles is not null " + " union all "
				+ " select A.shipping_costs " + " from ranges A, supplier B "
				+ " where B.id_supplier=? and B.id_supplier=A.id_supplier and A.num_min_articles <= ? and A.num_max_articles is null;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_supplier);
			pstatement.setInt(2, quantity);
			pstatement.setInt(3, quantity);
			pstatement.setInt(4, id_supplier);
			pstatement.setInt(5, quantity);

			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					float c = result.getFloat("shipping_costs");
					return c;
				}
			}
		}
		return null;
	}

	public Float FindAmount(int id_supplier) throws SQLException {
		String query = " select amount from supplier where id_supplier=? ;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_supplier);
			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					float c = result.getFloat("amount");
					return c;
				}
			}
		}
		return null;
	}
}
