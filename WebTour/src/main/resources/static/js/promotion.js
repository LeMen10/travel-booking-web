
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
const createPromotionBtn = document.getElementById('add-promotion-btn');
const confirmModalBtn = document.getElementById('btn-confirm-modal');

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

createPromotionBtn.addEventListener("click", async (event) => {
	event.preventDefault();
	modal.classList.add('d-flex');
	confirmModalBtn.setAttribute("onclick", "createPromotion()");
});

promotionalProgramCloseBtn.addEventListener("click", async (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.remove('d-flex')
});

close.addEventListener("click", async (event) => {
	event.preventDefault();
	const promotionCodeInput = document.getElementById("promotion-code");
	promotionCodeInput.disabled = false;
	const randomStringElement = document.getElementById("random-string");
	randomStringElement.classList.remove("disabled");
	modal.classList.remove('d-flex');
	promotionCode.value = '';
	discount.value = '';
	startTime.value = '';
	endTime.value = '';
	description.value = '';
	cumulativePoints.value = '';

});

const createPromotion = async (event) => {

	const data = {
		code: promotionCode.value,
		discount: discount.value,
		startDate: startTime.value,
		endDate: endTime.value,
		description: description.value,
		cumulativePoints: cumulativePoints.value
	};

	try {
		const res = await fetch("/admin/promotion-add", {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(data)
		});

		if (!res.ok) {
			if (res.status == 400) return alert("Mã khuyến mãi bị trùng.");
		}

		const rs = await res.json();
		if (rs.status == 200) location.reload();

	} catch (error) {
		console.log(error);
		alert("Đã xảy ra lỗi khi gửi dữ liệu");
	}
};

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
	const promotionId = getParentRowId(row)
	selectedPromotionId = promotionId;
	console.log("Selected Promotion ID:", promotionId);

	promotionalProgramModal.classList.add('d-flex');

	loadTours();
};

let selectedTourId;

const applyPromotion = async (tourId, event) => {
	event.preventDefault();
	selectedTourId = tourId;

	const data = {
		promotions: {
			promotionId: selectedPromotionId
		},
		tourId: selectedTourId,
		status: true
	};

	try {
		const res = await fetch("/admin/promotion-detail-add", {
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

let currentPage = 0;
let pageSize = 2;

const filterDataByDate = async (event) => {
	/*	event.preventDefault();*/

	const params = new URLSearchParams({
		startDate: datetimeStartFilter.value,
		endDate: datetimeEndFilter.value,
	});

	try {
		const res = await fetch(`/admin/get-promotion-by-date?${params.toString()}&page=${currentPage}&size=${pageSize}`, {
			method: "GET",
			headers: {
				"Content-Type": "application/json"
			}
		});
		if (!res.ok) {
			const errorText = await res.text();
			throw new Error(`HTTP error! status: ${res.status}, message: ${errorText}`);
		}
		const data = await res.json();
		console.log(data.content)

		const totalPages = data.totalPages;

		updateDataTable(data.content);
		renderPaginationControls(totalPages);

	} catch (error) {
		console.error('Error:', error);
		alert('Đã xảy ra lỗi khi gửi dữ liệu');
	}

};

filterPromotionByDate.addEventListener("click", () => {
	filterDataByDate();
	const fullUrl = window.location.href;
	const baseUrl = fullUrl.split('?')[0];
	window.history.replaceState({}, document.title, baseUrl);
});

const updateDataTable = (promotions) => {
	const tableBody = document.getElementById('promotion-table-body');
	tableBody.innerHTML = '';

	promotions.forEach(promotion => {
		const row = document.createElement('tr');
		row.setAttribute('data-id', promotion.promotionId);
		row.addEventListener('click', (event) => {
			// Kiểm tra nếu click xảy ra trên phần tử chứa các nút "Sửa" hoặc "Xóa"
			if (event.target.id === 'promotion-edit-btn' || event.target.id === 'promotion-delete-btn' || event.target.id === 'action') return;
			getPromotionId(row);
		});

		row.innerHTML = `
					<td>${promotion.code}</td>
					<td class="text-left">${promotion.description}</td>
					<td>${promotion.discount}%</td>
					<td>${promotion.startDate}</td>
					<td>${promotion.endDate}</td>
					<td id="action">
						<span id="promotion-edit-btn" onclick="showPromotionById(this)">Sửa</span> 
						<span id="promotion-delete-btn" onclick="deletePromotion(this)">Xóa</span>
					</td>
				`;

		tableBody.appendChild(row);
	});
};


const renderPaginationControls = (totalPages) => {
	const paginationContainer = document.getElementById("pagination");
	paginationContainer.innerHTML = ""; // Xóa các nút cũ

	// Tạo nút "Prev"
	if (currentPage > 0) {
		const prevButton = document.createElement("button");
		prevButton.className = "pagination-button";
		prevButton.innerHTML = '<i class="fas fa-chevron-left icon"></i>';
		prevButton.addEventListener("click", () => {

			currentPage--;
			filterDataByDate();
		});
		paginationContainer.appendChild(prevButton);
	}

	// Trang đầu tiên
	const firstPageButton = document.createElement("button");
	firstPageButton.className = "pagination-button";
	firstPageButton.textContent = 1;
	if (currentPage === 0) {
		firstPageButton.classList.add("active");
	}
	firstPageButton.addEventListener("click", () => {
		currentPage = 0;
		filterDataByDate();
	});
	paginationContainer.appendChild(firstPageButton);

	// Dấu "..." trước dải trang giữa
	if (currentPage > 2) {
		const dotsBefore = document.createElement("span");
		dotsBefore.textContent = "...";
		dotsBefore.className = "pagination-dots";
		paginationContainer.appendChild(dotsBefore);
	}

	// Các trang giữa
	for (let i = Math.max(1, currentPage - 1); i <= Math.min(totalPages - 2, currentPage + 1); i++) {
		const middlePageButton = document.createElement("button");
		middlePageButton.className = "pagination-button";
		middlePageButton.textContent = i + 1;

		if (i === currentPage) {
			middlePageButton.classList.add("active");
		}

		middlePageButton.addEventListener("click", () => {
			currentPage = i;
			filterDataByDate();
		});

		paginationContainer.appendChild(middlePageButton);
	}

	// Dấu "..." sau dải trang giữa
	if (currentPage < totalPages - 3) {
		const dotsAfter = document.createElement("span");
		dotsAfter.textContent = "...";
		dotsAfter.className = "pagination-dots";
		paginationContainer.appendChild(dotsAfter);
	}

	// Trang cuối cùng
	if (totalPages > 1) {
		const lastPageButton = document.createElement("button");
		lastPageButton.className = "pagination-button";
		lastPageButton.textContent = totalPages;

		if (currentPage === totalPages - 1) {
			lastPageButton.classList.add("active");
		}

		lastPageButton.addEventListener("click", () => {
			currentPage = totalPages - 1;
			filterDataByDate();
		});

		paginationContainer.appendChild(lastPageButton);
	}

	// Tạo nút "Next"
	if (currentPage < totalPages - 1) {
		const nextButton = document.createElement("button");
		nextButton.className = "pagination-button";
		nextButton.innerHTML = '<i class="fas fa-chevron-right icon"></i>';
		nextButton.addEventListener("click", () => {

			currentPage++;
			filterDataByDate();
		});
		paginationContainer.appendChild(nextButton);
	}
}

const getParentRowId = (element) => {
	const row = element.closest("tr"); // Tìm thẻ <tr> cha
	return row ? row.getAttribute("data-id") : null; // Lấy giá trị data-id
}

// Sửa
const showPromotionById = async (element) => {
	const promotionId = getParentRowId(element);
	const promotionCodeInput = document.getElementById("promotion-code");
	promotionCodeInput.disabled = true;
	const randomStringElement = document.getElementById("random-string");
	randomStringElement.classList.add("disabled");
	randomStringElement.onclick = null;
	try {
		const res = await fetch(`/admin/get-promotion-by-id?id=${promotionId}`, {
			method: "GET",
			headers: { "Content-Type": "application/json" }
		});

		if (res.ok) {
			const data = await res.json();
			console.log("Promotion Details:", data);
			confirmModalBtn.setAttribute("onclick", "edit()");

			// Hiển thị thông tin promotion trên giao diện
			modal.classList.add('d-flex');
			promotionCode.value = data.code;
			discount.value = data.discount;
			startTime.value = data.startDate;
			endTime.value = data.endDate;
			description.value = data.description;
			cumulativePoints.value = data.cumulativePoints;
		} else {
			const error = await res.text();
			console.error("Error:", error);

			// Hiển thị lỗi trên giao diện
			alert(`Lỗi: ${error}`);
		}
	} catch (err) {
		console.error("Error fetching promotion:", err);
		alert("Đã xảy ra lỗi khi kết nối đến máy chủ.");
	}
}
const edit = async () => {

	const data = {
		code: promotionCode.value,
		discount: discount.value,
		startDate: startTime.value,
		endDate: endTime.value,
		description: description.value,
		cumulativePoints: cumulativePoints.value
	};

	try {
		const res = await fetch("/admin/promotion-edit", {
			method: "PUT",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(data)
		});

		if (!res.ok) {
			if (res.status == 400) return alert("Mã khuyến mãi bị trùng.");
		}

		const rs = await res.json();
		if (rs.status == 200) location.reload();

	} catch (error) {
		console.log(error);
		alert("Đã xảy ra lỗi khi gửi dữ liệu");
	}

}
// Hàm xử lý logic khi nhấn "Xóa"
const deletePromotion = async (element) => {
	const promotionId = getParentRowId(element);

	if (confirm(`Bạn có chắc chắn muốn xóa mã khuyến mãi này không?`)) {
		try {
			const data = { promotionId };

			const res = await fetch("/admin/promotion-delete", {
				method: "DELETE",
				headers: {
					"Content-Type": "application/json"
				},
				body: JSON.stringify(data)
			});

			const rs = await res.json();
			if (rs.status === 200) location.reload();
			else alert(rs.message || "Không thể xóa khuyến mãi.");

		} catch (error) {
			console.log(error);
			alert("Không được phép xóa");
		}
	}
};
