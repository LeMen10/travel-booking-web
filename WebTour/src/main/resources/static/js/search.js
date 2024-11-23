// Lấy tất cả các phần tử có chứa tour-duration
document.addEventListener('DOMContentLoaded', function() {
	fomatDay();
	fomatPrice();
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
async function loadPage(page) {

	const size = 8;
	const url = `http://localhost:8080/api-get-search-tour?page=${page}&size=${size}`; // Endpoint mới
	console.log(url)
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
								    ${tour[3] != tour[7]? `<span class="tourPrice">${tour[7]}</span> `:''}
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
			updatePerPage(data.totalPages, page);
			console.log(data.totalPages + "aaaa" + page)
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
