document.addEventListener("DOMContentLoaded", function () {
    fetch("/api-get-header-admin")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header-main-content").innerHTML = data;
            
        });
});







