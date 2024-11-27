// Lấy tất cả các phần tử có chứa tour-duration
document.addEventListener('DOMContentLoaded', function() {
	fomatDay();
	fomatPrice();
	loadPage(0);
})
function fomatDay() {
	const tourDurations = document.querySelectorAll('.tour-duration');

	tourDurations.forEach(function(element) {
		// Lấy giá trị ngày bắt đầu và ngày kết thúc từ data attributes
		const startDate = new Date(element.getAttribute('data-start-date'));
		const endDate = new Date(element.getAttribute('data-end-date'));

		// Tính khoảng thời gian giữa ngày kết thúc và ngày bắt đầu (đơn vị là milliseconds)
		const timeDifference = startDate - endDate;

		// Tính số ngày bằng cách chia milliseconds cho số milliseconds trong 1 ngày
		const days = timeDifference / (1000 * 60 * 60 * 24) + 1;
		// Định dạng thời gian hiển thị theo dạng 'XN YĐ'
		// Thông thường số đêm sẽ bằng số ngày trừ đi 1
		const nights = days - 1;
		// Kiểm tra nếu days >= 0 thì hiển thị, nếu không sẽ hiển thị giá trị mặc định
		if (days > 0) {
			element.textContent = `${days}N${nights}Đ`; // Ví dụ: 3N2Đ
		} else {
			element.textContent = 'N/A'; // Nếu không hợp lệ
		}
	});
}
function linktoDetailTour(pram) {
	var tourId = pram.getAttribute("data-id");
	console.log(pram);
	window.location.href = "detail-tour/" + tourId;
}
function fomatPrice() {

	const priceElements = document.querySelectorAll('.tourPrice');
	console.log(priceElements);
	priceElements.forEach(function(priceElement) {
		let price = parseFloat(priceElement.textContent); // Chuyển đổi sang số
		if (!isNaN(price)) {
			// Định dạng và loại bỏ khoảng trắng giữa số và ký tự ₫
			priceElement.textContent = price.toLocaleString('vi-VN') + '₫';
		}
	});
	const originPriceElements = document.querySelectorAll('.tourOriginalPrice');
	originPriceElements.forEach(function(priceElement) {
		let price = parseFloat(priceElement.textContent); // Chuyển đổi sang số
		if (!isNaN(price)) {
			// Định dạng và loại bỏ khoảng trắng giữa số và ký tự ₫
			priceElement.textContent = price.toLocaleString('vi-VN') + '₫';
		}
	});
}
var  url=""
async function loadPage(page) {
	const size = 8;
	  url = `http://localhost:8080/api-get-search-tour?page=${page}&size=${size}&`; // Endpoint mới
		const urlParams = new URLSearchParams(window.location.search);
		const tourName = urlParams.get('tourName');
		const departure = urlParams.get('departure');
		const destination = urlParams.get('destination');
		const startDate = urlParams.get('startDate');
		const endDate = urlParams.get('endDate');
		const isDiscounted = urlParams.get('isDiscounted');
		const minPrice = urlParams.get('minPrice');
		const maxPrice = urlParams.get('maxPrice');
		const transportation = urlParams.get('transportation');
		
		console.log(startDate)
		console.log(transportation)
		if (tourName ) {
		    url += 'tourName=' + encodeURIComponent(tourName) + '&';
		}
		if (startDate) {
		    url += 'startDate=' + encodeURIComponent(startDate) + '&';
		}
		if (endDate) {
			url += 'endDate=' + encodeURIComponent(endDate) + '&';
		}
		if (departure) {
		    url += 'departure=' + encodeURIComponent(departure) + '&';
		}
		if (destination) {
		    url += 'destination=' + encodeURIComponent(destination) + '&';
		}
		if (isDiscounted) {
			url += 'isDiscounted=' + encodeURIComponent(isDiscounted) + '&';
		}
		if (minPrice) {
			url += 'minPrice=' + encodeURIComponent(minPrice) + '&';
		}
		if (maxPrice) {
			url += 'maxPrice=' + encodeURIComponent(maxPrice) + '&';
		}
		if (transportation) {
			url += 'transportation=' + encodeURIComponent(transportation) + '&';
		}
		
		
	console.log(url);
	url = url.slice(0, -1);
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
			console.log(data)
			const listTour = document.getElementById("list-tour");
			listTour.innerHTML = ""; // Xóa dữ liệu hiện tại
			data.content.forEach(tour => {

				listTour.innerHTML += `
				        <div class="card" onclick="linktoDetailTour(this)" data-id="${tour[0]}">
				            <img src="/image/${tour[6]}" alt="Tour Image">
				            <div class="card-content">
				                <p>
				                    <i class="fas fa-map-marker-alt icon"></i>
				                    Khởi hành từ: <span>${tour[1]}</span>
				                </p>
				                <h3>${tour[2]}</h3>
								<p class="rating" title="Đánh giá ${tour[8]}/5(${tour[9]})">
								<i class="fas fa-star ${tour[8] >= 1 ? ' full' : (tour[8] >= 0.5 ? ' half' : ' empty')}"></i> 
								<i class="fas fa-star ${tour[8] >= 2 ? ' full' : (tour[8] >= 1.5 ? ' half' : ' empty')}"></i> 
								<i class="fas fa-star ${tour[8] >= 3 ? ' full' : (tour[8] >= 2.5 ? ' half' : ' empty')}"></i> 
								<i class="fas fa-star ${tour[8] >= 4 ? ' full' : (tour[8] >= 3.5 ? ' half' : ' empty')}"></i> 
								<i class="fas fa-star ${tour[8] >= 5 ? ' full' : (tour[8] >= 4.5 ? ' half' : ' empty')}"></i>
								</p>

								<p class="price">
								    <span class="tourPrice">${tour[3]}</span>
								    ${tour[3] !== tour[7] ? 
								        `<span class="tourOriginalPrice" style="text-decoration: line-through; color: grey;">${tour[7]}</span>` 
								        : ''}
								</p>
				                <p class="time">
				                    <i class="fas fa-clock icon"></i>
				                    Thời gian:
				                    <span class="tour-duration" 
				                          data-start-date="${tour[4]}" 
				                          data-end-date="${tour[5]}">
				                    </span>
				                </p>
				            </div>
				        </div>
				    `;
			});
			fomatDay();
			fomatPrice();
			console.log(data.totalPages + "aaaa" + page)
			updatePerPage(data.totalPages, page);
		}
	} catch (error) {
		console.error("Lỗi trong fetchTours:", error);
	}
}
function updatePerPage(totalPages, currentPage) {
	const pagination = document.getElementById("pagination");
	pagination.innerHTML = ""; // Xóa nội dung phân trang cũ

	// Nút trước đó
	if (currentPage > 0) {
		const prevButton = document.createElement("li");
		prevButton.innerHTML = `
	            <button onclick="loadPage(${currentPage - 1})">
	                <i class="fas fa-chevron-left icon"></i>
	            </button>
	        `;
		pagination.appendChild(prevButton);
	}
	
	// Tạo các nút trang
	for (let i = 0; i < totalPages; i++) {
		const pageButton = document.createElement("li");
		pageButton.innerHTML = `
	            <a onclick="loadPage(${i})" class="${i === currentPage ? 'current' : ''}">
	                ${i + 1}
	            </a>
	        `;
		pagination.appendChild(pageButton);
	}

	// Nút tiếp theo
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement("li");
		nextButton.innerHTML = `
	            <button onclick="loadPage(${currentPage + 1})">
	                <i class="fas fa-chevron-right icon"></i>
	            </button>
	        `;
		pagination.appendChild(nextButton);
	}
}
/*===========================================================================================*/
let currentSortOrder = 'asc';
let currentPage = 0;
async function SortPrice(page = 0) {
	// Lấy giá trị sắp xếp từ dropdown
	const selectedSortOrder = document.getElementById('price-sort').value;
	if (selectedSortOrder === '0') {
		alert("Vui lòng chọn sắp xếp!");
		return;
	}

	// Cập nhật giá trị sắp xếp và trang hiện tại
	currentSortOrder = selectedSortOrder === '1' ? 'asc' : 'desc';
	currentPage = page;

	// Gọi API
	const size = 8; // Số phần tử mỗi trang
	const url = `http://localhost:8080/api-sorted?page=${page}&size=${size}&sort=${currentSortOrder}`;

	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	});

	try {
		const response = await fetch(request);
		if (!response.ok) {
			console.log("Có lỗi xảy ra trong SortPrice:", response);
			return;
		}

		const data = await response.json();
		console.log("Dữ liệu từ server:", data);


		updatePageWithData(data);
	} catch (error) {
		console.error("Lỗi trong SortPrice:", error);
	}
}
function updatePageWithData(data) {
	const listTour = document.getElementById("list-tour");
	listTour.innerHTML = ""; // Xóa dữ liệu hiện tại

	// Lặp qua nội dung trả về và cập nhật giao diện
	data.content.forEach(tour => {
		listTour.innerHTML += `
		<div class="card" onclick="linktoDetailTour(this)" data-id="${tour[0]}">
						            <img src="/image/${tour[6]}" alt="Tour Image">
						            <div class="card-content">
						                <p>
						                    <i class="fas fa-map-marker-alt icon"></i>
						                    Khởi hành từ: <span>${tour[1]}</span>
						                </p>
						                <h3>${tour[2]}</h3>
										<p class="rating" title="Đánh giá ${tour[8]}/5(${tour[9]})">
										<i class="fas fa-star ${tour[8] >= 1 ? ' full' : (tour[8] >= 0.5 ? ' half' : ' empty')}"></i> 
										<i class="fas fa-star ${tour[8] >= 2 ? ' full' : (tour[8] >= 1.5 ? ' half' : ' empty')}"></i> 
										<i class="fas fa-star ${tour[8] >= 3 ? ' full' : (tour[8] >= 2.5 ? ' half' : ' empty')}"></i> 
										<i class="fas fa-star ${tour[8] >= 4 ? ' full' : (tour[8] >= 3.5 ? ' half' : ' empty')}"></i> 
										<i class="fas fa-star ${tour[8] >= 5 ? ' full' : (tour[8] >= 4.5 ? ' half' : ' empty')}"></i>
										</p>

										<p class="price">
											<span class="tourPrice">${tour[3]}</span> 
										    ${tour[3] != tour[7] ? `<span class="tourPrice">${tour[7]}</span> ` : ''}
										</p>
						                <p class="time">
						                    <i class="fas fa-clock icon"></i>
						                    Thời gian:
						                    <span class="tour-duration" 
						                          data-start-date="${tour[4]}" 
						                          data-end-date="${tour[5]}">
						                    </span>
						                </p>
						            </div>
						        </div>
        `;
	});

	// Gọi các hàm format để xử lý hiển thị ngày và giá
	fomatDay();
	fomatPrice();

	// Cập nhật phân trang
	updatePagination(data.totalPages, data.number);
}
// Hàm cập nhật phân trang khi chọn giá tăng hay giảm
function updatePagination(totalPages, currentPage) {
	const pagination = document.getElementById("pagination");
	pagination.innerHTML = ""; // Xóa nội dung phân trang cũ

	// Nút "Prev"
	if (currentPage > 0) {
		const prevButton = document.createElement("li");
		prevButton.innerHTML = `
            <button onclick="SortPrice(${currentPage - 1})">
                <i class="fas fa-chevron-left icon"></i>
            </button>
        `;
		pagination.appendChild(prevButton);
	}

	// Trang đầu tiên
	const firstPageButton = document.createElement("li");
	firstPageButton.innerHTML = `
        <a onclick="SortPrice(0)" class="${currentPage === 0 ? 'current' : ''}">
            1
        </a>
    `;
	pagination.appendChild(firstPageButton);

	// Dấu "..." trước dải trang giữa
	if (currentPage > 2) {
		const dotsBefore = document.createElement("li");
		dotsBefore.innerHTML = `<span class="pagination-dots">...</span>`;
		pagination.appendChild(dotsBefore);
	}

	// Các trang giữa
	for (let i = Math.max(1, currentPage - 1); i <= Math.min(totalPages - 2, currentPage + 1); i++) {
		const pageButton = document.createElement("li");
		pageButton.innerHTML = `
            <a onclick="SortPrice(${i})" class="${i === currentPage ? 'current' : ''}">
                ${i + 1}
            </a>
        `;
		pagination.appendChild(pageButton);
	}

	// Dấu "..." sau dải trang giữa
	if (currentPage < totalPages - 3) {
		const dotsAfter = document.createElement("li");
		dotsAfter.innerHTML = `<span class="pagination-dots">...</span>`;
		pagination.appendChild(dotsAfter);
	}

	// Trang cuối cùng
	if (totalPages > 1) {
		const lastPageButton = document.createElement("li");
		lastPageButton.innerHTML = `
            <a onclick="SortPrice(${totalPages - 1})" class="${currentPage === totalPages - 1 ? 'current' : ''}">
                ${totalPages}
            </a>
        `;
		pagination.appendChild(lastPageButton);
	}

	// Nút "Next"
	if (currentPage + 1 < totalPages) {
		const nextButton = document.createElement("li");
		nextButton.innerHTML = `
            <button onclick="SortPrice(${currentPage + 1})">
                <i class="fas fa-chevron-right icon"></i>
            </button>
        `;
		pagination.appendChild(nextButton);
	}
}
document.getElementById("main-btn-search").addEventListener("click", function () {
	// Lấy giá trị từ các trường nhập liệu
	    const tourName = document.querySelector("#input-tour-name")?.value || null;
		console.log(tourName)
		   var startDate = document.getElementById('input-start-date').value;
		// Chuyển đổi định dạng ngày từ dd-MM-yyyy sang yyyy-MM-dd
		   var formattedStartDate = "";
		   if (startDate) {
		       var dateParts = startDate.split('-'); // Tách ngày, tháng, năm
		       if (dateParts.length === 3) {
		           formattedStartDate = dateParts[2] + '-' + dateParts[1] + '-' + dateParts[0]; // Chuyển đổi sang yyyy-MM-dd
		       }
		   }
		   var endDate = document.getElementById('input-end-date').value;
		   		// Chuyển đổi định dạng ngày từ dd-MM-yyyy sang yyyy-MM-dd
		   		   var formattedStartDate = "";
		   		   if (startDate) {
		   		       var dateParts = startDate.split('-'); // Tách ngày, tháng, năm
		   		       if (dateParts.length === 3) {
		   		           formattedStartDate = dateParts[2] + '-' + dateParts[1] + '-' + dateParts[0]; // Chuyển đổi sang yyyy-MM-dd
		   		       }
		   		   }
		var departureSelect = document.getElementById('departureSelect');
		var departureText = departureSelect.options[departureSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
		var departure = (departureText === "Chọn điểm đi") ? "" : departureText;

		const checkedInput = document.querySelector('.transportation-filter-box input[name="transport-option"]:checked');
		    const transportation= checkedInput ? checkedInput.parentElement.textContent.trim() : null;
	   

	    // Xử lý các tùy chọn giá
	    let minPrice = null;
	    let maxPrice = null;
	    let isDiscounted = false; // Biến để xác định "Đang giảm giá"
	    const priceOption = document.querySelector('.price-filter-box input[name="option"]:checked')?.value;

		console.log(transportation);
		console.log(priceOption);
	    if (priceOption === "0") {
	        // Đang giảm giá
	        isDiscounted = true;
	    } else if (priceOption === "1") {
	        // Dưới 1 triệu
	        minPrice = 0;
	        maxPrice = 1000000;
	    } else if (priceOption === "2") {
	        // Từ 1 - 5 triệu
	        minPrice = 1000000;
	        maxPrice = 5000000;
	    } else if (priceOption === "3") {
	        // Trên 5 triệu
	        minPrice = 5000000;
	        maxPrice = null;
	    } else if (priceOption === "4") {
	        // Chọn mức giá
	        minPrice = document.querySelector("#range-min").value || null;
	        maxPrice = document.querySelector("#range-max").value || null;
	    }

	    // Xây dựng URL với các tham số
	    const baseUrl = '/search?';
	    const urlParams = new URLSearchParams();

	    if (tourName) urlParams.append("tourName", tourName);
	    if (startDate) urlParams.append("startDate", startDate);
		if (endDate) urlParams.append("endDate", endDate);
	    if (departure) urlParams.append("departure", departure);
	    if (transportation) urlParams.append("transportation", transportation);
	    if (minPrice) urlParams.append("minPrice", minPrice);
	    if (maxPrice) urlParams.append("maxPrice", maxPrice);
	    if (isDiscounted) urlParams.append("isDiscounted", isDiscounted);

	    // Kết hợp URL cơ bản với tham số
	    const fullUrl = `${baseUrl}&${urlParams.toString()}`;
		window.location.href = fullUrl;
});

/*===========================================================================================*/
