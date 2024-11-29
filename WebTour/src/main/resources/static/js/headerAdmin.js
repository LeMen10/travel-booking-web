document.addEventListener("DOMContentLoaded", function() {
	fetch("/api-get-header-admin")
		.then(response => response.text())
		.then(async(data) => {
			document.querySelector(".header").innerHTML = data;
			const userData = await getProfile();
			if(userData == null) window.location.href = "/";
			else{
				document.getElementById("user-name-admin").innerHTML = userData.user.fullName;
			}
		});

	fetch("/api-get-side-bar-admin")
		.then(response => response.text())
		.then(data => {
			document.querySelector(".sidebar").innerHTML = data;
			

		});
});

async function getProfile() {
	const response = await fetch('http://localhost:8080/profile', {
		method: 'GET',
		credentials: 'include'
	});

	if (response.ok) {
		const profile = await response.json();
		console.log(profile);
		return profile;
	} else {
		console.error('Not logged in');
	}
}

async function Logout() {
	const response = await fetch('http://localhost:8080/logout', {
		method: 'POST',
		credentials: 'include'
	});

	if (response.ok) {
		console.log("logout success");
	} else {
		console.error('Not logged in');
	}
}



