document.addEventListener('DOMContentLoaded', function() {
	const bookingID = sessionStorage.getItem("bookingID");
	const tourID = sessionStorage.getItem("tourID");
	const userID = sessionStorage.getItem("userID");
	const totalPrice = sessionStorage.getItem("totalPrice");
	console.log("Booking ID: " + bookingID);
	console.log("Tour ID: " + tourID);
	console.log("User ID: " + userID);
	console.log("totalPrice : " + totalPrice);

	var email = document.getElementById('email').addEventListener('input', validateEmail);
	var name = document.getElementById('name').addEventListener('input', validateName);
	var phone = document.getElementById('phone').addEventListener('input', validatePhone);

	//giá hiển thị ban đầu khi chưa nhập mã giảm giá
	var temp = document.getElementById('temp-price').innerHTML = totalPrice + "₫";
	var total = document.getElementById('total-price').innerHTML = totalPrice + "₫";

	var selectProvince = document.getElementById("city").addEventListener("change", function() {
		const provinceId = this.value;
		if (provinceId) {
			getAllDistrict(provinceId);
		} else {
			document.getElementById("district").innerHTML = "<option value=''>---</option>";
			document.getElementById("ward").innerHTML = "<option value=''>---</option>";
		}
	});

	var selectDistrict = document.getElementById("district").addEventListener("change", function() {
		const districtId = this.value;
		if (districtId) {
			getAllWard(districtId);
		} else {
			document.getElementById("ward").innerHTML = "<option value=''>---</option>";
		}
	});

	var bt_apply = document.getElementById("bt-apply").addEventListener("click", function() {
		var discount_code = document.getElementById("discount-code").value;
		console.log("discount_code " + discount_code);
		//updateTotalPrice();

		if (discount_code) {
			paymentByDiscount(discount_code);
			//updateTotalPrice();
			//console.log("updateTotalPrice được gọi");
		}
		else {
			console.log("chưa có mã giảm giá.");
		}
	})

	let isPayPalInitialized = false;
	var bt_pay = document.getElementById("bt-pay").addEventListener("click", async function() {
		checkInformationInput();
		var div_paypal = document.getElementById("paypal-button-container");

		// Kiểm tra phương thức thanh toán
		const paymentMethod = checkPaymentMethod();
		console.log("Giá trị paymentMethod từ sự kiện nhấn nút: ", paymentMethod);

		if (!paymentMethod) {
			alert("Vui lòng chọn phương thức thanh toán.");
			return;
		}
		if (paymentMethod === 2) { //online
			if (!isPayPalInitialized) {
				await createPayment();
				div_paypal.style.display = "block";
				payPal();
				isPayPalInitialized = true;
			} else {
				div_paypal.style.display = "block";
			}
		} else if (paymentMethod === 1) { //cash
			div_paypal.style.display = "none";
			await createPayment();
			await updateTotalPrice();
			const bookingId = document.getElementById("id-booking").getAttribute("data-id");
			await updatePaymentStatus(bookingId);
			alert("Thanh toán thành công!");
			
			window.location.href = `/notificationSuccess/${bookingId}`;
		}
	});

	document.getElementById("paypal-button-container").style.display = "none"; // Ẩn div PayPal ban đầu


	getAllProvince();


})
// Hàm kiểm tra email
async function validateEmail() {
	const email = document.getElementById('email').value;
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	if (!emailRegex.test(email)) {
		document.getElementById('email-error').style.display = 'block';
		document.getElementById('email-error').textContent = 'Email không hợp lệ.(Ví dụ: name123@gmail.com)';
	} else {
		document.getElementById('email-error').style.display = 'none';
	}
}

// Hàm kiểm tra họ tên
async function validateName() {
	const name = document.getElementById('name').value;
	const nameRegex = /^[^0-9]+$/; // Không chứa số

	if (!nameRegex.test(name)) {
		document.getElementById('name-error').style.display = 'block';
		document.getElementById('name-error').textContent = 'Họ và tên không được chứa số';
	} else {
		document.getElementById('name-error').style.display = 'none';
	}
}

// Hàm kiểm tra số điện thoại
async function validatePhone() {
	const phone = document.getElementById('phone').value;
	const phoneRegex = /^0\d{9}$/; // 10 số và bắt đầu bằng số 0

	if (phone && !phoneRegex.test(phone)) {
		document.getElementById('phone-error').style.display = 'block';
		document.getElementById('phone-error').textContent = 'Số điện thoại không hợp lệ, cần có 10 số và bắt đầu bằng số 0';
	} else {
		document.getElementById('phone-error').style.display = 'none';
	}
}

// Hiển thi các province (tỉnh, thành) lên option
async function getAllProvince() {
	const url = `http://localhost:8080/api-get-province`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});
	try {
		const response = await fetch(request);
		if (!response.ok) {
			console.log(response);
			return null;
		}
		const dataProvince = await response.json();
		const fragment = document.createDocumentFragment();
		const citySelect = document.getElementById("city");
		dataProvince.forEach(province => {
			var option = document.createElement("option");
			option.value = province.provinceId;
			option.textContent = province.name;
			fragment.appendChild(option);
			/*citySelect.appendChild(option);*/
		})
		citySelect.appendChild(fragment);

	} catch (error) {
		console.error("Có lỗi xảy ra:", error);
	}

}

// Lấy quận theo provinceId
async function getAllDistrict(provinceId) {
	if (!provinceId) console.log("không có provinceí");
	const url = `http://localhost:8080/api-get-district/${provinceId}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});

	try {
		const response = await fetch(request);
		if (!response.ok) {
			console.log("Lỗi getAllDistrict:", response);
			return null;
		}
		const dataDistrict = await response.json();

		const districtSelect = document.getElementById("district");
		// Reset danh sách quận
		districtSelect.innerHTML = "<option value=''>---</option>";
		const fragment = document.createDocumentFragment();
		dataDistrict.forEach(district => {
			var option = document.createElement("option");
			option.value = district.districtId;
			option.textContent = district.name;
			fragment.appendChild(option);
			/*districtSelect.appendChild(option);*/
		});
		districtSelect.appendChild(fragment);
		// Reset danh sách phường khi thay đổi quận
		document.getElementById("ward").innerHTML = "<option value=''>---</option>";
	} catch (error) {
		console.error("Có lỗi xảy ra:", error);
	}
}

// Lấy phường theo districtId
async function getAllWard(districtId) {
	if (!districtId) console.log("không có districtid");
	const url = `http://localhost:8080/api-get-ward/${districtId}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});

	try {
		const response = await fetch(request);
		if (!response.ok) {
			console.log("Lỗi: getAllWard", response);
			return null;
		}
		const dataWard = await response.json();

		const wardSelect = document.getElementById("ward");
		wardSelect.innerHTML = "<option value=''>---</option>"; // Reset danh sách phường
		const fragment = document.createDocumentFragment();
		dataWard.forEach(ward => {
			var option = document.createElement("option");
			option.value = ward.wardId;
			option.textContent = ward.name;
			fragment.appendChild(option);
			/*wardSelect.appendChild(option);*/
		});
		wardSelect.appendChild(fragment);
	} catch (error) {
		console.error("Có lỗi xảy ra:", error);
	}
}

// tính tiền khi nhập mã giảm giá ở trang thanh toán
async function paymentByDiscount(code) {
	const totalPrice = localStorage.getItem("totalPrice");
	const tempPrice = parseInt(document.getElementById("temp-price").innerText.replace(/[^0-9]/g, ''));
	console.log("price " + totalPrice, tempPrice);
	/*code = document.getElementById('discount-code').value;
   console.log("code discount " + code);*/


	const url = `http://localhost:8080/api-get-promotion/${code}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});

	try {
		const response = await fetch(request);
		if (!response.ok) {
			console.log("Lỗi paymentByDiscount:", response);
			return null;
		}
		const data = await response.json();
		console.log("dataa: " + data);
		if (data) {
			const today = new Date();
			console.log("today " + today);
			const startDateString = data.startDate;
			const endDateString = data.endDate

			//ép về kiểu Date để so sánh trong js
			const startDate = new Date(startDateString);
			const endDate = new Date(endDateString);

			if (today >= startDate && today <= endDate) {
				const discount = tempPrice * (data.discount / 100);
				const total = totalPrice - discount;
				document.getElementById("total-price").innerHTML = total.toLocaleString() + "₫";
				updateTotalPrice();
			}
			else {
				console.log("Mã giảm giá đã hết hạn.");
			}

		}
		else {
			console.log("Không tìm thấy data của hàm paymentByDiscount ");
		}

	} catch (error) {
		console.error("Có lỗi xảy ra:", error);
	}

}

//kiểm tra thông tin người dùng
async function checkInformationInput() {
	let email = document.getElementById("email").value.trim();
	let name = document.getElementById("name").value.trim();
	let phone = document.getElementById("phone").value.trim();
	let address = document.getElementById("optional-address").value.trim();
	let city = document.getElementById("city").value.trim();
	let district = document.getElementById("district").value.trim();
	let ward = document.getElementById("ward").value.trim();

	// Kiểm tra nếu bất kỳ trường nào trống
	if (!email || !name || !phone || !address || !city || !district || !ward) {
		alert("Bạn chưa nhập đầy đủ thông tin!");
		return;
	}
}

//dùng để tính giá tiền khi dùng mã giảm giá
async function updateTotalPrice() {
	const bookingId = document.getElementById("id-booking").getAttribute("data-id");
	const totalPriceText = document.getElementById("total-price").innerText || document.getElementById("total-price").textContent;
	const totalPrice = parseFloat(totalPriceText.replace(/[^0-9]/g, '')) || 0;

	console.log("Booking ID:", bookingId);
	console.log("Total Price:", totalPrice);
	const url = `http://localhost:8080/api-update-totalprice?bookingId=${bookingId}&totalPrice=${totalPrice}`;
	const request = new Request(url, {
		method: "PUT",
		headers: {
			"Content-Type": "application/json",
		},

	});
	try {
		const response = await fetch(request);
		console.log("Phản hồi từ server:", response);

		if (!response.ok) {
			console.log("Có lỗi xảy ra updateTotalPrice:", response);
			return null;
		} else {
			// Kiểm tra nếu phản hồi là JSON hoặc text
			const contentType = response.headers.get("Content-Type");
			if (contentType && contentType.includes("application/json")) {
				const data = await response.json();
				console.log("Cập nhật thành công (JSON):", data);
			} else {
				const textData = await response.text();
				console.log("Cập nhật thành công (Text):", textData);
			}
		}
	} catch (error) {
		console.error("Lỗi  cập nhật updateTotalPrice:", error);
	}

}

//kiểm tra phương thức thanh toán (người dùng chọn trả online hay cash) trước khi ấn thanh toán
function checkPaymentMethod() {
	const paymentOnline = document.getElementById('payment-online');
	const paymentCash = document.getElementById('payment-cash');

	console.log("Trạng thái paymentOnline: ", paymentOnline.checked);
	console.log("Trạng thái paymentCash: ", paymentCash.checked);

	if (paymentOnline.checked) {
		console.log("paymentMethod: online");
		return 2; // Trả về phương thức thanh toán online
	} else if (paymentCash.checked) {
		console.log("paymentMethod: cash");
		return 1; // Trả về phương thức thanh toán bằng tiền mặt
	} else {
		console.log("Vui lòng chọn phương thức thanh toán");
		return null; // Trả về null nếu chưa chọn
	}
}
//tạo payment
async function createPayment() {
	const bookingId = document.getElementById("id-booking").getAttribute("data-id");
	var date = document.getElementById("date").innerText;
	console.log("date  " + date);
	const totalPriceText = document.getElementById("total-price").innerText || document.getElementById("total-price").textContent;
	const amount = parseFloat(totalPriceText.replace(/[^0-9]/g, '')) || 0;
	console.log(amount);
	const paymentOnline = document.getElementById('payment-online');
	const paymentCash = document.getElementById('payment-cash');

	// Lấy phương thức thanh toán ở đây để đảm bảo người dùng đã chọn
	const paymentMethod = checkPaymentMethod();

	// Nếu chưa chọn phương thức thanh toán thì dừng lại
	if (!paymentMethod) {
		return;  // Dừng hàm lại, cho người dùng chọn lại phương thức
	}


	const url = `http://localhost:8080/api-create-payment?bookingId=${bookingId}&paymentDate=${date}&amount=${amount}&paymentMethod=${paymentMethod}`;
	console.log(url);
	const request = new Request(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
		},

	});
	const response = await fetch(request);
	console.log("Phản hồi từ server createBooking:", response);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	const dataPayment = await response.json();
	updateTotalPrice();
	console.log("Payment đã được tạo: ", dataPayment);



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
        console.log("updatePaymentStatus "+message); 
    } catch (error) {
        console.error("Lỗi updatePaymentStatus:", error);
    }
}

//tạo thanh toán paypal
async function payPal() {
	
	const bookingId = document.getElementById("id-booking").getAttribute("data-id");
	const totalPriceText = document.getElementById('total-price').innerText;
	// Loại bỏ ký tự không phải số (ví dụ: "₫") và chuyển thành số
	const totalPrice = parseFloat(totalPriceText.replace(/[^\d.-]/g, ''));
	console.log("totalPrice trong hàm paypal " + totalPrice);
	// Chuyển đổi sang USD và lấy 2 chữ số thập phân
	const exchangeRate = 24000;
	const totalPriceUSD = (totalPrice / exchangeRate).toFixed(2);
	console.log("totalPriceUSD trong hàm paypal " + totalPriceUSD);
	paypal.Buttons({
		createOrder: function(data, actions) {
			// Tạo đơn hàng
			return actions.order.create({
				purchase_units: [{
					amount: {
						value: totalPriceUSD // Số tiền bạn muốn thanh toán (ví dụ: $10.00)
					}
				}]
			});
		},
		onApprove: function(data, actions) {
			// Thanh toán thành công
			return actions.order.capture().then(function(details) {
				alert('Thanh toán thành công, ' + details.payer.name.given_name);
				updatePaymentStatus(bookingId);
				
				window.location.href = `/notificationSuccess/${bookingId}`;
			});
		},
		onError: function(err) {
			console.log(err);
			alert('Đã xảy ra lỗi trong quá trình thanh toán.');
		}
	}).render('#paypal-button-container');

}








