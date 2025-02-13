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

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.carrellojs.beans.Product;
import it.polimi.tiw.carrellojs.dao.ProductDAO;
import it.polimi.tiw.carrellojs.utils.ServletErrorResponse;

@WebServlet("/SearchKeyword")
public class SearchKeyword extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public SearchKeyword() {
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

		List<Product> listaProdotti = new ArrayList<>();
		String keyword = "";

		ProductDAO productDAO = new ProductDAO(connection);

		try {
			keyword = StringEscapeUtils.escapeJava(request.getParameter("keyword"));
			if (keyword != null && !keyword.equals(""))
				listaProdotti.addAll(productDAO.FindListProductsbyKeyword(keyword));
		} catch (SQLException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Impossibile recuperare prodotti da keyword");
			return;
		} catch (Exception e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Keyword non esistente");
			return;
		}

		String json = gson.toJson(listaProdotti);

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