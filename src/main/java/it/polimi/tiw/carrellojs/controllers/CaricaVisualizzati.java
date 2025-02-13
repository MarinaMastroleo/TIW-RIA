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

import it.polimi.tiw.carrellojs.beans.Buyer;
import it.polimi.tiw.carrellojs.beans.Product;
import it.polimi.tiw.carrellojs.dao.ProductDAO;

@WebServlet("/CaricaVisualizzati")
public class CaricaVisualizzati extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CaricaVisualizzati() {
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

		Gson gson = new Gson();
		List<Product> list = new ArrayList<Product>();
		int j;
		ProductDAO productDAO = new ProductDAO(connection);

		HttpSession session = request.getSession();
		Buyer b = new Buyer();
		b = (Buyer) session.getAttribute("buyer");
		if (b != null) {

			try {
				list.addAll(productDAO.FindLast5(b.getIdBuyer()));
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Impossibile aggiungere prodotti da visualizzare nella HomePage");
				return;
			}
			j = list.size();

			if (j >= 0 && j <= 4) {
				try {
					list.addAll(productDAO.FindOtherSuggested(b.getIdBuyer(),j));
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Impossibile aggiungere prodotti suggeriti da visualizzare nella HomePage");
				}
			}
		}
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		String json = gson.toJson(list);

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