package it.polimi.tiw.carrellojs.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.polimi.tiw.carrellojs.beans.Buyer;
import it.polimi.tiw.carrellojs.dao.BuyerDAO;
import it.polimi.tiw.carrellojs.utils.ServletErrorResponse;

@WebServlet("/DefaultGoToLoginPage")
@MultipartConfig
public class DefaultGoToLoginPage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public DefaultGoToLoginPage() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

		Buyer buyer = null;
		String email = "";
		String psw = "";

		BuyerDAO buyerDAO = new BuyerDAO(connection);

		// Controlla l'esistenza delle credenziali di accesso
		try {
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			psw = StringEscapeUtils.escapeJava(request.getParameter("psw"));
			if (email == null || psw == null || email.isEmpty() || psw.isEmpty())
				throw new Exception("Credenziali mancanti o inesistenti");
		} catch (Exception e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_BAD_REQUEST,
					"Credenziali non presenti");
			return;
		}

		// Controlla la validità delle credenziali di accesso
		try {
			buyer = buyerDAO.checkCredentials(email, psw);
		} catch (SQLException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Impossibile controllare le credenziali");
			return;
		}

		if (buyer == null) {
			// Se le credenziali non sono valide viene ritornato un messaggio di errore
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Credenziali non valide");
		} else {
			// Se le credenziali sono valide l'utente viene aggiunto alla sessione
			request.getSession().setAttribute("buyer", buyer);

			String json = gson.toJson(buyer);// converto il buyer in formato json

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(json);
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