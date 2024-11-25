document.addEventListener('DOMContentLoaded', async function() {
	document.getElementById("bt-continuteShop").addEventListener("click", backHomeForm);
	var orderId = document.querySelector(".order-info").getAttribute("data-id");
	var bookingId = document.getElementById("bookingId").getAttribute("data-id");
	//await checkTransactionStatus(orderId);
	// Xử lý kết quả giao dịch
	if (orderId == 0) {
		
		const captureId = orderId; // Đối với MoMo, sử dụng orderId làm captureId

		await updatePaymentStatus(bookingId);
		window.location.href = `/notificationSuccess/${bookingId}`;

	} else {
		alert(`Giao dịch thất bại: ${data.message}`);
	}

});

async function backHomeForm() {
	window.location.href = '/home';
}

async function checkTransactionStatus(orderId) {
	try {
		const response = await fetch('http://localhost:3000/check-status-transaction', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({ orderId }), // Gửi dữ liệu orderId trong body
		});

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`);
		}

		const data = await response.json();
		console.log('Transaction status:', data);

		// Xử lý kết quả giao dịch
		if (data.resultCode === 0) {
			updatePaymentStatus()
			alert('Giao dịch thành công!');
		} else {
			alert(`Giao dịch thất bại: ${data.message}`);
		}
	} catch (error) {
		console.error('Error checking transaction status:', error);
	}
}



//cập nhật paymentStatus sau khi thanh toán thành công
async function updatePaymentStatus(bookingId) {
	const url = `http://localhost:8080/update-status/${bookingId}`;
	const request = new Request(url, {
		method: "PUT",
		headers: {
			"Content-Type": "application/json",
		},
	});

	try {
		const response = await fetch(request);
		if (!response.ok) {
			throw new Error("lỗi response updatePaymentStatus");
		}
		const message = await response.text();
		console.log("updatePaymentStatus " + message);
	} catch (error) {
		console.error("Lỗi updatePaymentStatus:", error);
	}
}
