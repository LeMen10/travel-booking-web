
const promotionCode = document.getElementById("promotion-code");
const discount = document.getElementById("discount");
const startTime = document.getElementById("datetime-start");
const endTime = document.getElementById("datetime-end");
const description = document.getElementById("description");
const cumulativePoints = document.getElementById("cumulative-points");
let randomString = document.getElementById('random-string');
let modal = document.getElementById('add-promotion-modal');
let close = document.getElementById('close-modal-btn');
const promotionalProgramModal = document.getElementById('promotional-program-modal');
const promotionalProgramCloseBtn = document.getElementById('close-promotional-program-btn');
const savePromotionDetails = document.getElementById('savePromotionDetails');
const filterPromotionByDate = document.getElementById('filter-promotion-byDate');
const datetimeStartFilter = document.getElementById('datetime-start-filter');
const datetimeEndFilter = document.getElementById('datetime-end-filter');

const generateRandomString = (length) => {
	const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
	let result = '';

	for (let i = 0; i < length; i++) {
		const randomIndex = Math.floor(Math.random()
			* characters.length);
		result += characters[randomIndex];
	}
	return result;
}

randomString.addEventListener('click', () => {
	promotionCode.value = generateRandomString(12);
});

document.getElementById("add-promotion-btn").addEventListener("click", async (event) => {
	event.preventDefault();
	modal.classList.add('d-flex');
});

promotionalProgramCloseBtn.addEventListener("click", async (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.remove('d-flex')
});

close.addEventListener("click", async function(event) {
	event.preventDefault();
	modal.classList.remove('d-flex');
	promotionCode.value = '',
		discount.value = '',
		startTime.value = '',
		endTime.value = '',
		description.value = '',
		cumulativePoints = ''
});

document.getElementById("getData").addEventListener("click", async (event) => {
	event.preventDefault();

	const data = {
		code: promotionCode.value,
		discount: discount.value,
		startDate: startTime.value,
		endDate: endTime.value,
		description: description.value,
		cumulativePoints: cumulativePoints.value
	};

	try {
		const response = await fetch("/admin/add-promotion", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(data)
		});

		if (!response.ok) {
			if (response.status == 400) return alert("Mã khuyến mãi bị trùng.");
		}

		const rs = await response.json();
		if (rs.status == 200) location.reload();

	} catch (error) {
		console.log(error);
		alert("Đã xảy ra lỗi khi gửi dữ liệu");
	}
});

var selectedPromotionId;

const loadTours = async () => {
	try {
		const res = await fetch(`/admin/get-tours?promotionId=${selectedPromotionId}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		});

		if (res.ok) {
			const data = await res.json();
			const tours = data.tours;
			const toursBody = document.getElementById('tours-body');
			toursBody.innerHTML = '';

			tours.forEach(tour => {
				const row = document.createElement('tr');

				row.innerHTML = `
                            <tr data-id="${tour.tourId}" onclick="getPromotionId(this)">
                                <td class="text-left">${tour.tourName}</td>
                                <td>${tour.price}</td>
                                <td>${tour.startDate}</td>
                                <td>${tour.endDate}</td>
                                <td>
                                    <button onclick="applyPromotion(${tour.tourId}, event)" class="btn-confirm">Áp dụng</button>
                                </td>
                            </tr>
                        `;

				toursBody.appendChild(row);
			});
		} else {
			console.error('Failed to load promotions');
		}
	} catch (error) {
		console.error('Error:', error);
	}
};


const getPromotionId = (row) => {
	selectedPromotionId = row.getAttribute("data-id");
	console.log("Selected Promotion ID:", selectedPromotionId);

	promotionalProgramModal.classList.add('d-flex');

	loadTours();
};

let selectedTourId;

const applyPromotion = async (tourId, event) => {
	event.preventDefault();
	selectedTourId = tourId;
	console.log("Selected Tour ID:", selectedTourId);
	console.log("Selected promotion ID:", selectedPromotionId);

	const data = {
	    promotions: {
	        promotionId: selectedPromotionId
	    },
	    tourId: selectedTourId,
	    status: true
	};

	try {
		const res = await fetch("/admin/add-promotion-detail", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(data)
		});

		console.log(res)

		if (res.ok) {
			alert('Đã áp dụng khuyến mãi cho tour thành công!');
			const button = event.target;
			button.disabled = true;
			button.innerText = 'Đã áp dụng';
			button.classList.add('applied');
		} else {
			alert('Có lỗi xảy ra khi áp dụng khuyến mãi');
		}
	} catch (error) {
		console.error('Error:', error);
		alert('Đã xảy ra lỗi khi gửi dữ liệu');
	}


};

savePromotionDetails.addEventListener('click', () => {
	promotionalProgramModal.classList.remove('d-flex')
})


filterPromotionByDate.addEventListener("click", async (event) => {
	event.preventDefault();

	const params = new URLSearchParams({
		startDate: datetimeStartFilter.value,
		endDate: datetimeEndFilter.value,
	});

	try {
		const res = await fetch(`/admin/get-promotion-by-date?${params.toString()}`, {
			method: "GET",
			headers: {
				"Content-Type": "application/json"
			}
		});

		console.log(res)

		if (res.ok) {
			const data = await res.json();
			loadPromotions(data.promotions)
			console.log("Promotions:", data.promotions);
		} else alert('Có lỗi xảy ra khi áp dụng khuyến mãi');

	} catch (error) {
		console.error('Error:', error);
		alert('Đã xảy ra lỗi khi gửi dữ liệu');
	}

});

const loadPromotions = (promotions) => {
	const tableBody = document.getElementById('promotion-table-body');
	tableBody.innerHTML = '';

	promotions.forEach(promotion => {
		const row = document.createElement('tr');
		row.setAttribute('data-id', promotion.promotionId);
		row.setAttribute('onclick', 'getPromotionId(this)');

		row.innerHTML = `
					<td>${promotion.code}</td>
					<td class="text-left">${promotion.description}</td>
					<td>${promotion.discount}%</td>
					<td>${promotion.startDate}</td>
					<td>${promotion.endDate}</td>
				`;

		tableBody.appendChild(row);
	});
};

/*let currentPage = 0;
let pageSize = 5;

async function FilterDataTable() {
	const userId = sessionStorage.getItem("userId") == null ? 0 : sessionStorage.getItem("userId");
	const paymentStatus = document.getElementById("payment-status").value;
	const btnPay = document.getElementById("btn-payment-status");
	console.log("paymentStatus " + paymentStatus);
	const url = `http://localhost:8080/account/filter-get-order?userId=${userId}&paymentStatus=${paymentStatus}&page=${currentPage}&size=${pageSize}`;
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
	//console.log(dataFilter[0][3]);

	console.log("dataFilter ", dataFilter);
	const totalPages = dataFilter.totalPages;

	updateDataTable(dataFilter.content);
	renderPaginationControls(totalPages);

}*/