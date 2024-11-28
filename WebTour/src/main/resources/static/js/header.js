document.addEventListener("DOMContentLoaded", function () {

	fetch("/api-get-header")
		.then(response => response.text())
		.then(data => {
			document.getElementById("header").innerHTML = data;
			//nagvigateHeader();
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
			searchInput.addEventListener('keydown', function (event) {
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

	const user = await getProfile();
	if (user) {
		var userName = user.user.fullName;

		var login = document.getElementById("login");
		login.innerHTML = userName;
		login.href = "/account?userId=" + user.user.user_id;
		var register = document.getElementById("register");
		register.innerHTML = 'Đăng xuất';
		register.href = "/home";
		register.addEventListener('click', async function (event) {
			await Logout();

		});
	}
}

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

function closeLoading() {
	document.getElementById('loading-content').classList.add("deactivate");
}

function openLoading() {
	document.getElementById('loading-content').classList.remove("deactivate");
}


/*async function nagvigateHeader() {
	const navigateElement = document.querySelector('.navigate');
	console.log(navigateElement);  // Kiểm tra xem navigateElement có tồn tại không
	const currentPageElement = document.getElementById('current-page');

	// Kiểm tra phần tử đã tồn tại trước khi tiếp tục
	if (!navigateElement || !currentPageElement) {
		console.error('Các phần tử .navigate hoặc #current-page không tồn tại.');
		return; // Dừng hàm nếu phần tử không tồn tại
	}

	// Lấy tất cả các phần tử trong lịch sử URL
	const pathParts = window.location.pathname.split('/').filter(Boolean);

	// Lịch sử điều hướng của người dùng (bao gồm trang chủ và các trang đã truy cập)
	const navigationHistory = ['Trang chủ', ...pathParts];  // Mở rộng ra từ trang chủ

	console.log('Navigation History:', navigationHistory);

	// Xóa nội dung thanh điều hướng cũ (chỉ xóa phần tử con)
	navigateElement.innerHTML = '';



	// Dấu phân cách ">"
	const separator = document.createElement('span');
	separator.innerHTML = ' &gt; ';
	navigateElement.appendChild(separator);

	// Duyệt qua các phần tử trong lịch sử điều hướng
	navigationHistory.forEach((part, index) => {
		// Tạo liên kết cho mỗi trang
		const pageLink = document.createElement('a');
		if (index === navigationHistory.length - 1) {
			// Nếu đây là phần tử cuối cùng (trang hiện tại), không tạo liên kết mà chỉ hiển thị tên trang
			pageLink.textContent = part;
			pageLink.classList.add('current');
		} else {
			// Tạo liên kết cho các trang trước
			pageLink.href = '/' + navigationHistory.slice(1, index + 1).join('/');  // Tạo đường dẫn từ các phần tử lịch sử
			pageLink.textContent = part;
		}

		// Thêm liên kết vào thanh điều hướng
		navigateElement.appendChild(pageLink);

		// Thêm dấu phân cách ">" nếu không phải phần tử cuối cùng
		if (index < navigationHistory.length - 1) {
			const separator = document.createElement('span');
			separator.innerHTML = ' &gt; ';
			navigateElement.appendChild(separator);
		}
	});

	// Cập nhật phần tử hiện tại của thanh điều hướng
	currentPageElement.textContent = navigationHistory[navigationHistory.length - 1];
	console.log('Navigation History:', currentPageElement);
}*/









