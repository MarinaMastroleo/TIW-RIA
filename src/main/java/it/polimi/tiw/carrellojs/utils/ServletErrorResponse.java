package it.polimi.tiw.carrellojs.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ServletErrorResponse {

	// Metodo per inviare una risposta contenente un errore al client
	public static void createResponse(HttpServletResponse response, int code, String message) throws IOException {
		/*
		 * response: Oggetto risposta tramite il quale è possibile comunicare 
		 * code: Codice d'errore 
		 * message: Messaggio d'errore 
		 * IOException: Viene lanciata una IOException nel caso non sia possibile accedere in scrittura allo Stream della risposta
		 */
		response.setStatus(code);
		response.setContentType("text/html");
		response.getWriter().println(message);
		return;
	}
}