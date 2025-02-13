//document è la pagina html 

(function() {	// Nasconde le variabli dallo scope globale
		document.getElementById("bottoneLogin").addEventListener("click", (e) => {
			var form = e.target.closest("form");//cerco il form più vicino al bottone cliccato (e.target) col metodo closest
			var messaggio = document.getElementById("erroreLogin");//It retrieves the error message element with the id "erroreLogin".
			if (form.checkValidity()) {//checks if the form is valid 
			makeCall("POST", "DefaultGoToLoginPage", new FormData(form), messaggio, function(req) {
					var buyer = JSON.parse(req.responseText);
					window.sessionStorage.setItem("buyer", JSON.stringify(buyer));
					window.location.href = "Home.html";//it redirects the user to the "Home.html" page using window.location.href. 
				}, null, true);
			}
			else
				form.reportValidity();
		});
	}
)();