document.addEventListener('DOMContentLoaded', function() {
	document.getElementById("payment-status").addEventListener("change", FilterDataTable);
	document.getElementById("payment-status").addEventListener("change", showInput);
})

async function FilterDataTable() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const paymentStatus = document.getElementById("payment-status").value;
	console.log("paymentStatus "+ paymentStatus);
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
		console.log("dataFilter "+ dataFilter);
		updateDataTable(dataFilter); 
}

function updateDataTable(dataTable) {
    const tableBody = document.getElementById("user-table");
    tableBody.innerHTML = ""; // Xóa các hàng cũ

    dataTable.forEach(booking => {
        const row = document.createElement("tr");

        // Mã đơn (booking_id)
        const bookingIdCell = document.createElement("td");
        bookingIdCell.textContent = booking[0];
        row.appendChild(bookingIdCell);

        // Tour
        const tourNameCell = document.createElement("td");
        tourNameCell.textContent = booking[4];
        row.appendChild(tourNameCell);

        // Mã thanh toán
        const paymentIdCell = document.createElement("td");
        paymentIdCell.textContent = booking[10];
        row.appendChild(paymentIdCell);

        // Ngày đặt (booking_date)
        const bookingDateCell = document.createElement("td");
        bookingDateCell.textContent = new Date(booking[1]).toISOString().split('T')[0]; // Định dạng yyyy-MM-dd
        row.appendChild(bookingDateCell);

        // Ngày đi (start_date)
        const startDateCell = document.createElement("td");
        startDateCell.textContent = new Date(booking[8]).toISOString().split('T')[0];
        row.appendChild(startDateCell);

        // Số lượng (totalQuantity)
        const quantityCell = document.createElement("td");
        quantityCell.textContent = booking[5];
        row.appendChild(quantityCell);

        // Loại vé (ticketDetails)
        const ticketDetailsCell = document.createElement("td");
        ticketDetailsCell.textContent = booking[6];
        row.appendChild(ticketDetailsCell);

        // Khởi hành (departure)
        const departureCell = document.createElement("td");
        departureCell.textContent = booking[9];
        row.appendChild(departureCell);

        // Thành tiền (total_price)
        const totalPriceCell = document.createElement("td");
        totalPriceCell.textContent = booking[7];
        row.appendChild(totalPriceCell);

        // Thanh toán (payment_method)
        const paymentMethodCell = document.createElement("td");
        paymentMethodCell.textContent = booking[2] === 1 ? "Cash" : "Online";
        row.appendChild(paymentMethodCell);

        // Trạng thái (payment_status)
        const paymentStatusCell = document.createElement("td");
        if (booking[3] === 1) {
            paymentStatusCell.textContent = "Paid";
        } else {
            paymentStatusCell.textContent = "Unpaid";
        }
        row.appendChild(paymentStatusCell);

		//tạo nút thanh toán
		const actionCell = document.createElement("td");
        const paymentButton = document.createElement("button");
        paymentButton.className = "btn-payment-status";
        paymentButton.textContent = "Thanh toán";
        actionCell.appendChild(paymentButton);
        row.appendChild(actionCell);
		
        // Thêm hàng mới vào tbody
        tableBody.appendChild(row);
    });
}

//ẩn hiện ô input của option departure trên filter
function showInput() {
    const selectElement = document.getElementById("payment-status");
    const inputElement = document.getElementById("add-input");

    if (selectElement.value == 3) {
        inputElement.style.display = "block"; //hiện
    } else {
        inputElement.style.display = "none"; 
    }
}

//tìm kiếm departure
async function searchDeparture() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const searchInput = document.getElementById("add-input").value;
	console.log("dataSearch "+ paymentStatus);
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
		console.log("dataSearch "+ dataSearch);
		updateDataTable(dataSearch); 
}