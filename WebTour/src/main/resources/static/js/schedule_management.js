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

//hiện tất cả tour 
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
                    <td class="td-table">${tour.tourId}</td>
                    <td class="td-table tour-name">${tour.tourName}</td>
                    <td class="td-table">${tour.transport}</td>
                    <td class="td-table">${tour.departure}</td>
                    <td class="td-table">${tour.destination}</td>
                    <td class="td-table">${new Date(tour.startDate).toLocaleDateString('vi-VN')}</td>
                    <td class="td-table">${new Date(tour.endDate).toLocaleDateString('vi-VN')}</td>
                    <td class="td-table">${tour.price}</td>
                    <td class="td-table">
						<button type="button" class="btn-edit btn-primary" data-tour-id="${tour.tourId}">
						                Edit
			            </button>
					</td>
                `;
				//tạo sự kiện khi ấn nút edit khi chuyển đến các trang khác của phân trang
				const editButton = row.querySelector(".btn-edit");
				editButton.onclick = function() {
					const tourId = editButton.getAttribute("data-tour-id");
					window.location.href = `/api-edit-schedule/${tourId}`;
				};
				
				tourTable.appendChild(row);
			});

			// Cập nhật phân trang
			//updatePerPage(data.totalPages, page);
			updateSearchTour(data.totalPages, page, showPerPage); // Cập nhật phân trang cho hiển thị
		}
	} catch (error) {
		console.error("Lỗi trong fetchTours:", error);
	}
}

// cập nhật lại phân trang khi chuyển trang ở hiện tất cả tour
function updatePerPage(totalPages, currentPage) {
	const paginationContainer = document.getElementById("pagination");
	paginationContainer.innerHTML = ""; // Xóa nội dung hiện tại

	// Previous Button
	if (currentPage > 0) {
		const previousButton = document.createElement('li');
		previousButton.innerHTML = `<button >
                                        <i class="fas fa-chevron-left icon"></i>
                                    </button>`;
		paginationContainer.appendChild(previousButton);
	}

	// First Page Button
	if (currentPage > 2) {
		const firstButton = document.createElement('li');
		firstButton.innerHTML = `<button ">1</button>`;
		paginationContainer.appendChild(firstButton);

		// Dots before the current range
		const dotsBefore = document.createElement('li');
		dotsBefore.innerHTML = `<span>...</span>`;
		paginationContainer.appendChild(dotsBefore);
	}

	// Middle Page Buttons (currentPage - 1 to currentPage + 1)
	for (let i = Math.max(0, currentPage - 1); i <= Math.min(totalPages - 1, currentPage + 1); i++) {
		const pageButton = document.createElement('li');
		pageButton.innerHTML = `<button  
                                    class="${currentPage === i ? 'current' : ''}">
                                    ${i + 1}
                                </button>`;
		paginationContainer.appendChild(pageButton);
	}

	// Last Page Button
	if (currentPage < totalPages - 3) {
		// Dots after the current range
		const dotsAfter = document.createElement('li');
		dotsAfter.innerHTML = `<span>...</span>`;
		paginationContainer.appendChild(dotsAfter);

		const lastButton = document.createElement('li');
		lastButton.innerHTML = `<button >${totalPages}</button>`;
		paginationContainer.appendChild(lastButton);
	}

	// Next Button
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement('li');
		nextButton.innerHTML = `<button >
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
		                <td class="td-table tour-name">${tour.tourName}</td>
		                <td class="td-table">${tour.transport}</td>
		                <td class="td-table">${tour.departure}</td>
		                <td class="td-table">${tour.destination}</td>
		                <td class="td-table">${new Date(tour.startDate).toLocaleDateString('vi-VN')}</td>
		                <td class="td-table">${new Date(tour.endDate).toLocaleDateString('vi-VN')}</td>
		                <td class="td-table">${tour.price}</td>
		                <td class="td-table">
							<button type="button" class="btn-edit btn-primary" 
					            onclick="window.location.href='/api-edit-schedule/${tour.tourId}'">
					            Edit
					        </button>
						</td>
		            `;
			tourTable.appendChild(row);
		});

		// Cập nhật phần phân trang với dữ liệu tìm kiếm
		updateSearchTour(data.totalPages, page, (newPage) => searchTours(tourName, newPage));
	} catch (error) {
		console.error("Lỗi trong searchTours:", error);
	}
}

//cập nhật lại phân trang khi tìm kiếm
function updateSearchTour(totalPages, currentPage, onPageClick) {
	const paginationContainer = document.getElementById("pagination");
	paginationContainer.innerHTML = "";

	// Nút Previous
	if (currentPage > 0) {
		const prevButton = document.createElement("button");
		prevButton.innerHTML = '<i class="fas fa-chevron-left icon"></i>';
		prevButton.classList.add("bt-pre");
		prevButton.onclick = () => onPageClick(currentPage - 1);
		paginationContainer.appendChild(prevButton);
	}

	// Nút trang đầu tiên
	if (currentPage > 2) {
		const firstButton = document.createElement("button");
		firstButton.textContent = "1";
		firstButton.onclick = () => onPageClick(0);
		paginationContainer.appendChild(firstButton);

		// Dấu ...
		if (currentPage > 3) {
			const dots = document.createElement("span");
			dots.textContent = "...";
			paginationContainer.appendChild(dots);
		}
	}

	// Nút trang ở giữa (trang hiện tại +/- 1)
	for (let i = Math.max(0, currentPage - 1); i <= Math.min(totalPages - 1, currentPage + 1); i++) {
		const pageButton = document.createElement("button");
		pageButton.textContent = i + 1;
		pageButton.className = i === currentPage ? "current" : "";
		pageButton.onclick = () => onPageClick(i);
		paginationContainer.appendChild(pageButton);
	}

	// Nút trang cuối cùng
	if (currentPage < totalPages - 3) {
		if (currentPage < totalPages - 4) {
			const dots = document.createElement("span");
			dots.textContent = "...";
			paginationContainer.appendChild(dots);
		}

		const lastButton = document.createElement("button");
		lastButton.textContent = totalPages;
		lastButton.onclick = () => onPageClick(totalPages - 1);
		paginationContainer.appendChild(lastButton);
	}

	// Nút Next
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement("button");
		nextButton.innerHTML = '<i class="fas fa-chevron-right icon"></i>';
		nextButton.classList.add("bt-next");
		nextButton.onclick = () => onPageClick(currentPage + 1);
		paginationContainer.appendChild(nextButton);
	}
}






