package it.polimi.tiw.carrellojs.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.carrellojs.utils.ServletErrorResponse;
import it.polimi.tiw.carrellojs.beans.*;
import it.polimi.tiw.carrellojs.dao.ItemDAO;
import it.polimi.tiw.carrellojs.dao.OrderDAO;

@WebServlet("/VisualizzaOrdini")
public class VisualizzaOrdini extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public VisualizzaOrdini() {
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

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
		List<Order> ordersToShow = new ArrayList<Order>();
		OrderDAO orderDAO = new OrderDAO(connection);
		ItemDAO itemDAO = new ItemDAO(connection);
		HttpSession session = request.getSession();
		Buyer b = new Buyer();
		b = (Buyer) session.getAttribute("buyer");

		// Ottengo tutti gli ordini associati ad un id utente
		try {
			ordersToShow.addAll(orderDAO.FindListOrders(b.getIdBuyer()));

			for (Order o : ordersToShow) {
				ArrayList<String> productsphotos = new ArrayList<String>();
				productsphotos.addAll(itemDAO.FindPhotos(o.getId_order()));
				o.setProductsphotos(productsphotos);
			}

		} catch (SQLException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Impossibile ottenere ordine da id utente");
			return;
		}
		catch (Exception e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Richiesta malformata");
			return;
		}

		String json = gson.toJson(ordersToShow);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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