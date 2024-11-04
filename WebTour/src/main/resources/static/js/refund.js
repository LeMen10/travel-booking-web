const CLIENT_ID = "AU2triLtOulok7sB9BuFfWxVK2XlCCu1nnJDYJF-AA2PJmISoEBqtd0DbutWL81p-9EEFcfmVRq6DpU-";
const CLIENT_SECRET = "EC9EpOHMkwhIHZ4so36WAdFhs_HaYUB5APa1UiBolPddVkJFdXu7Wkf_id5m5yujr27_mxkZFs6ASY9p";

document.addEventListener("DOMContentLoaded", function() {
	const refundButton = document.querySelector(".btn-continue");
	const backButton = document.querySelector(".btn-back");

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
