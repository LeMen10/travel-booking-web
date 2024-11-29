const CLIENT_ID = "AU2triLtOulok7sB9BuFfWxVK2XlCCu1nnJDYJF-AA2PJmISoEBqtd0DbutWL81p-9EEFcfmVRq6DpU-";
const CLIENT_SECRET = "EC9EpOHMkwhIHZ4so36WAdFhs_HaYUB5APa1UiBolPddVkJFdXu7Wkf_id5m5yujr27_mxkZFs6ASY9p";

document.addEventListener("DOMContentLoaded", function() {
	window.onload = function() {
		console.log("CryptoJS has been loaded successfully.");
		// Giờ bạn có thể gọi refundMoMo()
	}
	const refundButton = document.querySelector(".btn-continue");
	const backButton = document.querySelector(".btn-back");
	const Button = document.querySelector(".btn");


	Button.addEventListener("click", async () => {
		await handleRefundMoMo();
	});

	refundButton.addEventListener("click", async () => {
		await handleRefund();
	});

	backButton.addEventListener("click", () => {
		history.go(-1);
	});
});

async function handleRefund() {
	const bookingId = document.getElementById("booking-id").getAttribute("data-id");
	const booking = await getBooking(bookingId);
	const accessToken = await getAccessToken();
	const captureId = booking.payment.captureId;
	const amount = booking.payment.totalPriceDolar;
	const refundPaypal = await refundPayment(accessToken, captureId, amount);
	if (refundPaypal) {
		if (await updateCancelBooking(bookingId)) {
			openDialogSuccess('Refund successful!');
			hideRefundButton();
		}
		else openDialogError("Refund fail!");

	}
}

async function getBooking(bookingId) {
	const response = await fetch(`http://localhost:8080/get-booking/${bookingId}`, {
		method: 'GET',
		headers: {
			"Content-Type": "application/json",
		}
	});

	const data = await response.json();
	console.log(data.payment);
	return data;
}

async function getAccessToken() {
	const response = await fetch('https://api-m.sandbox.paypal.com/v1/oauth2/token', {
		method: 'POST',
		headers: {
			'Authorization': 'Basic ' + btoa(`${CLIENT_ID}:${CLIENT_SECRET}`),
			'Content-Type': 'application/x-www-form-urlencoded'
		},
		body: 'grant_type=client_credentials'
	});

	const data = await response.json();
	return data.access_token;
}

async function refundPayment(accessToken, captureId, amount) {
	const response = await fetch(`https://api-m.sandbox.paypal.com/v2/payments/captures/${captureId}/refund`, {
		method: 'POST',
		headers: {
			'Authorization': `Bearer ${accessToken}`,
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			amount: {
				value: amount,
				currency_code: 'USD'
			}
		})
	});

	if (!response.ok) {
		return false;
	}
	return true;
}

async function updateCancelBooking(bookingId) {
	const response = await fetch(`http://localhost:8080/api-refund-booking/${bookingId}`, {
		method: 'PUT',
		headers: {
			"Content-Type": "application/json",
		}
	});
	return response.ok;
}

function hideRefundButton() {
	document.getElementById("button-refund").style.display = "none";
	document.getElementById("icon-status").classList.remove(...element.classList);
	document.getElementById("icon-status").classList.add("fa-solid", "fa-ban");
	document.getElementById("icon-status").style.color = "#ff4242";
}

/*===========================================================================================================================*/
async function refundMoMo(originalOrderId, amount) {
    if (!originalOrderId || !amount) {
        console.error('orderId và amount là bắt buộc.');
        alert('orderId và amount là bắt buộc.');
        return false;
    }

    if (amount < 1000 || amount > 50000000) {
        console.error('Số tiền không hợp lệ. Phải từ 1000 VND đến 50000000 VND.');
        alert('Số tiền hoàn tiền không hợp lệ. Vui lòng kiểm tra lại.');
        return false;
    }

    const partnerCode = 'MOMO';
    const accessKey = 'F8BBA842ECF85';
    const secretKey = 'K951B6PE1waDMi640xX08PD3vg6EkVlz';
    const requestId = `${partnerCode}${Date.now()}`;
    const orderId = `${originalOrderId}-REFUND-${Date.now()}`; // Đảm bảo orderId duy nhất
    const description = 'Hoàn tiền giao dịch';

    const rawSignature = `accessKey=${accessKey}&amount=${amount}&description=${description}&orderId=${orderId}&partnerCode=${partnerCode}&requestId=${requestId}`;
    console.log("Raw Signature:", rawSignature);

    if (typeof CryptoJS === 'undefined') {
        console.error("CryptoJS không được tải đúng cách.");
        alert("Lỗi: CryptoJS không được tải đúng cách.");
        return false;
    }

    const signature = CryptoJS.HmacSHA256(rawSignature, secretKey).toString(CryptoJS.enc.Hex);
    console.log("Chữ ký:", signature);

    try {
        const response = await fetch('http://localhost:3000/refund-payment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                orderId: orderId,
                amount: amount,
                signature: signature,
            }),
        });

        const data = await response.json();
        console.log('Phản hồi từ API:', data);

        if (data.resultCode === 0) {
            alert('Hoàn tiền thành công!');
            return true;
        } else if (data.resultCode === 41) {
            console.error('Hoàn tiền thất bại: OrderId đã tồn tại.', data.message);
            alert(`Hoàn tiền thất bại: ${data.message}\nVui lòng kiểm tra hoặc sử dụng một OrderId khác.`);
            return false;
        } else {
            alert(`Hoàn tiền thất bại: ${data.message}`);
            return false;
        }
    } catch (error) {
        console.error('Lỗi khi gọi API hoàn tiền:', error);
        alert('Lỗi kết nối! Vui lòng thử lại sau.');
        return false;
    }
}






async function handleRefundMoMo() {
	try {
		const bookingId = document.getElementById("booking-id").getAttribute("data-id");
		const booking = await getBooking(bookingId);
		const amount = booking.payment.amount; // Đảm bảo giá trị này là chính xác

		// Gọi hàm refundMoMo để hoàn tiền
		const refundSuccess = await refundMoMo(bookingId, amount);

		if (refundSuccess) {
			// Tiến hành hủy booking nếu hoàn tiền thành công
			const cancelBookingResult = await updateCancelBooking(bookingId);
			if (cancelBookingResult) {
				openDialogSuccess('Refund successful!');
				hideRefundButton();
			} else {
				openDialogError('Refund failed!');
			}
		}
	} catch (error) {
		console.error('Lỗi khi xử lý hoàn tiền:', error);
		openDialogError('Lỗi khi xử lý hoàn tiền!');
	}
}

/*===========================================================================================================================*/



