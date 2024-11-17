document.addEventListener("DOMContentLoaded", function () {
    fetch("/api-get-header-employee")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
            
        });
});







