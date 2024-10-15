document.addEventListener('DOMContentLoaded', function() {

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
	
	var start = document.addEventListener('DOMContentLoaded',HandelStart());
	
	HandleGetDay();
})


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
	const response = await  fetch(request);
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
function HandleGetDay() {
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
			let day = end - start;

			document.getElementById("tour-day").innerHTML = day.toLocaleString() + " Day";
		})
		.catch(error => {
			console.error('There has been a problem with your fetch operation:', error);
		});
}

//xử lý khi ấn vào các ngôi sao đánh giá 
function HandelStart(){
	/*để chọn các phần tử có lớp .fa-star bên trong phần tử có lớp .stars*/
	const stars = document.querySelectorAll('.stars .fa-star');
	    stars.forEach(star => {
	        star.addEventListener('click', function() {
				//this: để truy cập đến phần tử nhận sự kiện, getAttribute :để lấy giá trị của thuộc tính data-value
				// data-value (bên html) để lưu trữ thông tin về sao khi click
	            const rate = this.getAttribute('data-value');
				if(this.classList.contains('selected')){
					resetStars(); 
				}else{
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

	
//-------------------------------------------Manh Here-------------------------------------
async function ManhHandleIncrement(elementQuantityId)
{
	const tourId = document.getElementById("id-booking-info").getAttribute("data-id");
	var quantity = document.getElementById(elementQuantityId).value;
	const tourData = await getTourData(tourId);
	if(tourData == null) return;
	
	const numAdultTicket = parseInt(document.getElementById("price-adult").innerText.replace(/[^0-9]/g, '')) || 0;
	const numChildTicket = parseInt(document.getElementById("price-chill").innerText.replace(/[^0-9]/g, '')) || 0;
	
	if(numAdultTicket + numChildTicket == tourData.peopleMax) return;
	
	document.getElementById(elementQuantityId).value = quantity + 1;
	
	
}
async function ManhHandleDescrement(elementQuantityId)
{
	const id = document.getElementById("id-booking-info").getAttribute("data-id");
	var quantity = parseInt(document.getElementById(elementQuantityId).innerText.replace(/[^0-9]/g, '')) || 0;
	
	if(quantity - 1  < 0) return;
	
	document.getElementById(elementQuantityId).value = quantity - 1;
}

async function getTourData(tourId)
{
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

async function getTicketData()
{
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

