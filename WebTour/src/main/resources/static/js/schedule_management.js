document.addEventListener('DOMContentLoaded', function() {

	const searchInput = document.getElementById("search-input");
	const searchIcon = document.getElementById("search-icon");

	// Lắng nghe sự kiện nhấn vào icon kính lúp
	searchIcon.addEventListener("click", function() {
		const tourName = searchInput.value.trim();
		if (tourName !== "") {
			searchTours(tourName, 0); // tìm kiếm từ trang đầu 
		}
		else {
				console.log("Ô tìm kiếm rỗng, hiển thị tất cả tour");
				showPerPage(0); // Hiển thị tất cả tour
			}
	});
})


async function showPerPage(page) {
	const size = 5;
	const url = `http://localhost:8080/admin/schedule_management/data?page=${page}&size=${size}`; // Endpoint mới
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	});

	try {
		const response = await fetch(request);
		console.log("Phản hồi từ server:", response);

		if (!response.ok) {
			console.log("Có lỗi xảy ra trong fetchTours:", response);
			return null;
		} else {
			const data = await response.json();
			const tourTable = document.getElementById("tour-table");
			tourTable.innerHTML = ""; // Xóa dữ liệu hiện tại

			data.content.forEach(tour => {
				const row = document.createElement("tr");
				row.innerHTML = `
                    <td>${tour.tourId}</td>
                    <td>${tour.tourName}</td>
                    <td>${tour.transport}</td>
                    <td>${tour.departure}</td>
                    <td>${tour.destination}</td>
                    <td>${new Date(tour.startDate).toLocaleDateString('vi-VN')}</td>
                    <td>${new Date(tour.endDate).toLocaleDateString('vi-VN')}</td>
                    <td>${tour.price}</td>
                    <td>${tour.status}</td>
                `;
				tourTable.appendChild(row);
			});

			// Cập nhật phân trang
			updatePerPage(data.totalPages, page);
			updateSearchTour(data.totalPages, page, showPerPage); // Cập nhật phân trang cho hiển thị
		}
	} catch (error) {
		console.error("Lỗi trong fetchTours:", error);
	}
}

function updatePerPage(totalPages, currentPage) {
	const paginationContainer = document.getElementById("pagination");
	paginationContainer.innerHTML = ""; // Xóa nội dung hiện tại

	// Previous Button
	if (currentPage > 0) {
		const previousButton = document.createElement('li');
		previousButton.innerHTML = `<button onclick="fetchTours(${currentPage - 1})">
                                        <i class="fas fa-chevron-left icon"></i>
                                    </button>`;
		paginationContainer.appendChild(previousButton);
	}

	// Page Buttons
	for (let i = 1; i <= totalPages; i++) {
		const pageButton = document.createElement('li');
		pageButton.innerHTML = `<button  onclick="showPerPage(${i - 1})" 
                                    class="pre ${currentPage === i - 1 ? 'current' : ''}">
                                    <span>${i}</span>
                                </button>`;
		paginationContainer.appendChild(pageButton);
	}

	// Next Button
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement('li');
		nextButton.innerHTML = `<button onclick="showPerPage(${currentPage + 1})">
                                    <i class="fas fa-chevron-right icon"></i>
                                </button>`;
		paginationContainer.appendChild(nextButton);
	}
}

//tìm kiếm 
async function searchTours(tourName, page) {
	const size = 5;
	
	const url = `/admin/schedule_management-search/search?tourName=${encodeURIComponent(tourName)}&page=${page}&size=${size}`;
	try {
		const response = await fetch(url);
		if (!response.ok) {
			console.error("Có lỗi xảy ra khi tìm kiếm tour:", response);
			return;
		}
		const data = await response.json();
		// Cập nhật bảng tour với dữ liệu tìm kiếm
		const tourTable = document.getElementById("tour-table");
		tourTable.innerHTML = ""; // Xóa dữ liệu hiện tại

		data.content.forEach(tour => {
			const row = document.createElement("tr");
			row.innerHTML = `
		                <td>${tour.tourId}</td>
		                <td>${tour.tourName}</td>
		                <td>${tour.transport}</td>
		                <td>${tour.departure}</td>
		                <td>${tour.destination}</td>
		                <td>${new Date(tour.startDate).toLocaleDateString('vi-VN')}</td>
		                <td>${new Date(tour.endDate).toLocaleDateString('vi-VN')}</td>
		                <td>${tour.price}</td>
		                <td>${tour.status}</td>
		            `;
			tourTable.appendChild(row);
		});

		// Cập nhật phần phân trang với dữ liệu tìm kiếm
		updateSearchTour(data.totalPages, page, (newPage) => searchTours(tourName, newPage));
	} catch (error) {
		console.error("Lỗi trong searchTours:", error);
	}
}
function updateSearchTour(totalPages, currentPage, onPageClick) {
	const paginationContainer = document.getElementById("pagination");
	paginationContainer.innerHTML = "";

	// Nút Previous
	if (currentPage > 0) {
		const prevButton = document.createElement("button");
		prevButton.textContent = "Previous";
		prevButton.classList.add("bt-pre");
		prevButton.onclick = () => onPageClick(currentPage - 1);
		paginationContainer.appendChild(prevButton);
	}

	// Nút trang
	for (let i = 0; i < totalPages; i++) {
		const pageButton = document.createElement("button");
		pageButton.textContent = i + 1;
		pageButton.className = i === currentPage ? "current" : "";
		pageButton.onclick = () => onPageClick(i);
		paginationContainer.appendChild(pageButton);
	}

	// Nút Next
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement("button");
		nextButton.textContent = "Next";
		nextButton.classList.add("bt-next");
		nextButton.onclick = () => onPageClick(currentPage + 1);
		paginationContainer.appendChild(nextButton);
	}
}





