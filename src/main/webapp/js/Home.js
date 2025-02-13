(function() {	
	var gestorePagina;

	window.addEventListener("load", () => {
		if (infoUtente() != null) {
			gestorePagina = new GestorePagina();//creo gestore della pagina
			gestorePagina.init();//lo inizializzo
			gestorePagina.visHome();//visualizzo la Home page
		}
		else window.location.href = "LoginPage.html";//altrimenti visualizzo la Login page
	});

	function Benvenuto(lBenvenuto, buyer) {
		this.buyer = buyer;
		this.show = function() { lBenvenuto.textContent = this.buyer; }
	}
	// Menu per la navigazione all'interno della pagina
	function Menu(bHome, bCarrello, bOrdini, tRicerca, bRicerca, bLogout) {//nodi contenenti b=bottonw t=textbox
		this.bHome = bHome;
		this.bCarrello = bCarrello;
		this.bOrdini = bOrdini;
		this.tRicerca = tRicerca;
		this.bRicerca = bRicerca;
		this.bLogout = bLogout;
		this.aggiungiEventi = function(gestore) {
			this.bHome.addEventListener("click", () => { gestore.visHome(); });
			bCarrello.addEventListener("click", () => { gestore.visCarrello(); });
			bOrdini.addEventListener("click", () => { gestore.visOrdini(); });
			tRicerca.addEventListener("keypress", (e) => {
				if (e.code === "Enter") {//se l'utente preme INVIO nel campo di ricerca l'evento viene reindirizzato al gestore del pulsante
					bRicerca.click();
					e.preventDefault();
				}
			});
			bRicerca.addEventListener("click", (e) => {
				var form = e.target.closest("form");
				if (form.checkValidity()) {//se il form è valido si effettua la ricerca
					gestore.visRisultati(form);
				}
				else form.reportValidity();
			})
			bLogout.addEventListener("click", () => {
				makeCall("GET", "Logout", null, gestore.messaggio, () => {
					window.sessionStorage.removeItem("buyer");
					window.sessionStorage.removeItem("cart");
					window.location.href = "LoginPage.html";
				});
			})
		}
	}
	function GestorePagina() {//regola l'inizializzazione dei componenti e la visualizzazione degli elementi
		this.messaggio = null;
		this.benvenuto = null;
		this.menu = null;
		this.ordini = null;
		this.listResults = null;
		this.listSuggested = null;
		this.cart = null;
		this.init = function() {
			this.messaggio = document.getElementById("messaggio");// Messaggio per errori/notifiche
			this.benvenuto = new Benvenuto(document.getElementById("buyer"), infoUtente().name);// Messaggio di benvenuto
			this.benvenuto.show();
			this.menu = new Menu(// Menu con bottoni per la navigazione 
				document.getElementById("bottoneHome"),
				document.getElementById("bottoneCarrello"),
				document.getElementById("bottoneOrdini"),
				document.getElementById("testoRicerca"),
				document.getElementById("bottoneRicerca"),
				document.getElementById("bottoneLogout")
			);
			this.menu.aggiungiEventi(this);
			// Lista per visualizzare prodotti cercati tramite keyword
			this.listResults = new ListaOggetti(this, ProductResult, document.getElementById("listResults"),
				function(keyword) {
					caricaLista(this, "GET", "SearchKeyword?keyword=" + keyword, null, messaggio, false, 'Nessun risultato con la parola chiave \"' + keyword + '"');
				}
			);
			// Lista per mostrare gli ultimi 5 visualizzati ed eventuali random da una categoria di default
			this.listSuggested = new ListaOggetti(this, ProductSuggested, document.getElementById("listSuggested"),
				function() {
					caricaLista(this, "POST", "CaricaVisualizzati", null, messaggio, true, "Nessun prodotto visualizzato di recente");
				}
			);
			// Lista per visualizzare gli ordini dell'utente
			this.ordini = new ListaOggetti(this, Order, document.getElementById("ordini"),
				function() {
					caricaLista(this, "GET", "VisualizzaOrdini", null, messaggio, false,"Nessun ordine presente");
				}
			);
		}
		this.update = function() {
			this.messaggio.textContent = "";//Ogni volta che si aggiorna la pagina, il messaggio precedente viene cancellato
			
			// Ogni volta che si aggiorna la pagina, i contenuti vengono nascosti
			this.listResults.hide();
			this.listSuggested.hide();
			document.getElementById("cart").hidden = true
			this.ordini.hide();
		}
		this.visHome = function() {
			this.update();//nascondo tutto
			this.listSuggested.carica(null);//mostro listSuggested
		}
		this.visRisultati = function(form) {
			this.update();//nascondo tutto
			this.listResults.carica(new FormData(form).get("keyword"));//mostro listResults
		}
		this.visCarrello = function() {
			this.update();//nascondo tutto
			document.getElementById("cart").hidden = false;//mostro il carrello
		}
		this.visOrdini = function() {
			this.update();//nascondo tutto
			this.ordini.carica(null);//mostro ordini
		}
	}
})();