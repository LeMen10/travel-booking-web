document.addEventListener("DOMContentLoaded", function () {
    fetch("/api-get-notification")
        .then(response => response.text())
        .then(data => {
            document.getElementById("notification").innerHTML = data;
			
         })
});


function openDialogSuccess(message) {
	console.log("success");
    const formOverlay = document.getElementById("notification");
    const dialogMessSuccess = document.getElementById("success-box");
	const dialogMessError = document.getElementById("error-box");
	const messContent = document.getElementById("mess-success");

    messContent.innerText = message;

    
    dialogMessError.classList.add('deactivate');
	dialogMessSuccess.classList.remove('deactivate');

    formOverlay.style.display = "block";
}

function openDialogError(message) {
	console.log("Error");
    const formOverlay = document.getElementById("notification");
    const dialogMessSuccess = document.getElementById("success-box");
	const dialogMessError = document.getElementById("error-box");
	const messContent = document.getElementById("mess-error");

    messContent.innerText = message;

    
    dialogMessError.classList.remove('deactivate');
	dialogMessSuccess.classList.add('deactivate');

    formOverlay.style.display = "block";
}

function closeDialog(isSuccess) {
    const formOverlay = document.getElementById("notification");
    formOverlay.style.display = "none";
}
