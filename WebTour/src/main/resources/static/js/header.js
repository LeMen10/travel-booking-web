document.addEventListener("DOMContentLoaded", function() {
	fetch("/api-get-header")
		.then(response => response.text())
		.then(data => {
			document.getElementById("header").innerHTML = data;
			const searchBox = document.getElementById('searchBox');
			const emptyBox = document.getElementById('emptyBox');
			const searchBtn = document.querySelector('.btn-search');
			const searchIcon = document.getElementById('searchIcon');

			const toggleSearchBox = () => {
				if (searchBox.classList.contains('active')) {
					searchBox.classList.remove('active');
					setTimeout(() => {
						searchBox.style.display = 'none';
					}, 300);
				} else {
					// Nếu đang ẩn, hiển thị
					searchBox.style.display = 'flex';
					setTimeout(() => {
						searchBox.classList.add('active');
					}, 10);
				}
			};
			searchBtn.addEventListener('click', toggleSearchBox);
			emptyBox.addEventListener('click', () => {
				searchBox.classList.remove('active');
				setTimeout(() => {
					searchBox.style.display = 'none';
				}, 300);
			});

			const searchInput = document.getElementById('searchInput');
			searchIcon.addEventListener('click', redirectToSearch);
			searchInput.addEventListener('keydown', function(event) {
				if (event.key === 'Enter') {
					redirectToSearch();
				}
			});
			function redirectToSearch() {
				const query = searchInput.value.trim();
				if (query) {
					window.location.href = `http://localhost:8080/search?s=${encodeURIComponent(query)}`;
				}
			}
		});

	fetch("/api-get-footer")
		.then(response => response.text())
		.then(data => {
			document.getElementById("footer").innerHTML = data;
		});
	getUser();
});

async function getUser() {
	let userId = sessionStorage.getItem("userId");

	if (userId) {
		// URL để gọi API
		const url = `http://localhost:8080/api-get-user?userId=${userId}`;
		try {
			// Thực hiện request tới API
			const request = new Request(url, {
				method: "GET",
				headers: {
					"Content-Type": "application/json",
				}
			});

			// Gọi API và đợi kết quả trả về
			const response = await fetch(request);

			// Kiểm tra nếu có lỗi khi gọi API
			if (!response.ok) {
				console.log("Error with API response", response);
				return;
			}

			// Lấy dữ liệu user từ API
			const user = await response.json();
			console.log(user);
			var userName = user.fullName;
			console.log(userName);

			var login = document.getElementById("login");
			login.innerHTML = userName;
			login.href = "/account?userId=" + userId;
			var register = document.getElementById("register");
			register.innerHTML = 'Đăng xuất';
			register.href = "/home";
			register.addEventListener('click', function(event) {
				sessionStorage.removeItem('userId');

			});


		} catch (error) {
			console.error("Error fetching user data:", error);
		}
	} else {
		console.log("userId is null or empty");
	}
}







