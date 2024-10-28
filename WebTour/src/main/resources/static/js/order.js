document.addEventListener('DOMContentLoaded', function() {
	//lọc paid hoặc unpaid
	document.getElementById("payment-status").addEventListener("change", FilterDataTable);
	//ẩn hiện ô tìm kiếm departure
	document.getElementById("payment-status").addEventListener("change", showInput);
	//nút ok để tìm kiếm departure
	document.getElementById("btn-input-ok").addEventListener("click", searchDeparture);


	// ấn nút thanh toán trên table
	const btnPayments = document.querySelectorAll(".btn-payment-status");
	//dùng for vì có nhiều nút (nhiều hàng)
	btnPayments.forEach((button) => {
		button.addEventListener('click', async function() {
			await navigateToPaymentPage(button);
		});
	});

	/*hủy đơn*/
	const cancelButtons = document.querySelectorAll(".btn-cancel");
	/*const confirmCancelButton = document.getElementById("confirm-cancel");*/
	const closeModalButton = document.getElementById("cancel-close");
	const modalOverlay = document.getElementById("cancel-confirm-modal");

	cancelButtons.forEach(button => {
		button.addEventListener("click", function() {
			openCancelModal(this.getAttribute("data-booking-id"));
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

})

//lọc paid hoặc unpaid
async function FilterDataTable() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const paymentStatus = document.getElementById("payment-status").value;
	const btnPay = document.getElementById("btn-payment-status");
	console.log("paymentStatus " + paymentStatus);
	const url = `http://localhost:8080/account/filter-get-order?userId=${userId}&paymentStatus=${paymentStatus}`;
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
	console.log(dataFilter[0][3]);


	console.log("dataFilter ", dataFilter);
	updateDataTable(dataFilter);


}

//cập nhật bảng sau khi ấn lọc hoặc thay đổi
function updateDataTable(dataTable) {
	const tableBody = document.getElementById("user-table");
	tableBody.innerHTML = ""; // Xóa các hàng cũ

	dataTable.forEach(booking => {
		const row = document.createElement("tr");

		// Mã đơn (booking_id)
		const bookingIdCell = document.createElement("td");
		bookingIdCell.className = "table-td";
		bookingIdCell.textContent = booking[0];
		row.appendChild(bookingIdCell);

		// Tour
		const tourNameCell = document.createElement("td");
		tourNameCell.className = "table-td";
		tourNameCell.textContent = booking[4];
		row.appendChild(tourNameCell);

		// Mã thanh toán
		const paymentIdCell = document.createElement("td");
		paymentIdCell.className = "table-td";
		paymentIdCell.textContent = booking[10];
		row.appendChild(paymentIdCell);

		// Ngày đặt (booking_date)
		const bookingDateCell = document.createElement("td");
		bookingDateCell.className = "table-td";
		bookingDateCell.textContent = new Date(booking[1]).toISOString().split('T')[0]; // Định dạng yyyy-MM-dd
		row.appendChild(bookingDateCell);

		// Ngày đi (start_date)
		const startDateCell = document.createElement("td");
		startDateCell.className = "table-td";
		startDateCell.textContent = new Date(booking[8]).toISOString().split('T')[0];
		row.appendChild(startDateCell);

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
		paymentStatusCell.textContent = booking[3] === 1 ? "Paid" : "Unpaid";
		row.appendChild(paymentStatusCell);

		//cột Action . Tạo nút thanh toán 
		const actionCell = document.createElement("td");
		actionCell.className = "table-td";
		const paymentButton = document.createElement("button");
		paymentButton.className = "btn-payment-status";
		paymentButton.textContent = booking[3] === 1 ? "Đã thanh toán" : "Thanh toán";
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

		// Nút hủy đơn
		if(booking[3] != 1){
		const cancelButton = document.createElement("button");
		cancelButton.className = "btn-cancel";
		cancelButton.textContent = "Hủy đơn";
		cancelButton.setAttribute("data-booking-id", booking[0]);
		// Gán sự kiện click cho nút hủy đơn để mở modal xác nhận
		cancelButton.addEventListener('click', function() {
			openCancelModal(booking[0]);
		});
		actionCell.appendChild(cancelButton);
		}
		row.appendChild(actionCell);

		// Thêm hàng mới vào tbody
		tableBody.appendChild(row);

		// Log thông tin cho mỗi booking
		console.log(
			`id: ${booking[0]}, tourName: ${booking[4]}, paymentId: ${booking[10]}, ` +
			`bookingDate: ${new Date(booking[1]).toISOString().split('T')[0]}, startDate: ${new Date(booking[8]).toISOString().split('T')[0]}, ` +
			`totalQuantity: ${booking[5]}, ticketDetails: ${booking[6]}, departure: ${booking[9]}, ` +
			`totalPrice: ${booking[7]}, paymentMethod: ${booking[2] === 1 ? "Cash" : "Online"}, ` +
			`paymentStatus: ${booking[3] === 1 ? "Paid" : "Unpaid"}`
		);
	});
}


//ẩn hiện ô input của option departure trên filter
function showInput() {
	const selectElement = document.getElementById("payment-status");
	const inputElement = document.getElementById("add-input");
	const btnInputOk = document.getElementById("btn-input-ok");

	if (selectElement.value == 3) {
		inputElement.style.display = "block"; //hiện
		btnInputOk.style.display = "block";
	} else {
		inputElement.style.display = "none";
		btnInputOk.style.display = "none";
	}
}

//tìm kiếm departure
async function searchDeparture() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const searchInput = document.getElementById("add-input").value;
	console.log("searchInput " + searchInput);
	const url = `http://localhost:8080/account/search-departure?userId=${userId}&searchInput=${searchInput}`;
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
	updateDataTable(dataSearch);
}

//ấn nút thanh toán trên mỗi dòng sẽ chuyển đến trang thanh toán (payment)
async function navigateToPaymentPage(button) {
	const bookingId = button.getAttribute("data-booking-id");
	const totalPrice = button.getAttribute("data-total-price");
	window.location.href = `/payment/${bookingId}?totalPrice=${totalPrice}`;
	/*window.location.href = `/payment/${bookingId}`;*/
}

/*hủy đơn*/
let selectedBookingId = null;
function openCancelModal(bookingId) {

	const modal = document.getElementById("cancel-confirm-modal");
	selectedBookingId = bookingId;
	modal.classList.add("show");
}



// Hàm đóng modal
function closeCancelModal() {
	const modal = document.getElementById("cancel-confirm-modal");
	modal.classList.remove("show");
}

