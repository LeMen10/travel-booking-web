document.addEventListener('DOMContentLoaded', function() {

	//xử lý khi ấn vào hình nhỏ sẽ hiện lên thành ảnh lớn trong detailTour.html
	const subImage = document.querySelectorAll(".sub-image");
	//sử dụng for để quét (lấy hết) các ảnh nhỏ và đặt sự kiện click
 	subImage.forEach(image => {
		image.addEventListener("click", function() {
			showImage(image);
		});
	});
	
	var plus = document.getElementById("bt-plus");
	plus.addEventListener("click", function() {

		HandleIncrement("value-quantity");

	});
	var sub = document.getElementById("bt-subtract");
	sub.addEventListener("click", function() {
		HandleDecrease("value-quantity");
	});

	var plus1 = document.getElementById("bt-plus1");
	plus1.addEventListener("click", function() {
		HandleIncrement("value-quantity1");

	});
	var sub1 = document.getElementById("bt-subtract1");
	sub1.addEventListener("click", function() {
		HandleDecrease("value-quantity1");
	});

	var btn_contract = document.querySelector(".btn-contract");
	btn_contract.addEventListener("click", navigateContractPage) ;
	
	var book_now = document.getElementById("book-now");
	console.log("Book Now button:", book_now);
	book_now.addEventListener("click", async function(event) {
		event.preventDefault();
		showLoading();
		const isValidQuantity = await checkQuantity();
		if (!isValidQuantity) {
			hideLoading();
			return;
		}
		await createBooking();
		console.log("Booking created");
		hideLoading();
	});

	HandleGetDay();
	openModalReview();

	var bt_send_review = document.getElementById("bt-send-review");
	bt_send_review.addEventListener("click", async function() {
		await createReview();
	});
})

let scrollPosition = 0; // Vị trí cuộn hiện tại
const scrollAmount = 160; // Độ dài mỗi lần cuộn (150px width + 10px gap)
const thumbnails = document.querySelector(".image-thumbnails");
const thumbnailContainer = document.querySelector(".thumbnail-container");

function scrollThumbnails(direction) {
    // Tổng độ rộng của tất cả các ảnh
    const thumbnailsWidth = thumbnails.scrollWidth;
    // Độ rộng hiển thị của container
    const containerWidth = thumbnailContainer.offsetWidth;

    // Tính vị trí cuộn mới
    scrollPosition += direction * scrollAmount;

    // Đảm bảo không cuộn vượt quá giới hạn
    scrollPosition = Math.max(0, Math.min(scrollPosition, thumbnailsWidth - containerWidth));

    // Cập nhật vị trí cuộn bằng CSS transform
    thumbnails.style.transform = `translateX(-${scrollPosition}px)`;
}


function navigateContractPage() {
    window.location.href = `http://localhost:8080/contract`;
}

function showLoading() {
    document.getElementById('overlay-loading').style.display = 'flex'; // Hiển thị màn hình loading
}

// Hàm ẩn màn hình loading
function hideLoading() {
    document.getElementById('overlay-loading').style.display = 'none'; // Ẩn màn hình loading
}

//xử lý dấu cộng
async function HandleIncrement(inputId) {
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	var valueInput = document.getElementById(inputId).value;
	valueInput = parseInt(valueInput);
	if (!id) {
		console.error("ID không tồn tại hoặc không hợp lệ");
	}
	const url = `http://localhost:8080/api-get-detail-tour?id=${id}`;
	console.log(url);
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "applycation/json",
		}
	});
	const response = await fetch(request);
	if (!response.ok) {
		console.log(response);
		return;
	}

	const data = await response.json();
	console.log(data);
	const peopleMax = data.peopleMax;

	var input_adult = 0;
	input_adult = parseInt(document.getElementById("value-quantity").value);
	var input_child = 0;
	input_child = parseInt(document.getElementById("value-quantity1").value);

	if (valueInput < peopleMax) {
		valueInput += 1;
		if (input_child + input_adult < peopleMax) {
			await HandlePrice(inputId, valueInput);
		} else {
			return;
		}

	} else {
		return;
	}
	document.getElementById(inputId).value = valueInput;
}

//xử lý dấu trừ
async function HandleDecrease(inputId) {
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	//inputId để lấy id của dấu cộng của 2 loại vé (trẻ em và người lớn)
	var valueInput = document.getElementById(inputId).value;
	valueInput = parseInt(valueInput);

	if (!id) {
		console.error("ID không tồn tại hoặc không hợp lệ");
	}
	const url = `http://localhost:8080/api-get-detail-tour?id=${id}`;
	console.log(url);
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "applycation/json",
		}
	});
	const response = await fetch(request);
	if (!response.ok) {
		console.log(response);
		return;
	}

	const data = await response.json();
	console.log(data);
	if (valueInput > 0) {
		valueInput -= 1;
		await HandlePrice(inputId, valueInput);
	} else {
		return;
	}
	document.getElementById(inputId).value = valueInput;
}

//tính giá tiền của từng loại vé
async function HandlePrice(inputId, valueInput) {
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	if (!id) {
		console.error("ID không tồn tại hoặc không hợp lệ");
		return;
	}
	const url = `http://localhost:8080/api-get-detail-tour?id=${id}`;
	console.log(url);
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});
	const response = await fetch(request);
	if (!response.ok) {
		console.log(response);
		return;
	}

	const tourdata = await response.json();
	const tourPrice = tourdata.price;
	const url_get_ticket = `http://localhost:8080/api-get-ticket`;
	console.log(url_get_ticket);
	const ticketRequest = new Request(url_get_ticket, {
		method: "GET",
		headers: {
			"Content-Type": "application/json",
		}
	});
	const ticketResponse = await fetch(ticketRequest)
	if (!ticketResponse.ok) {
		console.log(ticketResponse)
		return;
	}

	ticketData = await ticketResponse.json();
	console.log(ticketData);
	let price_adult = 0;
	let price_chill = 0;

	if (inputId === "value-quantity") {
		price_adult = tourPrice * valueInput;
		document.getElementById("price-adult").innerHTML = price_adult.toLocaleString() + "₫";

	} else if (inputId === "value-quantity1") {
		price_chill = tourPrice * valueInput * 0.5;
		document.getElementById("price-chill").innerHTML = price_chill.toLocaleString() + "₫";
	}
	/*valueInput -= 1;*/

	const adult = parseInt(document.getElementById("price-adult").innerText.replace(/[^0-9]/g, '')) || 0; // Chỉ lấy số
	const child = parseInt(document.getElementById("price-chill").innerText.replace(/[^0-9]/g, '')) || 0;
	let total = adult + child;
	document.getElementById("total-price").innerHTML = total.toLocaleString() + "₫";
}

//xử lý số ngày hiển thị 
async function HandleGetDay() {
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	var tour_day = document.getElementById("tour-day").value;


	if (!id) {
		console.error("ID không tồn tại hoặc không hợp lệ");
		return;
	}
	const url = `http://localhost:8080/api-get-detail-tour?id=${id}`;
	console.log(url);
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "applycation/json",
		}
	});
	fetch(request).then(response => {
		if (!response.ok) {
			throw new Error('Lỗi mạng xảy ra khi lấy day');
		}
		return response.json();
	})
		.then(data => {
			const startDay = data.startDate;
			const endDay = data.end_Date;
			let start = startDay.split("-")[2];
			let end = endDay.split("-")[2];
			let day = end - start + 1;

			document.getElementById("tour-day").innerHTML = day.toLocaleString() + " Day";
		})
		.catch(error => {
			console.error('There has been a problem with your fetch operation:', error);
		});
}

//xử lý khi ấn vào các ngôi sao đánh giá 
async function HandelStart() {
	/*để chọn các phần tử có lớp .fa-star bên trong phần tử có lớp .stars*/
	const stars = document.querySelectorAll('.stars .fa-star');
	stars.forEach(star => {
		star.addEventListener('click', function() {
			//this: để truy cập đến phần tử nhận sự kiện, getAttribute :để lấy giá trị của thuộc tính data-value
			// data-value (bên html) để lưu trữ thông tin về sao khi click
			const rate = this.getAttribute('data-value');
			console.log("Clicked on star with value: " + rate);
			if (this.classList.contains('selected')) {
				resetStars();
			} else {
				resetStars();
				paintStars(rate);
			}

		});
	});
	// selected ở bên file css (dòng 27), dùng để tô màu vàng khi chọn 
	function resetStars() {
		stars.forEach(star => {
			star.classList.remove('selected');
		});
	}
	// selected ở bên file css (dòng 27), dùng để tô màu vàng khi chọn
	function paintStars(count_start) {
		for (let i = 0; i < count_start; i++) {
			stars[i].classList.add('selected');
		}
	}
}

//kiểm tra người dùng có chọn số lượng chưa
async function checkQuantity() {
	var valueInputAdult = document.getElementById("value-quantity").value;
	var valueInputChild = document.getElementById("value-quantity1").value;
	// Chuyển đổi thành số nguyên, nếu không hợp lệ thì đặt về 0
	valueInputAdult = parseInt(valueInputAdult) || 0;
	valueInputChild = parseInt(valueInputChild) || 0;
	let quantity = valueInputAdult + valueInputChild;

	// Kiểm tra nếu tổng số lượng <= 0
	if (quantity <= 0) {
		alert("Mời nhập số lượng vé hợp lệ");
		return false; // Trả về false nếu số lượng không hợp lệ
	}
	return true;
}

//tạo booking khi ấn nút đặt ngay(lưu bookingID) sau đó chuyển đến trang payment
async function createBooking() {
	console.log("Hàm createBooking đã được gọi.");
	const tourId = document.getElementById("id-booking-info").getAttribute("data-id");

	const account = await getProfile();
	const userId = account == null ? null : account.user.user_id;

	const today = new Date().toISOString().split('T')[0]; // lấy định dạng yyyy-MM-dd
	const bookingDate = today;
	console.log("bookingDate " + bookingDate);
	const adult = parseInt(document.getElementById("value-quantity").value.replace(/[^0-9]/g, '')) || 0; // Chỉ lấy số
	const child = parseInt(document.getElementById("value-quantity1").value.replace(/[^0-9]/g, '')) || 0;

	const peopleNums = adult + child;
	console.log(peopleNums);

	const totalPriceText = document.getElementById("total-price").innerText;
	console.log("Giá trị của totalPriceText:", totalPriceText);
	const totalPrice = parseFloat(totalPriceText.replace(/\./g, '').replace(/[^\d]/g, '')) || 0;
	console.log("Tổng giá sau khi chuyển đổi:", totalPrice);

	/*tạo booking khi ấn nút đặt ngay*/
	const url = `http://localhost:8080/create-booking?tourId=${tourId}&userId=${userId}&bookingDate=${bookingDate}&peopleNums=${peopleNums}&quantityAdult=${adult}&quantityChild=${child}&totalPrice=${totalPrice}`;
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
	const dataBooking = await response.json();
	console.log("Dữ liệu booking đã lưu: ", dataBooking);
	const bookingID = dataBooking.bookingId
	console.log("bookingID " + bookingID);
	console.log(totalPrice);
	
	sessionStorage.setItem("bookingID", bookingID);
	sessionStorage.setItem("tourID", tourId);
	sessionStorage.setItem("totalPrice", totalPrice);
	//kiểm tra đăng nhập
	if (!userId) {
		sessionStorage.setItem("bookingID", bookingID);
		window.location.href = `/`;
		return;
	}

	sessionStorage.setItem("bookingID", bookingID);
	sessionStorage.setItem("tourID", tourId);
	sessionStorage.setItem("totalPrice", totalPrice);
	/*mở trang payment với id của booking mới tạo*/
	window.location.href = `/payment/${bookingID}`;
}

function showContent(contentId) {

	// Ẩn tất cả các nội dung tab
	var tabContents = document.getElementsByClassName('tab-content');
	for (var i = 0; i < tabContents.length; i++) {
		tabContents[i].classList.remove('active-content');
	}

	// Hiện nội dung của tab được chọn
	document.getElementById(contentId).classList.add('active-content');

	// Đặt lại trạng thái active cho tab
	var tabs = document.getElementsByClassName('tab');
	for (var i = 0; i < tabs.length; i++) {
		tabs[i].classList.remove('active');
	}
	event.target.classList.add('active');
}

//ẩn hiện modal đánh giá ở tap đánh giá tour khi ấn gửi đánh giá của bạn
function openModalReview() {

	document.getElementById('openReviewModal').addEventListener('click', function() {
		document.getElementById('reviewContainer').style.display = 'block';
		document.getElementById('overlay').style.display = 'block'; // Hiển thị modal
		HandelStart.re
		HandelStart();

	});

	document.getElementById('closeReviewModal').addEventListener('click', function() {
		document.getElementById('reviewContainer').style.display = 'none';
		document.getElementById('overlay').style.display = 'none'; // Ẩn overlay
	});

	// Để đóng modal khi nhấn ra ngoài modal (nếu cần)
	window.onclick = function(event) {
		if (event.target == document.getElementById('overlay')) {
			document.getElementById('reviewContainer').style.display = 'none';
			document.getElementById('overlay').style.display = 'none'; // Ẩn overlay
		}
	};
}

//tạo review 
async function createReview() {
	console.log("Hàm createReview đã được gọi.");
	const tourId = document.getElementById("id-booking-info").getAttribute("data-id");
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");/*= document.getElementById("userId").value;*/
	const stars = document.querySelectorAll('.stars .fa-star.selected');
	const rate = stars.length;
	console.log("value của sao đã chọn: " + rate);
	const comment = document.getElementById('textarea-review').value;
	const reviewDate = new Date().toISOString().split('T')[0];

	const url = `http://localhost:8080/create-review?userId=${userId}&tourId=${tourId}&rate=${rate}&comment=${comment}&reviewDate=${reviewDate}`;
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
	const dataReview = await response.json();
	console.log("Đánh giá đã được gửi:", dataReview);

	// Load lại trang sau khi gửi đánh giá thành công
	location.reload();
}

//xử lý khi ấn vào hình nhỏ sẽ hiện lên thành ảnh lớn trong detailTour.html
function showImage(image) {
	const mainImage = document.getElementById("main-image");
	//thay đổi ảnh cần thay đổi src và alt
	mainImage.src = image.src;  
	mainImage.alt = image.alt;  
}

//-------------------------------------------Manh Here-------------------------------------
async function ManhHandleIncrement(elementQuantityId) {
	const tourId = document.getElementById("id-booking-info").getAttribute("data-id");
	var quantity = document.getElementById(elementQuantityId).value;
	const tourData = await getTourData(tourId);
	if (tourData == null) return;

	const numAdultTicket = parseInt(document.getElementById("price-adult").innerText.replace(/[^0-9]/g, '')) || 0;
	const numChildTicket = parseInt(document.getElementById("price-chill").innerText.replace(/[^0-9]/g, '')) || 0;

	if (numAdultTicket + numChildTicket == tourData.peopleMax) return;

	document.getElementById(elementQuantityId).value = quantity + 1;


}
async function ManhHandleDescrement(elementQuantityId) {
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	var quantity = parseInt(document.getElementById(elementQuantityId).innerText.replace(/[^0-9]/g, '')) || 0;

	if (quantity - 1 < 0) return;

	document.getElementById(elementQuantityId).value = quantity - 1;
}

async function getTourData(tourId) {
	const url = `http://localhost:8080/api-get-detail-tour?id=${id}`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "applycation/json",
		}
	});
	const response = await fetch(request);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	return await response.json();
}

async function getTicketData() {
	const url = `http://localhost:8080/api-get-ticket`;
	const request = new Request(url, {
		method: "GET",
		headers: {
			"Content-Type": "applycation/json",
		}
	});
	const response = await fetch(request);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	return await response.json();
}
//-------------------------------------------Manh Here-------------------------------------

