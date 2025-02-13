package it.polimi.tiw.carrellojs.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import it.polimi.tiw.carrellojs.beans.Order;
import it.polimi.tiw.carrellojs.beans.Ranges;
import it.polimi.tiw.carrellojs.beans.Selected_product;
import it.polimi.tiw.carrellojs.beans.Supplier;
import it.polimi.tiw.carrellojs.dao.RangesDAO;
import it.polimi.tiw.carrellojs.beans.Product;
import it.polimi.tiw.carrellojs.dao.ProductDAO;
import it.polimi.tiw.carrellojs.dao.Sales_conditionsDAO;
import it.polimi.tiw.carrellojs.dao.SupplierDAO;

@WebServlet("/SearchDetails")
public class SearchDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public SearchDetails() {
		super();
	}

	public void init() throws ServletException {
		try {
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UnavailableException("Couldn't get db connection");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedReader reader = request.getReader();
		Gson gson = new Gson();
		JsonObject jsonObject;

		List<Map<String, Object>> supplierDetailsList = new ArrayList<>();
		SupplierDAO supplierDAO = new SupplierDAO(connection);
		Sales_conditionsDAO sales_conditionsDAO = new Sales_conditionsDAO(connection);
		List<Integer> supplierIds = new ArrayList<Integer>();
		RangesDAO rangesDAO = new RangesDAO(connection);
		ProductDAO productDAO = new ProductDAO(connection);

		try {
			jsonObject = gson.fromJson(reader, JsonObject.class);

		} catch (JsonSyntaxException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Invalid JSON syntax: " + e.getMessage());
			return;
		}
		if (jsonObject == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("Errore! Parametri mancanti");
			return;
		}

		int idProduct = jsonObject.get("idProduct").getAsInt();// prendo l'id del prodotto su cui il buyer ha cliccato

		try {

			supplierIds = sales_conditionsDAO.ListSuppliers(idProduct);// metto in questa lista tutti gli id dei
																		// fornitori che vendono quel prodotto

			for (Integer i : supplierIds) {// per ogni fornitore il cui id è nella lista supplierIds mi creo una mappina
				Map<String, Object> supplierDetails = new HashMap<String, Object>();
				Float unitPrice = sales_conditionsDAO.UnitPrice(idProduct, i);// metto prezzo unitario di quel prodotto
																				// di quel fornitore
				supplierDetails.put("unitPrice", unitPrice);

				List<Ranges> ranges = new ArrayList<Ranges>();
				ranges.addAll(rangesDAO.FindRanges(i));// mi salvo le fasce di quel fornitore
				supplierDetails.put("ranges", ranges);

				Supplier supplier = supplierDAO.SupplierDetails(i);// metto nome fornitore,numero stelle e importo min x
																	// spedizione gratuita
				Integer numProdsInCart = 0;
				Float priceProdsInCart = 0.0f;
				ArrayList<Product> overlaylist = new ArrayList<Product>();
				HttpSession session = request.getSession();
				ArrayList<Order> orders = (ArrayList<Order>) session.getAttribute("cart");
				if (orders == null)
					orders = new ArrayList<>();// se non ho ordini nella sessione creo un arraylist
				for (Order o : orders) {
					if (o.getName_supplier().equals(supplier.getName())) {
						numProdsInCart = o.getNum_ordered_articles();
						priceProdsInCart = o.getPrice_tot_products();

						for (Selected_product q : o.getProducts()) {
							int idp = productDAO.Convert(q.getId__sel_product());
							Product product = new Product();
							product = productDAO.ProductDetails(idp);
							overlaylist.add(product);
						}

					}
				}
				supplier.setOverlaylist(overlaylist);
				supplier.setNumProdsInCart(numProdsInCart);
				supplier.setPriceProdsInCart(priceProdsInCart);
				supplierDetails.put("supplier", supplier);
				supplierDetailsList.add(supplierDetails);// metto tutte queste mappe nella lista grande di mappe
			}
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Impossibile recuperare dettagli prodotto dal suo id: ");
			return;
		}
		String json = gson.toJson(supplierDetailsList);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(json);
	}

	public void destroy() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}
}