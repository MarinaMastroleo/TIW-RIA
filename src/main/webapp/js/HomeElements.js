//creo il carrello prendendo cart2, il quale è il carrello attuale appena preso dalla sessione
function populatecart(gestore, cart2) {
	var tabellacart, thead1, headRow, headCell1, headCell2, headCell3, tbody1, o, row, cell1, cell2,
		cell3, cell4, cell5, cell6, nestedTable, nestedtbody1, p, nestedRow, nestedCell1, nestedCell2,
		nestedImg, form, supplierInput, priceInput, deliveryInput, numOrderedInput, button;
	tabellacart = document.createElement("table");
	tabellacart.className = "tabellaCarrello";
	tabellacart.id = "tabellacart";
	thead1 = document.createElement("thead1");
	headRow = document.createElement("tr");
	headCell1 = document.createElement("th");
	headCell1.textContent = "Nome fornitore";
	headRow.appendChild(headCell1);
	headCell2 = document.createElement("th");
	headCell2.textContent = "Prezzo totale prodotti";
	headRow.appendChild(headCell2);
	headCell3 = document.createElement("th");
	headCell3.textContent = "Spese spedizione";
	headRow.appendChild(headCell3);
	thead1.appendChild(headRow);
	tabellacart.appendChild(thead1);
	tbody1 = document.createElement("tbody1");
	for (var i = 0; i < cart2.length; i++) {
		o = cart2[i];
		row = document.createElement("tr");
		cell1 = document.createElement("td");
		cell1.textContent = o.name_supplier;
		row.appendChild(cell1);
		cell2 = document.createElement("td");
		cell2.textContent = o.price_tot_products;
		row.appendChild(cell2);
		cell3 = document.createElement("td");
		cell3.textContent = o.delivery_costs;
		row.appendChild(cell3);
		nestedTable = document.createElement("table");
		nestedtbody1 = document.createElement("tbody1");
		for (var j = 0; j < o.products.length; j++) {
			p = o.products[j];
			nestedRow = document.createElement("tr");
			nestedCell1 = document.createElement("th");
			nestedCell1.textContent = "Foto:";
			nestedRow.appendChild(nestedCell1);
			nestedCell2 = document.createElement("td");
			nestedImg = document.createElement("img");
			nestedImg.src = "images/" + p.photo;
			nestedImg.height = "100";
			nestedCell2.appendChild(nestedImg);
			nestedRow.appendChild(nestedCell2);
			nestedtbody1.appendChild(nestedRow);
		}
		nestedTable.appendChild(nestedtbody1);
		cell4 = document.createElement("td");
		cell4.appendChild(nestedTable);
		row.appendChild(cell4);
		cell5 = document.createElement("th");
		cell5.textContent = ":";
		row.appendChild(cell5);
		cell6 = document.createElement("td");
		form = document.createElement("form");
		supplierInput = document.createElement("input");
		supplierInput.type = "hidden";
		supplierInput.name = "supplier";
		supplierInput.value = o.name_supplier;
		form.appendChild(supplierInput);
		priceInput = document.createElement("input");
		priceInput.type = "hidden";
		priceInput.name = "price_tot_products";
		priceInput.value = o.price_tot_products;
		form.appendChild(priceInput);
		deliveryInput = document.createElement("input");
		deliveryInput.type = "hidden";
		deliveryInput.name = "delivery_costs";
		deliveryInput.value = o.delivery_costs;
		form.appendChild(deliveryInput);
		numOrderedInput = document.createElement("input");
		numOrderedInput.type = "hidden";
		numOrderedInput.name = "num_ordered_articles";
		numOrderedInput.value = o.num_ordered_articles;
		form.appendChild(numOrderedInput);
		button = document.createElement("button");
		button.type = "submit";
		button.setAttribute("price_tot_products", o.price_tot_products);
		button.setAttribute("delivery_costs", o.delivery_costs);
		button.setAttribute("num_ordered_articles", o.num_ordered_articles);
		button.setAttribute("suppliername", o.name_supplier);
		button.textContent = "Ordina tutti i prodotti di questo fornitore!";
		(function() {
			button.addEventListener("click", function(e) {
				e.preventDefault();
				var price_tot_products = e.target.getAttribute("price_tot_products");
				var delivery_costs = e.target.getAttribute("delivery_costs");
				var num_ordered_articles = e.target.getAttribute("num_ordered_articles");
				var suppliername = e.target.getAttribute("suppliername");
				var json4 = {
					price_tot_products: price_tot_products, delivery_costs: delivery_costs,
					num_ordered_articles: num_ordered_articles, suppliername: suppliername
				};
				makeCallJSON("POST", "MakesOrder", JSON.stringify(json4), function(req) {
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							try {//dopo che ho effettuato l'ordine
								var element = document.getElementById('tabellacart');//prima pulisco il carrello
								if (element) { element.remove(); }
								var cart2 = JSON.parse(req.responseText);
								populatecart(gestore, cart2);//ripopolo correttamente il carrello
								gestore.visCarrello();//poi gli mostro il carrello
							} catch (error) {
								console.log('Error parsing JSON:', error);
									message = "Error";alert(message);
							}
						} else {
							if (message == "") message = "Error";
							alert(message);
						}
					}
				});
			});
		})();
		form.appendChild(button);
		cell6.appendChild(form);
		row.appendChild(cell6);
		tbody1.appendChild(row);
	}
	tabellacart.appendChild(tbody1);
	this.cart = document.getElementById("cart");
	this.cart.appendChild(tabellacart);
}

function showOverlay(overlayList, overlayDiv) {
	overlayDiv.innerHTML = "";
	var overlayTable, overlayTbody, overlayItem, overlayTr, overlayTh1, overlayTd1, overlayTh2,
		overlayTd2, overlayTh3, overlayTd3, overlayTh4, overlayTd4, nestedI;
	overlayTable = document.createElement("table");
	overlayTbody = document.createElement("tbody");
	overlayTbody.innerHTML = "";//pulisco il contenuto precedente della tabella
	for (var j = 0; j < overlayList.length; j++) {
		overlayItem = overlayList[j];
		overlayTr = document.createElement("tr");
		overlayTh1 = document.createElement("th");
		overlayTh1.textContent = "";
		overlayTd1 = document.createElement("td");
		nestedI = document.createElement("img");
		nestedI.src = "images/" + overlayItem.photo;
		nestedI.height = "100";
		overlayTd1.appendChild(nestedI);
		overlayTh2 = document.createElement("th");
		overlayTh2.textContent = "nome:";
		overlayTd2 = document.createElement("td");
		overlayTd2.textContent = overlayItem.name;
		overlayTh3 = document.createElement("th");
		overlayTh3.textContent = "categoria merceologica:";
		overlayTd3 = document.createElement("td");
		overlayTd3.textContent = overlayItem.merchandise_category;
		overlayTh4 = document.createElement("th");
		overlayTh4.textContent = "descrizione:";
		overlayTd4 = document.createElement("td");
		overlayTd4.textContent = overlayItem.description;
		overlayTr.appendChild(overlayTh1);
		overlayTr.appendChild(overlayTd1);
		overlayTr.appendChild(overlayTh2);
		overlayTr.appendChild(overlayTd2);
		overlayTr.appendChild(overlayTh3);
		overlayTr.appendChild(overlayTd3);
		overlayTr.appendChild(overlayTh4);
		overlayTr.appendChild(overlayTd4);
		overlayTbody.appendChild(overlayTr);
	} overlayTable.appendChild(overlayTbody);
	overlayDiv.appendChild(overlayTable);
	document.body.appendChild(overlayDiv);
	overlayDiv.style.display = 'block';//infine mostro la finestra sovrapposta
}

//mostro i risultati in base alla keyword
function ProductResult(gestore, listaProdotti) {
	this.listaProdotti = listaProdotti;
	this.listaDettagli;
	this.listaInfoProd;
	this.update = function(product) {
		var tabellaProdotto, riga1, riga2, riga3, cellaImmagine, immagine, cellaNome, cellaPrezzo, cellaId,
			cellaDet, cellaInfo, table, thead, trHeader, th1, th2, th3, th4, th5, th6, th7, tbody, trBody,
			td1, td2, td3, td4, td5, td6, td7, td8, form, label1, input1, submitInput, innerTable, innerTbody,
			innerTr, innerTh1, innerTd1, innerTh2, innerTd2, innerTh3, innerTd3, label2, input2, label4, input4,
			label5, input5, label6, input6, label7, input7, overlayDiv;
		var mostra = (e, cart3) => {//quando clicca sull'id di un prodotto se le 2 tabelle son nascoste le mostro, sennò le nascondo
			e.preventDefault();
			if (document.getElementById("cellaInfo").hidden === true && document.getElementById("cellaDet").hidden === true) {
				var idSelected = e.target.getAttribute("id_product");
				var photo = e.target.getAttribute("photo");
				var json1 = { idProduct: idSelected, ProdPhoto: photo };
				
				makeCallJSON("POST", "SearchInfoProd", JSON.stringify(json1), function(req) {//mostro i dettagli del prodotto
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var product = JSON.parse(req.responseText);
							cellaInfo.textContent = product.name + "\n" + product.description + "\n" + product.merchandise_category;
							document.getElementById("cellaInfo").hidden = false;
						} else {
							if (message == "") message = "Error";
							alert(message);
						}
					}
				});

				var json2 = { idProduct: idSelected, cart3: cart3 };
				makeCallJSON("POST", "SearchDetails", JSON.stringify(json2), function(req) {//mostro i dettagli dei fornitori di quel prodotto
					if (req.readyState == 4) {
						var message = req.responseText;
						if (req.status == 200) {
							var supplierDetailsList = JSON.parse(req.responseText);
							var a = document.getElementById("tableDetails");
							if (a) { a.remove(); }
							table = document.createElement("table");
							table.id = "tableDetails";
							thead = document.createElement("thead");
							trHeader = document.createElement("tr");
							th1 = document.createElement("th");
							th1.textContent = "Nome fornitore";
							th2 = document.createElement("th");
							th2.innerHTML = "Numero stelle<br> [da 1 a 5]";
							th3 = document.createElement("th");
							th3.innerHTML = "Importo minimo <br>per la spedizione gratuita";
							th4 = document.createElement("th");
							th4.textContent = "Prezzo unitario";
							th5 = document.createElement("th");
							th5.innerHTML = "Numero dei prodotti di quel<br> fornitore nel carrello";
							th6 = document.createElement("th");
							th6.innerHTML = "Prezzo totale dei prodotti<br> di quel fornitore<br>già nel carrello";
							th7 = document.createElement("th");
							th7.textContent = "Quantità";
							trHeader.appendChild(th1);
							trHeader.appendChild(th2);
							trHeader.appendChild(th3);
							trHeader.appendChild(th4);
							trHeader.appendChild(th5);
							trHeader.appendChild(th6);
							trHeader.appendChild(th7);
							thead.appendChild(trHeader);
							tbody = document.createElement("tbody");
							overlayDiv = document.createElement("div");
							overlayDiv.className = "overlay";
							overlayDiv.id = "div";
							overlayDiv.style.display = 'none';//nascondo la finestra sovrapposta appena la creo
							for (var i = 0; i < supplierDetailsList.length; i++) {
								var map = supplierDetailsList[i];
								trBody = document.createElement("tr");
								td1 = document.createElement("td");
								td1.textContent = map.supplier.name;
								td2 = document.createElement("td");
								td2.textContent = map.supplier.num_stars;
								td3 = document.createElement("td");
								td3.textContent = map.supplier.amount;
								td4 = document.createElement("td");
								td4.textContent = map.unitPrice;
								td5 = document.createElement("td");
								td5.textContent = map.supplier.numProdsInCart;
								
								//mi salvo tutto ciò che devo mostare nella fiestra sovrapposta
								td5.setAttribute("mapsupplieroverlaylist", JSON.stringify(map.supplier.overlaylist));
								
								td5.addEventListener('mouseover', function() {
									//e lo passo alla funzione showOverlay che chiamo
									var mapsupplieroverlaylist = JSON.parse(this.getAttribute("mapsupplieroverlaylist"));
									showOverlay(mapsupplieroverlaylist, overlayDiv);
								});
								td5.addEventListener('mouseleave', function() {
									var element = document.getElementById('div');
									if (element) { overlayDiv.style.display = 'none'; //nascondo la finestra sovrapposta 
									}
								});
								td6 = document.createElement("td");
								td6.textContent = map.supplier.priceProdsInCart;
								td7 = document.createElement("td");
								form = document.createElement("form");
								label1 = document.createElement("label");
								label1.htmlFor = "numProdDaMettere";
								input1 = document.createElement("input");
								input1.type = "number";
								input1.id = "numProdDaMettere";
								input1.placeholder = "quantità";
								input1.name = "numProdDaMettere";
								input1.required = true;
								label2 = document.createElement('label');
								label2.htmlFor = 'idSelected';
								input2 = document.createElement('input');
								input2.type = 'hidden';
								input2.id = 'idSelected';
								input2.name = 'idSelected';
								input2.value = idSelected;
								label4 = document.createElement('label');
								label4.htmlFor = 'supplier';
								input4 = document.createElement('input');
								input4.type = 'hidden';
								input4.id = 'supplier';
								input4.name = 'supplier';
								input4.value = map.supplier.name;
								label5 = document.createElement('label');
								label5.htmlFor = 'idsupplier';
								input5 = document.createElement('input');
								input5.type = 'hidden';
								input5.id = 'idsupplier';
								input5.name = 'idsupplier';
								input5.value = map.supplier.id_supplier;
								label6 = document.createElement('label');
								label6.htmlFor = 'unitPrice';
								input6 = document.createElement('input');
								input6.type = 'hidden';
								input6.id = 'unitPrice';
								input6.name = 'unitPrice';
								input6.value = map.unitPrice;
								label7 = document.createElement('label');
								label7.htmlFor = 'productPhoto';
								input7 = document.createElement('input');
								input7.type = 'hidden';
								input7.id = 'productPhoto';
								input7.name = 'productPhoto';
								input7.value = photo;
								submitInput = document.createElement("input");//è il bottone per inserire nel carrello
								submitInput.id = "submitInput";
								submitInput.type = "button";
								submitInput.className = "metti-button";
								submitInput.value = "Inserisci nel carrello";
								submitInput.setAttribute("idsupplier", map.supplier.id_supplier);
								submitInput.setAttribute("suppliername", map.supplier.name);
								submitInput.setAttribute("unitPrice", map.unitPrice);
								(function(input1) {
									submitInput.addEventListener("click", function(e) {
										e.preventDefault();
										var idsupplier = e.target.getAttribute("idsupplier");
										var suppliername = e.target.getAttribute("suppliername");
										var unitPrice = e.target.getAttribute("unitPrice");
										var quantity = parseInt(input1.value);
										var json3 = {
											idSelected: idSelected, idsupplier: idsupplier, quantity: quantity,
											suppliername: suppliername, productPhoto: photo, unitPrice: unitPrice
										};
										var element = document.getElementById('tabellacart');
										if (element) { element.remove(); }
										makeCallJSON("POST", "CaricaCarrello", JSON.stringify(json3), function(req) {//carico il carrello
											if (req.readyState == 4) {
												var message = req.responseText;
												if (req.status == 200) {
													try {
														var element = document.getElementById('tabellacart');//pulisco il carrello
														if (element) { element.remove(); }
														var cart2 = JSON.parse(req.responseText);
														populatecart(gestore, cart2);//popolo bene il carrello
														gestore.visCarrello();//gli mostro il carrello caricato
													} catch (error) {
														console.log('Error parsing JSON:', error);
														message = "Error";
														alert(message);
													}
												} else {
													if (message == "") message = "Error";
													alert(message);
												}
											}
										});
									}
									);
								})(input1);
								form.appendChild(input1);
								form.appendChild(label2);
								form.appendChild(input2);
								form.appendChild(label4);
								form.appendChild(input4);
								form.appendChild(label5);
								form.appendChild(input5);
								form.appendChild(label6);
								form.appendChild(input6);
								form.appendChild(label7);
								form.appendChild(input7);
								form.appendChild(submitInput);
								td7.appendChild(form);
								td8 = document.createElement("td");
								innerTable = document.createElement("table");
								innerTbody = document.createElement("tbody");
								for (var j = 0; j < map.ranges.length; j++) {
									innerTr = document.createElement("tr");
									innerTh1 = document.createElement("th");
									innerTh1.textContent = "Da:";
									innerTd1 = document.createElement("td");
									innerTd1.textContent = map.ranges[j].num_min_articles;
									innerTh2 = document.createElement("th");
									innerTh2.textContent = "A:";
									innerTd2 = document.createElement("td");
									innerTd2.textContent = map.ranges[j].num_max_articles;
									innerTh3 = document.createElement("th");
									innerTh3.textContent = "Spese spedizione:";
									innerTd3 = document.createElement("td");
									innerTd3.textContent = map.ranges[j].shipping_costs;
									innerTr.appendChild(innerTh1);
									innerTr.appendChild(innerTd1);
									innerTr.appendChild(innerTh2);
									innerTr.appendChild(innerTd2);
									innerTr.appendChild(innerTh3);
									innerTr.appendChild(innerTd3);
									innerTbody.appendChild(innerTr);
								}
								innerTable.appendChild(innerTbody);
								td8.appendChild(innerTable);
								trBody.appendChild(td1);
								trBody.appendChild(td2);
								trBody.appendChild(td3);
								trBody.appendChild(td4);
								trBody.appendChild(td5);
								trBody.appendChild(td6);
								trBody.appendChild(td7);
								trBody.appendChild(td8);
								tbody.appendChild(trBody);
							}
							table.appendChild(thead);
							table.appendChild(tbody);
							cellaDet.appendChild(table);
							document.getElementById("cellaDet").hidden = false;
						} else {
							if (message == "") message = "Error";
							alert(message);
						}
					}
				});
			}
			else {
				document.getElementById("cellaDet").hidden = true;
				document.getElementById("cellaInfo").hidden = true;
			}
		}
		//creo tabella totale dei prodotti in listResults
		tabellaProdotto = document.createElement("table");
		tabellaProdotto.className = "tabellaProdotto";
		tabellaProdotto.id = "tabellaProdotto";
		this.listaProdotti.appendChild(tabellaProdotto);
		riga1 = document.createElement("tr");
		tabellaProdotto.appendChild(riga1);
		riga2 = document.createElement("tr");
		tabellaProdotto.appendChild(riga2);
		riga3 = document.createElement("tr");
		riga3.colSpan = "4";
		tabellaProdotto.appendChild(riga3);
		cellaImmagine = document.createElement("td");
		cellaImmagine.rowSpan = "2";
		riga1.appendChild(cellaImmagine);
		immagine = document.createElement("img");
		immagine.src = "images/" + product.photo;
		immagine.className = "immagineGrande";
		cellaImmagine.appendChild(immagine);
		cellaNome = document.createElement("td");
		cellaNome.textContent = product.name;
		cellaNome.className = "testoProdotto";
		riga1.appendChild(cellaNome);
		cellaPrezzo = document.createElement("td");
		cellaPrezzo.textContent = "prezzo in Euro: " + product.min_unit_price;
		riga1.appendChild(cellaPrezzo);
		cellaId = document.createElement("td");
		cellaId.textContent = "id: " + product.id_product;
		cellaId.setAttribute("id_product", product.id_product);
		cellaId.setAttribute("photo", product.photo);
		cellaId.classList.add("bigger-id");
		cellaId.addEventListener("click", function(e) {
			var cart3 = JSON.parse(sessionStorage.getItem("cart"));
			mostra(e, cart3);
		});
		riga1.appendChild(cellaId);
		cellaInfo = document.createElement("td");
		cellaInfo.id = "cellaInfo";
		riga2.appendChild(cellaInfo);
		document.getElementById("cellaInfo").hidden = true;
		cellaDet = document.createElement("td");
		cellaDet.id = "cellaDet";
		riga3.appendChild(cellaDet);
		document.getElementById("cellaDet").hidden = true;
	};
}

function ProductSuggested(gestore, listaProdotti) {
	this.listaProdotti = listaProdotti;
	this.update = function(product) {
		var tabellaProdotto, riga1, riga2, riga3, cellaImmagine, immagine, cellaCategoria, cellaNome, cellaDescrizione;
		tabellaProdotto = document.createElement("table");
		tabellaProdotto.className = "tabellaProdotto";
		tabellaProdotto.id = "tabellaProdotto";
		this.listaProdotti.appendChild(tabellaProdotto);
		riga1 = document.createElement("tr");
		tabellaProdotto.appendChild(riga1);
		riga2 = document.createElement("tr");
		tabellaProdotto.appendChild(riga2);
		riga3 = document.createElement("tr");
		riga3.colSpan = "4";
		tabellaProdotto.appendChild(riga3);
		cellaImmagine = document.createElement("td");
		cellaImmagine.rowSpan = "2";
		riga1.appendChild(cellaImmagine);
		immagine = document.createElement("img");
		immagine.src = "images/" + product.photo;
		immagine.className = "immagineGrande";
		immagine.setAttribute("id_product", product.id_product);
		cellaImmagine.appendChild(immagine);
		cellaNome = document.createElement("td");
		cellaNome.textContent = product.name;
		cellaNome.className = "testoProdotto";
		cellaNome.setAttribute("id_product", product.id_product);
		riga1.appendChild(cellaNome);
		cellaCategoria = document.createElement("td");
		cellaCategoria.textContent = product.merchandise_category;
		riga1.appendChild(cellaCategoria);
		cellaDescrizione = document.createElement("td");
		cellaDescrizione.textContent = product.description;
		cellaDescrizione.colSpan = "3";
		riga1.appendChild(cellaDescrizione);
	};
}

function Order(gestore, listaOrdini) {
	this.listaOrdini = listaOrdini;
	this.update = function(ordine) {
		var tabellaOrdine, riga1, riga2, riga3, cellaAcquisto, cellaLista, listaProdotti;
		tabellaOrdine = document.createElement("table");
		tabellaOrdine.className = "tabellaOrdine";
		this.listaOrdini.appendChild(tabellaOrdine);
		riga1 = document.createElement("tr");
		tabellaOrdine.appendChild(riga1);
		riga2 = document.createElement("tr");
		tabellaOrdine.appendChild(riga2);
		riga3 = document.createElement("tr");
		tabellaOrdine.appendChild(riga3);
		cellaAcquisto = document.createElement("td");
		cellaAcquisto.textContent = "FORNITORE: " + ordine.name_supplier + "\nprezzo tot prodotti+spese di spedizione in Euro: " + ordine.price_tot_products +
			"\nINFORMAZIONI SPEDIZIONE\n" + "Indirizzo: " + ordine.address_buyer + "\nData: " + ordine.delivery_date;
		riga2.appendChild(cellaAcquisto);
		cellaLista = document.createElement("td");
		riga3.appendChild(cellaLista);
		listaProdotti = document.createElement("ul");
		cellaLista.appendChild(listaProdotti);
		ordine.productsphotos.forEach((p) => {
			var entryProdotto = document.createElement("li");
			var image = document.createElement("img");
			image.src = "images/" + p;
			image.className = "immaginePiccolissima";
			entryProdotto.appendChild(image);
			listaProdotti.appendChild(entryProdotto);
		});
	};
}

/*Lista di oggetti generica:raggruppo più oggetti in una lista per una visualizzazione dinamica

*   gestore :Gestore della pagina che contiene la lista
*   Oggetto :Riferimento al costruttore dell'oggetto di cui si vuole creare la lista
*   divLista :Nodo che contierrà la lista
*   fCaricamento :Funzione di caricamento della lista
*   optOggetto :Parametri opzionali per l'oggetto
*/
function ListaOggetti(gestore, Oggetto, divLista, fCaricamento, optOggetto) {
	this.divLista = divLista;
	this.carica = fCaricamento;
	this.update = function(oggetti) {
		this.show();//rendo visibile la divLista
		this.divLista.innerHTML = ""; //svuota la lista che le passo con divLista
		var self = this;//mi salvo l'oggetto su cui viene chiamato update in self per usarlo nella callback update
		//Inizializza ogni oggetto e chiama la funzione update su di esso
		oggetti.forEach((o) => {
			var p = new Oggetto(gestore, self.divLista, optOggetto);
			p.update(o);
		});
	};
	this.show = () => {
		this.divLista.hidden = false;
	};
	this.hide = () => {
		this.divLista.hidden = true;
	};
	this.isHidden = () => {
		return this.divLista.hidden;//restituisce lo stato di visibilità di divLista
	};
}