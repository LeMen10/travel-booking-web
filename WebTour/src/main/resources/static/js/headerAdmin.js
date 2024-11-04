document.addEventListener("DOMContentLoaded", function() {
	fetch("/api-get-header-admin")
		.then(response => response.text())
		.then(data => {
			document.querySelector(".header").innerHTML = data;

		});

	fetch("/api-get-side-bar-admin")
		.then(response => response.text())
		.then(data => {
			document.querySelector(".sidebar").innerHTML = data;

		});
});







