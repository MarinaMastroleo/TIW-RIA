/*Funzione utilizzata per caricare una lista di elementi
 *   {ListaOggetti} self è il riferimento alla lista di oggetti
 *   {String} httpMethod da utilizzare per la chiamata
 *   {String} url da utilizzare per la chiamata
 *   {Object} data Eventuali dati aggiuntivi da inserire nella chiamata
 *   {Node} responseTag è il nodo da utilizzare per visualizzare il messaggio di risposta
 *   {Boolean} json che è true se i dati aggiuntivi specificati nel parametro "data" siano di tipo JSON
 *   {String} emptyMessage Messaggio da visualizzare nel caso la lista sia vuota
 */
function caricaLista(self, httpMethod, url, data, responseTag, json, emptyMessage) {
	makeCall(httpMethod, url, data, responseTag, function(req) {
		var elementi = JSON.parse(req.responseText);//faccio il parsing e lo metto in 'elementi'
		if (elementi.length == 0) {
			if (emptyMessage) {
				responseTag.className = "messaggioNotifica";
				responseTag.textContent = emptyMessage;
			}
			return;
		}
		self.update(elementi);
	}, json);
}


// Recupera le informazioni dell'utente (con tutti i suoi dati) dalla sessione
function infoUtente() {
	return JSON.parse(window.sessionStorage.getItem("buyer"));
}


function makeCallJSON(method, url, json, cback) {
	var req = new XMLHttpRequest(); // visible by closure
	req.onreadystatechange = function() {
		if (req.readyState === 4) {
			if (req.status === 200) {
				cback(req);
			} else {
				console.log('Request failed with status:', req.status);
				console.log('Response:', req.responseText);
				document.getElementById('messaggio').className = "messaggioErrore";
				document.getElementById('messaggio').textContent =  req.responseText;
			}
		}
	};
	//
	req.open(method, url);
	req.setRequestHeader("Content-Type", "application/json");

	if (json == null) req.send();
	else req.send(json);
}

/*Fa una chiamata al server, usando l'oggetto XMLHttpRequest
 *   {String} httpMethod da utilizzare per la chiamata
 *   {String} url da utilizzare per la chiamata
 *   {Object} data Eventuali dati aggiuntivi da inserire nella chiamata
 *   {Node} responseTag Nodo da utilizzare per visualizzare il messaggio di risposta
 *   {Function} callBack Funzione da chiamare una volta ottenuta una risposta positiva dal server
 *   {Boolean} json Flag è true se i dati aggiuntivi specificati nel parametro "data" siano di tipo JSON
 *   {Boolean} login Flag è true se la chiamata arriva da una richiesta di autenticazione (per la gestione di 401)
*/
function makeCall(httpMethod, url, data, responseTag, callBack, json, login) {
	var req = new XMLHttpRequest();//variabile zombie
	//qando cambi lo stato chiama questa funz
	req.onreadystatechange = function() {//onreadystatechange memorizza la funz che gestiche l'evento
		if (req.readyState == XMLHttpRequest.DONE) {//readyState memorizza la  funz che gestisce l'evento di cambio dello stato
			if (req.status == 200) callBack(req);
			else if (req.status == 401 && !login || req.status == 403) {
				// Nel caso l'utente non abbia effettuato l'accesso o non possieda i privilegi
				// per visualizzare una determinata risorsa, viene rispedito alla LoginPage (a meno che l'utente non stia tentando di autenticarsi)
				window.sessionStorage.removeItem("buyer");
				window.location.href = "LoginPage.html";
			}
			else {
				responseTag.className = "messaggioErrore";
				if (req.status == 404 && !req.responseText)// Nel caso avvenga un errore durante l'inizializzazione della servlet
					responseTag.textContent = "Errore: " + req.status + " - Risorsa non disponibile";
				else responseTag.textContent = "Errore: " + req.status + " - " + req.responseText;
			}
		}
	};
	req.open(httpMethod, url);//apre la richiesta verso un certo endpoint, ma il metodo js continua a funzionare xk è asincrona
	if (json)//Se i dati sono di tipo JSON viene specificato il Content-Type all'interno della richiesta
		req.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	if (data == null) req.send();//mando la richiesta al server senza data
	else req.send(data);//mando la richiesta al server con data
}