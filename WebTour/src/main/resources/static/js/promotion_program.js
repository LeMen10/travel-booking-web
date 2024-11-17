
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

promotionalProgramCloseBtn.addEventListener("click", (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.remove('d-flex')
});

close.addEventListener("click", (event) => {
	event.preventDefault();
	promotionalProgramModal.classList.remove('d-flex');
	promotionCode.value = '',
		discount.value = '',
		startTime.value = '',
		endTime.value = '',
		description.value = ''
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
createProgramBtn.addEventListener('click', createNewPromotion)

async function createNewPromotion() {
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
            await res.json();
			promotionalProgramModal.classList.remove('d-flex');
        } else {
            alert('Có lỗi xảy ra khi áp dụng khuyến mãi');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Đã xảy ra lỗi khi gửi dữ liệu');
    }
}

