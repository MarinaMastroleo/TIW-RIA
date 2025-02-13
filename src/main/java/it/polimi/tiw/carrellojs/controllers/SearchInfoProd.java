package it.polimi.tiw.carrellojs.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

import it.polimi.tiw.carrellojs.beans.Buyer;
import it.polimi.tiw.carrellojs.beans.Product;
import it.polimi.tiw.carrellojs.dao.ProductDAO;
import it.polimi.tiw.carrellojs.dao.Selected_productDAO;

@WebServlet("/SearchInfoProd")
public class SearchInfoProd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public SearchInfoProd() {
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
		String photo = jsonObject.get("ProdPhoto").getAsString();

		Product product = new Product();
		Selected_productDAO selected_productDAO = new Selected_productDAO(connection);
		ProductDAO productDAO = new ProductDAO(connection);
		HttpSession session = request.getSession();
		Buyer b = new Buyer();
		b = (Buyer) session.getAttribute("buyer");
		int idbuyer = b.getIdBuyer();

		try {// aggiungo al DB il prodotto selezionato
			selected_productDAO.AddSelProduct(idbuyer, idProduct, photo);
		} catch (SQLException e1) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
		}
		try {
			product = productDAO.ProductDetails(idProduct);// metto nel product nome,descrizione,categoria merceologica e
															// foto
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Impossibile recuperare dettagli prodotto dal suo id: ");
			return;
		}

		String json = gson.toJson(product);

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