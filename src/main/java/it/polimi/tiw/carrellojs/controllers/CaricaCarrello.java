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

import it.polimi.tiw.carrellojs.dao.SupplierDAO;
import it.polimi.tiw.carrellojs.utils.ServletErrorResponse;
import it.polimi.tiw.carrellojs.beans.Buyer;
import it.polimi.tiw.carrellojs.beans.Order;
import it.polimi.tiw.carrellojs.beans.Selected_product;
import it.polimi.tiw.carrellojs.dao.RangesDAO;
import it.polimi.tiw.carrellojs.dao.Sales_conditionsDAO;
import it.polimi.tiw.carrellojs.dao.Selected_productDAO;

@WebServlet("/CaricaCarrello")
@MultipartConfig
public class CaricaCarrello extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CaricaCarrello() {
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

		Selected_productDAO selected_productDAO = new Selected_productDAO(connection);
		Sales_conditionsDAO sales_conditionsDAO = new Sales_conditionsDAO(connection);
		SupplierDAO supplierDAO = new SupplierDAO(connection);

		RangesDAO rangesDAO = new RangesDAO(connection);

		HttpSession session = request.getSession();
		Buyer b = new Buyer();
		b = (Buyer) session.getAttribute("buyer");
		int idbuyer = b.getIdBuyer();
		Integer idSelected, idsupplier, quantity, unitPrice;
		String suppliername, productPhoto;

		try {
			// prendo informazioni dal json
			idSelected = jsonObject.get("idSelected").getAsInt();
			idsupplier = jsonObject.get("idsupplier").getAsInt();
			quantity = jsonObject.get("quantity").getAsInt();
			suppliername = jsonObject.get("suppliername").getAsString();
			productPhoto = jsonObject.get("productPhoto").getAsString();
			unitPrice = jsonObject.get("unitPrice").getAsInt();

			ArrayList<Order> orders = (ArrayList<Order>) session.getAttribute("cart");
			if (orders == null)
				orders = new ArrayList<>();

			int quantityAvailable;
			boolean cartContainsSupplier = false, cartContainsProduct = false;
			int idrange;

			try {

				if (idsupplier > 0 && idsupplier < 6 && idsupplier instanceof Integer && suppliername instanceof String
						&& supplierDAO.CheckExistence(suppliername) && !suppliername.equals("") && unitPrice > 6
						&& unitPrice < 151 && unitPrice instanceof Integer && !unitPrice.equals("")
						&& productPhoto instanceof String && productPhoto.length() <= 150 && productPhoto.length() > 8
						&& productPhoto.matches("^[a-z_]+\\.png$") && !productPhoto.equals("")
						&& idSelected instanceof Integer && quantity > 0 && suppliername.length() < 46
						&& quantity instanceof Integer && suppliername != null && idSelected > 0 && quantity != null) {

					quantityAvailable = sales_conditionsDAO.FindQuantityAvailable(idSelected, idsupplier);

					if (quantity <= quantityAvailable) {

						for (Order o : orders) {
							if (o.getName_supplier().equals(suppliername)) {
								for (Selected_product s : o.getProducts()) {
									if (s.getId_product() == idSelected) {
										// se cart contiene già nomefornitore ed idprodotto
										// aumento la quantità del prodotto
										float m = 0;
										for (int i = 0; i < s.getQuantity_selected(); i++)
											m = m + s.getUnit_price();
										s.setQuantity_selected(s.getQuantity_selected() + quantity);
										o.setNum_ordered_articles(o.getNum_ordered_articles() + quantity);

										for (int u = 0; u < o.getNum_ordered_articles(); u++) {
											o.getProducts().add(s);
										}
										// poi dico che cart contiene già nomefornitore e idprodotto
										cartContainsProduct = true;
										cartContainsSupplier = true;

										float t = 0;
										for (int i = 0; i < s.getQuantity_selected(); i++) {
											t = t + s.getUnit_price();
										}
										o.setPrice_tot_products(t + o.getPrice_tot_products() - m);
										Selected_product z = s;
										for (int j = 0; j < quantity; j++)
											o.getProducts().add(z);
										try {
											float f = rangesDAO.FindShippingCosts((int) idsupplier,
													o.getNum_ordered_articles());
											float amount = rangesDAO.FindAmount((int) idsupplier);
											if (o.getPrice_tot_products() < amount) {
												o.setDelivery_costs(f);
											} else {
												float h = 0.0f;
												o.setDelivery_costs(h);
											}
										} catch (SQLException e) {
											response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
													"errori nel dao ");
										}
										break;
									}
								}
								// se cart contiene già nomefornitore ma non idprodotto
								// allora creo un prodotto e lo aggiungo
								if (!cartContainsProduct) {
									Selected_product s = new Selected_product();
									try {
										idrange = rangesDAO.FindIdRange(idsupplier, quantity);
										s = selected_productDAO.AddSelectedProduct(idbuyer, idSelected, productPhoto,
												quantity, unitPrice, idrange);
									} catch (SQLException e) {
										response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
												"errori nel dao ");
									}
									s.setQuantity_selected(quantity);
									for (int u = 0; u < quantity; u++) {
										// aggiungo all'ordine il prodotto creato tante
										// volte quanto è il numero quantity
										o.getProducts().add(s);
									}
									o.setNum_ordered_articles(o.getNum_ordered_articles() + quantity);

									try {
										float f = rangesDAO.FindShippingCosts((int) idsupplier,
												o.getNum_ordered_articles());
										float amount = rangesDAO.FindAmount((int) idsupplier);

										if (o.getPrice_tot_products() < amount) {
											o.setDelivery_costs(f);
										} else {
											float h = 0.0f;
											o.setDelivery_costs(h);
										}
									} catch (SQLException e) {
										response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
												"errori nel dao ");
									}
									cartContainsSupplier = true;
									float t = 0;
									for (Selected_product i : o.getProducts())
										t += i.getUnit_price();
									o.setPrice_tot_products(t);
									try {
										float f = rangesDAO.FindShippingCosts((int) idsupplier,
												o.getNum_ordered_articles());
										float amount = rangesDAO.FindAmount((int) idsupplier);
										if (o.getPrice_tot_products() < amount) {
											o.setDelivery_costs(f);
										} else {
											float h = 0.0f;
											o.setDelivery_costs(h);
										}
									} catch (SQLException e) {
										response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
												"errori nel dao ");
									}
									break;
								}
							}
						}
						// cart non contiene nomefornitore allora creo tutto
						if (!cartContainsSupplier) {
							Order o = new Order();
							o.setName_supplier(suppliername);
							Selected_product s = null;
							try {
								idrange = rangesDAO.FindIdRange(idsupplier, quantity);
								s = selected_productDAO.AddSelectedProduct(idbuyer, idSelected, productPhoto, quantity,
										unitPrice, idrange);
							} catch (SQLException e) {
								response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
							}
							s.setQuantity_selected(quantity);
							for (int u = 0; u < quantity; u++) {// aggiungo all'ordine il prodotto creato tante
																// volte quanto è il numero quantity
								o.getProducts().add(s);
							}
							o.setNum_ordered_articles(o.getNum_ordered_articles() + quantity);

							try {
								float f = rangesDAO.FindShippingCosts((int) idsupplier, o.getNum_ordered_articles());
								float amount = rangesDAO.FindAmount((int) idsupplier);
								if (o.getPrice_tot_products() < amount) {
									o.setDelivery_costs(f);
								} else {
									float h = 0.0f;
									o.setDelivery_costs(h);
								}
							} catch (SQLException e) {
								response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
							}
							float c = 0;
							for (Selected_product i : o.getProducts()) {
								c += i.getUnit_price();
							}
							o.setPrice_tot_products(c);
							try {
								float f = rangesDAO.FindShippingCosts((int) idsupplier,
										o.getNum_ordered_articles());
								float amount = rangesDAO.FindAmount((int) idsupplier);
								if (o.getPrice_tot_products() < amount) {
									o.setDelivery_costs(f);
								} else {
									float h = 0.0f;
									o.setDelivery_costs(h);
								}
							} catch (SQLException e) {
								response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
										"errori nel dao ");
							}
							orders.add(o);
						}
					} else if (quantity > quantityAvailable) {
						ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
								"La quantità richiesta è maggiore di quella disponibile dal fornitore, per favore riprova.");
					}
				} else {
					ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
							"Errore,parametri errati!");
				}
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "errori nel dao ");
			}
			session.setAttribute("cart", orders);
			String json = gson.toJson(orders);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(json);
		} catch (JsonParseException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
		} catch (UnsupportedOperationException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
		} catch (NumberFormatException e) {
			ServletErrorResponse.createResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
					"Errore,parametri errati!");
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