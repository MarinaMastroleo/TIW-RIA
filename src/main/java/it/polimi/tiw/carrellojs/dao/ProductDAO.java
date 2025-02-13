package it.polimi.tiw.carrellojs.dao;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.carrellojs.beans.Product;

public class ProductDAO {
	private Connection connection;

	public ProductDAO() {
	}

	public ProductDAO(Connection con) {
		this.connection = con;
	}

	public List<Product> FindLast5(int id_buyer) throws SQLException {
		List<Product> last5 = new ArrayList<Product>();
		int i = 0;
		String query = " select  P.id_product, P.name,P.description, P.merchandise_category,P.photo,  MAX(S.views_time) as viewtime "
				+ " from product P, buyer B, selected_product S "
				+ " where B.id_buyer=? and B.id_buyer=S.id_buyer and S.id_product=P.id_product   "
				+ " group by P.id_product, P.name,P.description, P.merchandise_category,P.photo "
				+ " order by viewtime " + " desc limit 5;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_buyer);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return last5;
				else
					while (i < 5 && result.next()) {
						Product prod = new Product();
						prod.setIdProduct(result.getInt("id_product"));
						prod.setName(result.getString("name"));
						prod.setDescription(result.getString("description"));
						prod.setMerchandise_category(result.getString("merchandise_category"));
						prod.setPhoto(result.getString("photo"));
						last5.add(prod);
						i++;
					}
			}
		}
		return last5;
	}

	public List<Product> FindOtherSuggested(int id_buyer,int j) throws SQLException {
		List<Product> suggested = new ArrayList<Product>();
		int i = 0;
		int k=5-j;
		//mi salvo in questa tabella gli ultimi da 0 a 4 prodotti visualizzati
		String query = " create or replace view sub_vista(a,b,c,d,e)  as ( select P2.* "
				+ " from product P2,buyer B2,selected_product S2 "
				+ " where B2.id_buyer=?  and B2.id_buyer=S2.id_buyer and S2.id_product=P2.id_product  "
				+ " order by S2.views_time desc); ";
		PreparedStatement pstatement1 = null;
		//seleziono max 5 prodotti altri da suggerire tali che non siano fra gli ultimi 5 visualizzati
		//in questo modo non mostrerò mai doppioni nella pagina html
		String query2 = " select  distinct P.* from product P  "
				+ " where P.id_product not in ( select S.a from sub_vista S)"
				+ " and P.merchandise_category like 'abbigliamento'  " 
				+ " order by rand() limit 5";

		PreparedStatement pstatement2 = null;
		try {
			connection.setAutoCommit(false);

			pstatement1 = connection.prepareStatement(query);
			pstatement1.setInt(1, id_buyer);
			pstatement1.executeUpdate();
			pstatement2 = connection.prepareStatement(query2);
			ResultSet result2 = pstatement2.executeQuery();
			if (!result2.isBeforeFirst()) {
				connection.rollback();
				throw new SQLException();
			} else {
				while (i < k && result2.next()) {
					Product prod = new Product();
					prod.setIdProduct(result2.getInt("id_product"));
					prod.setName(result2.getString("name"));
					prod.setDescription(result2.getString("description"));
					prod.setMerchandise_category(result2.getString("merchandise_category"));
					prod.setPhoto(result2.getString("photo"));
					suggested.add(prod);
					i++;
				}
				connection.commit();
			}
		} catch (SQLException e) {
			connection.rollback();
		} finally {
			connection.setAutoCommit(true);
		}
		return suggested;
	}
	public List<Product> FindListProductsbyKeyword(String keyword) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		String query = "select P.*, S.unit_price from product P, sales_conditions S "
				+ " where (P.name LIKE CONCAT('%',?,'%') or P.description LIKE CONCAT('%',?,'%') ) "
				+ " and P.id_product=S.id_product and S.unit_price = "
				+ " (select min(S2.unit_price) from sales_conditions S2 where S2.id_product = S.id_product) "
				+ " order by S.unit_price;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, keyword);
			pstatement.setString(2, keyword);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					throw new SQLException();
				else
					while (result.next()) {
						Product prod = new Product();
						prod.setIdProduct(result.getInt("id_product"));
						prod.setName(result.getString("name"));
						prod.setPhoto(result.getString("photo"));
						prod.setMin_unit_price(result.getInt("unit_price"));
						list.add(prod);
					}
			}
		}
		return list;
	}

	public Product ProductDetails(int id_product) throws SQLException {
		String query = "select name,description,merchandise_category,photo from product where id_product=? ;";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_product);
			try (ResultSet result = pstatement.executeQuery();) {
				if (!result.isBeforeFirst())
					return null;// aggiustare in modo che non mi ritorni null se scrivo keyword non valida
				else {
					result.next();
					Product prod = new Product();
					prod.setName(result.getString("name"));
					prod.setDescription(result.getString("description"));
					prod.setMerchandise_category(result.getString("merchandise_category"));
					prod.setPhoto(result.getString("photo"));
					return prod;
				}
			}
		}
	}

	public int Convert(int id_sel_prod) throws SQLException {
		String query = " select id_product from selected_product where id_sel_prod=?; ;";

		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, id_sel_prod);

			try (ResultSet result = pstatement.executeQuery();) {
				if (result.next()) {
					int c = result.getInt("id_product");
					return c;
				}
			}
		}
		return 0;
	}
}