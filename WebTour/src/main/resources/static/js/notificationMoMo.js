document.addEventListener('DOMContentLoaded', async function() {
	document.getElementById("bt-continuteShop").addEventListener("click", backHomeForm);
	var orderId = document.querySelector(".order-info").getAttribute("data-id");
	var bookingId = document.getElementById("bookingId").getAttribute("data-id");
	//await checkTransactionStatus(orderId);
	// Xử lý kết quả giao dịch
	if (orderId == 0) {
		await updatePaymentStatus(bookingId,3);
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
			const bookingId = document.getElementById("bookingId").getAttribute("data-id");
			updatePaymentStatus(bookingId,3);
			alert('Giao dịch thành công!');
		} else {
			alert(`Giao dịch thất bại: ${data.message}`);
		}
	} catch (error) {
		console.error('Error checking transaction status:', error);
	}
}



//cập nhật paymentStatus sau khi thanh toán thành công
async function updatePaymentStatus(bookingId,PaymentMethodId) {
	const url = `http://localhost:8080/update-status?bookingId=${bookingId}&PaymentMethodId=${PaymentMethodId}`;
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

/*async function refundMoMo(orderId, amount) {
    try {
        const response = await fetch('http://localhost:3000/refund-payment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ orderId, amount }),
        });

        const data = await response.json();
        if (data.resultCode === 0) {
            console.log('Hoàn tiền thành công:', data);
            alert('Hoàn tiền thành công!');
        } else {
            console.log('Hoàn tiền thất bại:', data.message);
            alert('Hoàn tiền thất bại!');
        }
    } catch (error) {
        console.error('Lỗi khi gọi API hoàn tiền:', error);
        alert('Lỗi khi hoàn tiền!');
    }
}
*/
