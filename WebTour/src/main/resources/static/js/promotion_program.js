
const promotionCode = document.getElementById("promotion-code");
const discount = document.getElementById("discount");
const startTime = document.getElementById("datetime-start");
const endTime = document.getElementById("datetime-end");
const description = document.getElementById("description");

let modal = document.getElementById('add-promotion-modal');
let close = document.getElementById('close-modal-btn');
const promotionalProgramModal = document.getElementById('add-promotion-program-modal');
const promotionalProgramCloseBtn = document.getElementById('close-promotional-program-btn');
const savePromotionDetails = document.getElementById('savePromotionDetails');
const filterPromotionByDate = document.getElementById('filter-promotion-byDate');
const datetimeStartFilter = document.getElementById('datetime-start-filter');
const datetimeEndFilter = document.getElementById('datetime-end-filter');

document.getElementById("add-promotion-program-btn").addEventListener("click", (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.add('d-flex');
});


close.addEventListener("click", (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.remove('d-flex');
	const programTitle = document.getElementById('program-title');
	const promotionType = document.querySelector('input[name="promotion"]:checked');
	const promotionValue = document.getElementById('discount');
	const startDate = document.getElementById('datetime-start');
	const endDate = document.getElementById('datetime-end');
	programTitle.value = '';
	promotionType.value = '';
	promotionValue.value = '';
	startDate.value = '';
	endDate.value = '';
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

const promotionByPercent = document.getElementById('promotion-by-percent');
const promotionByMoney = document.getElementById('promotion-by-money');
const promotionValueIcon = document.querySelector('.promotion-value-icon');

const updatePromotionIcon = () => {
	if (promotionByPercent.checked) promotionValueIcon.textContent = '%';
	else if (promotionByMoney.checked) promotionValueIcon.textContent = 'đ';
}

promotionByPercent.addEventListener('change', updatePromotionIcon);
promotionByMoney.addEventListener('change', updatePromotionIcon);

const createProgramBtn = document.getElementById('create-program-btn');
createProgramBtn.addEventListener('click', createPromotion)

async function createPromotion() {
	// Lấy dữ liệu từ form
	const programTitle = document.getElementById('program-title').value;
	const promotionType = document.querySelector('input[name="promotion"]:checked').value;
	const promotionValue = parseInt(document.getElementById('discount').value);
	const startDate = document.getElementById('datetime-start').value;
	const endDate = document.getElementById('datetime-end').value;

	if (!programTitle || !promotionValue || !startDate || !endDate) {
		alert('Vui lòng nhập đầy đủ thông tin!');
		return;
	}

	const programData = {
		title: programTitle,
		promotionType: promotionType,
		promotionValue: promotionValue,
		startDate: startDate,
		endDate: endDate
	};

	try {
		const res = await fetch(`/admin/create-promotion-program`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json"
			},
			body: JSON.stringify(programData)
		});

		if (res.ok) {
			const rs = await res.json();
			promotionalProgramModal.classList.remove('d-flex');
			if (rs.status == 200) location.reload();
		} else {
			alert('Có lỗi xảy ra khi áp dụng khuyến mãi');
		}
	} catch (error) {
		console.error('Error:', error);
		alert('Đã xảy ra lỗi khi gửi dữ liệu');
	}
}

let currentPage = 0;
let pageSize = 2;

const filterDataByDate = async (event) => {
	/*	event.preventDefault();*/

	const params = new URLSearchParams({
		startDate: datetimeStartFilter.value,
		endDate: datetimeEndFilter.value,
	});

	try {
		const res = await fetch(`/admin/get-promotion-program-by-date?${params.toString()}&page=${currentPage}&size=${pageSize}`, {
			method: "GET",
			headers: {
				"Content-Type": "application/json"
			}
		});
		if (!res.ok) {
			// Nếu mã trạng thái không phải 200
			const errorText = await res.text(); // Lấy nội dung phản hồi để xem lỗi
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

const updateDataTable = (programs) => {
	const tableBody = document.getElementById('promotion-program-table-body');
	tableBody.innerHTML = '';

	programs.forEach(program => {
		const row = document.createElement('tr');
		row.setAttribute('data-id', program.promotionId);
		row.setAttribute('onclick', 'getPromotionId(this)');

		row.innerHTML = `
					<td class="text-left">${program.title}</td>
					<td>${program.promotionType}</td>
					<td>${program.promotionValue}</td>
					<td>${program.startDate}</td>
					<td>${program.endDate}</td>
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