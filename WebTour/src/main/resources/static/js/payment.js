document.addEventListener('DOMContentLoaded', function() {
	var book_now = document.getElementById("book-now");
	book_now.addEventListener("click", function(event) {
		event.preventDefault();
		createBooking();
		console.log(1);
		
	});



})

//tạo booking
async function createBooking() {
	console.log("Hàm createBooking đã được gọi.");
	const tourId = document.getElementById("id-booking-info").getAttribute("data-id");
	const userId = 6;/*= document.getElementById("userId").value;*/
	//kiểm tra đăng nhập
	if (!userId) {
		window.location.href = `/User/login`;
		return;
	}

	/*const rawBookingDate = document.getElementById("book-day").value;
	console.log("rawBookingDate "+rawBookingDate);
	const bookingDate = new Date(rawBookingDate).toISOString().split('T')[0]; 
	console.log("bookingDate "+ bookingDate);*/
	const bookingDate = document.getElementById("book-day").value;
	console.log("bookingDate "+bookingDate);
	const adult = parseInt(document.getElementById("price-adult").innerText.replace(/[^0-9]/g, '')) || 0; // Chỉ lấy số
	const child = parseInt(document.getElementById("price-chill").innerText.replace(/[^0-9]/g, '')) || 0;
	const peopleNums = adult + child;
	console.log(peopleNums);

	const bookingData = {
		tourId: tourId,
		userId: userId,
		bookingDate: bookingDate,
		peopleNums: peopleNums
	};

	/*tạo booking khi ấn nút đặt ngay*/
	const url = `http://localhost:8080/create-booking?tourId=${tourId}&userId=${userId}&bookingDate=${bookingDate}&peopleNums=${peopleNums}`;
	const request = new Request(url, {
		method: "POST",
		headers: {
			"Content-Type": "application/json",
			/*"Content-Type": "application/x-www-form-urlencoded", */
		},
		body: JSON.stringify(bookingData)
	});
	const response = await fetch(request);
	console.log("Phản hồi từ server:", response);
	if (!response.ok) {
		console.log(response);
		return null;
	}
	const dataBooking = await response.json();
	console.log("Dữ liệu booking đã lưu: ", dataBooking);
	const bookingID = dataBooking.bookingId
	console.log("bookingID "+bookingID);

	/*mở trang payment với id của booking mới tạo*/
	window.location.href = `/payment/${bookingID}`;
}
