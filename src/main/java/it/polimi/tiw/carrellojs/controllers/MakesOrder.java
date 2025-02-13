package it.polimi.tiw.carrellojs.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import it.polimi.tiw.carrellojs.beans.Buyer;
import it.polimi.tiw.carrellojs.beans.Order;
import it.polimi.tiw.carrellojs.dao.ItemDAO;
import it.polimi.tiw.carrellojs.dao.OrderDAO;
import it.polimi.tiw.carrellojs.dao.SupplierDAO;
import it.polimi.tiw.carrellojs.utils.ServletErrorResponse;

@WebServlet("/MakesOrder")
@MultipartConfig
public class MakesOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public MakesOrder() {
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
		SupplierDAO supplierDAO = new SupplierDAO(connection);

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

		HttpSession session = request.getSession();
		Buyer b = new Buyer();
		b = (Buyer) session.getAttribute("buyer");
		int idbuyer = b.getIdBuyer();

		try {
			Float price_tot_products = jsonObject.get("price_tot_products").getAsFloat();
			Float delivery_costs = jsonObject.get("delivery_costs").getAsFloat();
			Integer num_ordered_articles = jsonObject.get("num_ordered_articles").getAsInt();
			String suppliername = jsonObject.get("suppliername").getAsString();

			// prendo la lista degli ordini dal carrello
			ArrayList<Order> orders = (ArrayList<Order>) session.getAttribute("cart");

			if (orders == null)
				orders = new ArrayList<>();

			if (suppliername instanceof String && supplierDAO.CheckExistence(suppliername) && !suppliername.equals("")
					&& suppliername != null && suppliername.length() < 46 && price_tot_products instanceof Float
					&& price_tot_products > 0 && !price_tot_products.equals("") && price_tot_products < 9001
					&& delivery_costs instanceof Float && !delivery_costs.equals("") && delivery_costs >= 0.0f
					&& delivery_costs < 41 && num_ordered_articles instanceof Integer && num_ordered_articles > 0
					&& num_ordered_articles < 61) {

				// itero per tutti gli ordini
				for (Order o : orders) {
					if (o.getName_supplier().equals(suppliername)) {
						// vedo se trovo un ordine con nome del fornitore uguale a supplier che prendo
						// da Carrello.html

						OrderDAO orderDAO = new OrderDAO(connection);
						try {
							// inserisco l'ordine nel db
							orderDAO.sentOrder(suppliername, price_tot_products, delivery_costs, num_ordered_articles,
									idbuyer, o);// inserisco l'ordine nel db
							ItemDAO itemDAO = new ItemDAO(connection);
							itemDAO.InsertItems(o.getProducts());// inserisco nel bd tutti i prodotti ordinati
						} catch (SQLException e) {
							response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
						}
						orders.remove(o);// rimuovo l'ordine dal carrello
						session.setAttribute("cart", orders);// aggiorno il carrello con l'ordine che ho rimosso
						break;
					}
				}
				if (orders.isEmpty()) {
					session.setAttribute("cart", null);
					String json = gson.toJson(orders);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(json);
				} else {
					String json = gson.toJson(orders);
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().write(json);
				}
			} else {
				ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
						"Errore,parametri errati!");
			}
		} catch (JsonParseException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
		} catch (UnsupportedOperationException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
		} catch (NumberFormatException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
		}

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