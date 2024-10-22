document.addEventListener('DOMContentLoaded', function() {
	document.getElementById("bt-continuteShop").addEventListener("click", backHomeForm);
})
async function backHomeForm(){
	window.location.href = '/home';
}