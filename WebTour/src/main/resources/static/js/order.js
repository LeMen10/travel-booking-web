document.addEventListener('DOMContentLoaded', function() {
	//lọc paid hoặc unpaid
	document.getElementById("payment-status").addEventListener("change", FilterDataTable);
	//ẩn hiện ô tìm kiếm departure
	document.getElementById("payment-status").addEventListener("change", showInput);
	//nút ok để tìm kiếm departure
	document.getElementById("btn-input-ok").addEventListener("click", () => {
		currentPage = 0; // Đặt lại trang về 0
		searchDeparture();
	})

	// ấn nút thanh toán trên table
	const btnPayments = document.querySelectorAll(".btn-payment-status");
	//dùng for vì có nhiều nút (nhiều hàng)
	btnPayments.forEach((button) => {
		button.addEventListener('click', async function() {
			await navigateToPaymentPage(button);
		});
	});


	/*const confirmCancelButton = document.getElementById("confirm-cancel");*/
	const closeModalButton = document.getElementById("cancel-close");
	const modalOverlay = document.getElementById("cancel-confirm-modal");

	/*hủy đơn*/
	const cancelButtons = document.querySelectorAll(".btn-cancel");
	cancelButtons.forEach(button => {
		button.addEventListener("click", function() {
			//openCancelModal(this.getAttribute("data-booking-id"));
			const bookingId = this.getAttribute("data-booking-id");
			const departureDate = this.getAttribute("data-departure-date");
			checkCancelOrderDate(bookingId, departureDate);
		});
	});

	/*confirmCancelButton.addEventListener("click",funcon);*/
	closeModalButton.addEventListener("click", closeCancelModal);

	// Đóng modal khi nhấn vào vùng overlay bên ngoài modal-content
	modalOverlay.addEventListener("click", function(event) {
		if (event.target === modalOverlay) { // Kiểm tra nếu nhấn vào overlay
			closeCancelModal();
		}
	});

	document.querySelectorAll(".show-notification").forEach((element) => {
		element.addEventListener("click", showDetailPayment);
	});

})

//lọc paid hoặc unpaid
let currentPage = 0;
let pageSize = 5;

async function FilterDataTable() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const paymentStatus = document.getElementById("payment-status").value;
	const btnPay = document.getElementById("btn-payment-status");
	console.log("paymentStatus " + paymentStatus);
	const url = `http://localhost:8080/account/filter-get-order?userId=${userId}&paymentStatus=${paymentStatus}&page=${currentPage}&size=${pageSize}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	});
	const response = await fetch(request);
	console.log("Phản hồi từ server FilterDataTable:", response);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	const dataFilter = await response.json();
	//console.log(dataFilter[0][3]);

	console.log("dataFilter ", dataFilter);
	const totalPages = dataFilter.totalPages;

	updateDataTable(dataFilter.content);
	renderPaginationControls(totalPages);

}

function navigateRefundPage(bookingId) {
	//const departureDate = document.getElementById("bt-cancel-order").getAttribute("data-departure-date");
	//checkCancelOrderDate(bookingId, departureDate);
	window.location.href = "/refund/" + bookingId;
}

//cập nhật lại các nút phân trang khi lọc
function renderPaginationControls(totalPages) {
    const paginationContainer = document.getElementById("pagination");
    paginationContainer.innerHTML = ""; // Xóa các nút cũ

    // Tạo nút "Prev"
    if (currentPage > 0) {
        const prevButton = document.createElement("button");
        prevButton.className = "pagination-button";
        prevButton.innerHTML = '<i class="fas fa-chevron-left icon"></i>';
        prevButton.addEventListener("click", () => {
			
			currentPage--;
            FilterDataTable(); // Gọi lại FilterDataTable
        });
        paginationContainer.appendChild(prevButton);
    }

    // Trang đầu tiên
    const firstPageButton = document.createElement("button");
    firstPageButton.className = "pagination-button";
    firstPageButton.textContent = 1;
    if (currentPage === 0) {
        firstPageButton.classList.add("active");
    }
    firstPageButton.addEventListener("click", () => {
        currentPage = 0;
        FilterDataTable();
    });
    paginationContainer.appendChild(firstPageButton);

    // Dấu "..." trước dải trang giữa
    if (currentPage > 2) {
        const dotsBefore = document.createElement("span");
        dotsBefore.textContent = "...";
        dotsBefore.className = "pagination-dots";
        paginationContainer.appendChild(dotsBefore);
    }

    // Các trang giữa
    for (let i = Math.max(1, currentPage - 1); i <= Math.min(totalPages - 2, currentPage + 1); i++) {
        const middlePageButton = document.createElement("button");
        middlePageButton.className = "pagination-button";
        middlePageButton.textContent = i + 1;

        if (i === currentPage) {
            middlePageButton.classList.add("active");
        }

        middlePageButton.addEventListener("click", () => {
            currentPage = i;
            FilterDataTable();
        });

        paginationContainer.appendChild(middlePageButton);
    }

    // Dấu "..." sau dải trang giữa
    if (currentPage < totalPages - 3) {
        const dotsAfter = document.createElement("span");
        dotsAfter.textContent = "...";
        dotsAfter.className = "pagination-dots";
        paginationContainer.appendChild(dotsAfter);
    }

    // Trang cuối cùng
    if (totalPages > 1) {
        const lastPageButton = document.createElement("button");
        lastPageButton.className = "pagination-button";
        lastPageButton.textContent = totalPages;

        if (currentPage === totalPages - 1) {
            lastPageButton.classList.add("active");
        }

        lastPageButton.addEventListener("click", () => {
            currentPage = totalPages - 1;
            FilterDataTable();
        });

        paginationContainer.appendChild(lastPageButton);
    }

    // Tạo nút "Next"
    if (currentPage < totalPages - 1) {
        const nextButton = document.createElement("button");
        nextButton.className = "pagination-button";
        nextButton.innerHTML = '<i class="fas fa-chevron-right icon"></i>';
        nextButton.addEventListener("click", () => {
			
			currentPage++;
            FilterDataTable(); // Gọi lại FilterDataTable
        });
        paginationContainer.appendChild(nextButton);
    }
}



//cập nhật bảng sau khi ấn lọc hoặc tìm kiếm
function updateDataTable(dataTable) {
	const tableBody = document.getElementById("user-table");
	tableBody.innerHTML = ""; // Xóa các hàng cũ

	dataTable.forEach(booking => {
		const row = document.createElement("tr");

		// Mã đơn (booking_id)
		const bookingIdCell = document.createElement("td");
		bookingIdCell.className = "table-td";
		bookingIdCell.setAttribute('data-booking-id', booking[0]);
		bookingIdCell.textContent = booking[0];
		row.appendChild(bookingIdCell);

		console.error(booking);
		// Tour
		const tourNameCell = document.createElement("td");
		tourNameCell.className = "table-td";
		tourNameCell.innerHTML = `<a href="/detail-tour/${booking[11]}">${booking[4]}</a>`;
		row.appendChild(tourNameCell);

		// Mã thanh toán
		const paymentIdCell = document.createElement("td");
		paymentIdCell.className = "table-td";
		paymentIdCell.textContent = booking[10];
		row.appendChild(paymentIdCell);

		// Ngày đặt (booking_date)
		const bookingDateCell = document.createElement("td");
		bookingDateCell.className = "table-td table-td-date";

		const bookingDate = new Date(booking[1]);
		const year = bookingDate.getFullYear();
		const month = String(bookingDate.getMonth() + 1).padStart(2, '0'); // Thêm số 0 nếu tháng < 10
		const day = String(bookingDate.getDate()).padStart(2, '0'); // Thêm số 0 nếu ngày < 10

		//bookingDateCell.textContent = new Date(booking[1]).toLocaleDateString().split('T')[0]; // Định dạng yyyy-MM-dd
		bookingDateCell.textContent = `${year}-${month}-${day}`;
		row.appendChild(bookingDateCell);
		console.log(bookingDateCell);

		// Ngày đi (start_date)
		const startDateCell = document.createElement("td");
		startDateCell.className = "table-td table-td-date";
		startDateCell.textContent = new Date(booking[8]).toISOString().split('T')[0];
		row.appendChild(startDateCell);
		console.log(startDateCell);

		// Ngày về (end_date)
		const endDateCell = document.createElement("td");
		endDateCell.className = "table-td table-td-date";
		endDateCell.textContent = new Date(booking[12]).toISOString().split('T')[0];
		row.appendChild(endDateCell);
		console.log(endDateCell);

		// Số lượng (totalQuantity)
		const quantityCell = document.createElement("td");
		quantityCell.className = "table-td";
		quantityCell.textContent = booking[5];
		row.appendChild(quantityCell);

		// Loại vé (ticketDetails)
		const ticketDetailsCell = document.createElement("td");
		ticketDetailsCell.className = "table-td";
		ticketDetailsCell.textContent = booking[6];
		row.appendChild(ticketDetailsCell);

		// Khởi hành (departure)
		const departureCell = document.createElement("td");
		departureCell.className = "table-td";
		departureCell.textContent = booking[9];
		row.appendChild(departureCell);

		// Thành tiền (total_price)
		const totalPriceCell = document.createElement("td");
		totalPriceCell.className = "table-td";
		totalPriceCell.textContent = booking[7];
		row.appendChild(totalPriceCell);

		// Thanh toán (payment_method)
		const paymentMethodCell = document.createElement("td");
		paymentMethodCell.className = "table-td";
		paymentMethodCell.textContent = booking[2] === 1 ? "Cash" : "Online";
		row.appendChild(paymentMethodCell);

		// Trạng thái (payment_status)
		const paymentStatusCell = document.createElement("td");
		paymentStatusCell.className = "table-td";
		//paymentStatusCell.textContent = booking[3] === 1 ? "Paid" : "Unpaid";
		paymentStatusCell.textContent = booking[3] === 1 ? "Paid" : booking[3] === null ? "Unpaid" : booking[3] === 3 ? "Cancelled" : "Unknown";
		row.appendChild(paymentStatusCell);

		//cột Action . Tạo nút thanh toán 
		const actionCell = document.createElement("td");
		actionCell.className = "table-td table-td-action";

		if (booking[3] !== 1) {
			const paymentButton = document.createElement("button");
			paymentButton.className = "btn-payment-status";
			paymentButton.textContent = "Thanh toán";
			paymentButton.disabled = booking[3] === 3 || booking[3] === 1;
			paymentButton.textContent = booking[3] === 1 ? "Đã thanh toán" : booking[3] === null ? "Thanh toán" : booking[3] === 3 ? "Thanh toán" : "Unknown";
			// Thêm class paid-status nếu booking[3] là 1 (Đã thanh toán)
			if (booking[3] === 1) {
				paymentButton.classList.add("paid-status"); //thêm css (dòng 203) cho nút khi có thay đổi
			}
			// Gán thuộc tính dữ liệu
			paymentButton.setAttribute("data-booking-id", booking[0]);
			paymentButton.setAttribute("data-total-price", booking[7]);
			// Gán sự kiện click cho nút thanh toán
			paymentButton.addEventListener('click', async function() {
				await navigateToPaymentPage(paymentButton);
			});
			actionCell.appendChild(paymentButton);
			row.appendChild(actionCell);
		}

		// Nút hủy đơn
		const cancelButton = document.createElement("button");
		if (booking[3] == 1) {

			cancelButton.className = "btn-cancel";
			cancelButton.textContent = "Hủy đơn";
			cancelButton.setAttribute("data-booking-id", booking[0]);
			cancelButton.setAttribute("data-departure-date", booking[8]);
			// Gán sự kiện click cho nút hủy đơn để mở modal xác nhận
			/*cancelButton.addEventListener('click', function() {
			openCancelModal(booking[0]);
			});*/

		}
		actionCell.appendChild(cancelButton);
		row.appendChild(actionCell);

		// Cột Action với biểu tượng "fa-eye"
		const eyesCell = document.createElement("td");
		eyesCell.className = "table-td table-td-action";
		const viewIcon = document.createElement("i");
		viewIcon.id = "show-notification";
		viewIcon.setAttribute('data-booking-id', booking[0]);
		viewIcon.className = "fa-solid fa-eye";
		// Thêm sự kiện click cho biểu tượng
		viewIcon.addEventListener('click', function() {
			showDetailPayment(booking[0]); 
		});
		eyesCell.appendChild(viewIcon);
		row.appendChild(eyesCell);

		// Thêm hàng mới vào tbody
		tableBody.appendChild(row);

		// Log thông tin cho mỗi booking
		console.log(
			`id: ${booking[0]}, tourName: ${booking[4]}, paymentId: ${booking[10]}, ` +
			`bookingDate: ${new Date(booking[1]).toISOString().split('T')[0]}, startDate: ${new Date(booking[8]).toISOString().split('T')[0]}, ` +
			`totalQuantity: ${booking[5]}, ticketDetails: ${booking[6]}, departure: ${booking[9]}, ` +
			`totalPrice: ${booking[7]}, paymentMethod: ${booking[2] === 1 ? "Cash" : "Online"}, ` +
			`paymentStatus: ${booking[3] === 1 ? "Paid" : booking[3] === 2 ? "Unpaid" : booking[3] === 3 ? "Cancelled" : 'Unknown'}`
		);
	});
}



//ẩn hiện ô input của option departure trên filter
function showInput() {
	const selectElement = document.getElementById("payment-status");
	const inputElement = document.getElementById("add-input");
	const btnInputOk = document.getElementById("btn-input-ok");

	if (selectElement.value == 4) {
		inputElement.style.display = "block"; //hiện
		btnInputOk.style.display = "block";
	} else {
		inputElement.style.display = "none";
		btnInputOk.style.display = "none";
	}
}

//tìm kiếm departure
let currentPageSearch = 0; // Đặt trang hiện tại
const pageSizeSearch = 5;
async function searchDeparture() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const searchInput = document.getElementById("add-input").value;
	console.log("searchInput " + searchInput);
	const url = `http://localhost:8080/account/search-departure?userId=${userId}&searchInput=${searchInput}&page=${currentPageSearch}&size=${pageSizeSearch}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		},
	});
	const response = await fetch(request);
	console.log("Phản hồi từ server searchDeparture:", response);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	const dataSearch = await response.json();
	console.log("dataSearch " + dataSearch);
	updateDataTable(dataSearch.content);
	renderPaginationControlsSearch(dataSearch.totalPages);
}

//cập nhật lại các nút phân trang khi tìm kiếm
function renderPaginationControlsSearch(totalPages) {
    const paginationContainer = document.getElementById("pagination");
    paginationContainer.innerHTML = ""; // Xóa các nút cũ

    // Tạo nút "Prev"
    if (currentPageSearch > 0) {
        const prevButton = document.createElement("button");
        prevButton.className = "pagination-button";
        prevButton.innerHTML = '<i class="fas fa-chevron-left icon"></i>';
        prevButton.addEventListener("click", () => {
            currentPageSearch--;
            searchDeparture();
        });
        paginationContainer.appendChild(prevButton);
    }

    // Trang đầu tiên
    const firstPageButton = document.createElement("button");
    firstPageButton.className = "pagination-button";
    firstPageButton.textContent = 1;
    if (currentPageSearch === 0) {
        firstPageButton.classList.add("active");
    }
    firstPageButton.addEventListener("click", () => {
        currentPageSearch = 0;
        searchDeparture();
    });
    paginationContainer.appendChild(firstPageButton);

    // Dấu "..." trước dải trang giữa
    if (currentPageSearch > 2) {
        const dotsBefore = document.createElement("span");
        dotsBefore.textContent = "...";
        dotsBefore.className = "pagination-dots";
        paginationContainer.appendChild(dotsBefore);
    }

    // Các trang giữa
    for (let i = Math.max(1, currentPageSearch - 1); i <= Math.min(totalPages - 2, currentPageSearch + 1); i++) {
        const middlePageButton = document.createElement("button");
        middlePageButton.className = "pagination-button";
        middlePageButton.textContent = i + 1;

        if (i === currentPageSearch) {
            middlePageButton.classList.add("active");
        }

        middlePageButton.addEventListener("click", () => {
            currentPageSearch = i;
            searchDeparture();
        });

        paginationContainer.appendChild(middlePageButton);
    }

    // Dấu "..." sau dải trang giữa
    if (currentPageSearch < totalPages - 3) {
        const dotsAfter = document.createElement("span");
        dotsAfter.textContent = "...";
        dotsAfter.className = "pagination-dots";
        paginationContainer.appendChild(dotsAfter);
    }

    // Trang cuối cùng
    if (totalPages > 1) {
        const lastPageButton = document.createElement("button");
        lastPageButton.className = "pagination-button";
        lastPageButton.textContent = totalPages;

        if (currentPageSearch === totalPages - 1) {
            lastPageButton.classList.add("active");
        }

        lastPageButton.addEventListener("click", () => {
            currentPageSearch = totalPages - 1;
            searchDeparture();
        });

        paginationContainer.appendChild(lastPageButton);
    }

    // Tạo nút "Next"
    if (currentPageSearch < totalPages - 1) {
        const nextButton = document.createElement("button");
        nextButton.className = "pagination-button";
        nextButton.innerHTML = '<i class="fas fa-chevron-right icon"></i>';
        nextButton.addEventListener("click", () => {
            currentPageSearch++;
            searchDeparture();
        });
        paginationContainer.appendChild(nextButton);
    }
}


//ấn nút thanh toán trên mỗi dòng sẽ chuyển đến trang thanh toán (payment)
async function navigateToPaymentPage(button) {
	const bookingId = button.getAttribute("data-booking-id");
	const totalPrice = button.getAttribute("data-total-price");
	window.location.href = `/payment/${bookingId}?totalPrice=${totalPrice}`;
	/*window.location.href = `/payment/${bookingId}`;*/
}

// Hàm đóng modal
function closeCancelModal() {
	const modal = document.getElementById("cancel-confirm-modal");
	modal.classList.remove("show");
}

/*hiện modal hủy đơn*/
let selectedBookingId = null;
function openCancelModal(bookingId) {
	const modal = document.getElementById("cancel-confirm-modal");
	selectedBookingId = bookingId;
	modal.classList.add("show"); //css dòng 268
}

//kiểm tra ngày hủy đơn
async function checkCancelOrderDate(bookingId, startDate) {
	console.log("hàm checkCancelOrderDate đươc gọi ");
	//const btCancelOrder = querySelectorAll(".btn-cancel");
	const departureDate = new Date(startDate);
	const currentDate = new Date();

	const lastCancelDate = new Date(departureDate);
	lastCancelDate.setDate(lastCancelDate.getDate() - 3);

	console.log("Ngày khởi hành:", departureDate);
	console.log("Ngày hiện tại:", currentDate);
	console.log("Ngày hủy cuối cùng:", lastCancelDate);

	if (currentDate > lastCancelDate) {
		alert("Đã quá thời hạn hủy đơn.");
	} else {
		console.log("gọi modal");
		// Hiển thị modal xác nhận
		openCancelModal(bookingId);
		navigateRefundPage(bookingId);
	}
}

//ấn để xem chi tiết hóa đơn (trang notificationSuccess)
async function showDetailPayment() {
	const bookingId = event.target.getAttribute('data-booking-id');
	const paymentIdCell = event.target.closest('tr').querySelector('td:nth-child(3)'); // Cột Mã thanh toán
	const paymentId = paymentIdCell ? paymentIdCell.textContent.trim() : null;
	
	if (!paymentId || paymentId === 'null' || paymentId === '') {
		alert('Vui lòng thanh toán trước khi xem chi tiết hóa đơn.');
		return;
	}
	console.log(bookingId);
	window.location.href = `/notificationSuccess/${bookingId}`;
}



