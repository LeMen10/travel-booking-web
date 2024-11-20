document.addEventListener('DOMContentLoaded', function() {
	// Mở modal khi nhấn nút "Tạo lịch trình"
	document.querySelector('.btn-insert-schedule').addEventListener('click', openCreateScheduleModal);

	// Mở modal khi nhấn nút "sửa lịch trình"
	//document.getElementById('btEditScheduleInTable').addEventListener('click', openEditScheduleModal);

	// Đóng modal khi nhấn nút Cancel
	document.querySelector('#createModal .btn-cancel').addEventListener('click', closeModal);
	document.querySelector('.btn-cancel').addEventListener('click', closeModal);

	// Đóng modal khi nhấn vào overlay
	document.querySelector('#modalOverlay').addEventListener('click', closeModal);

	//tạo mới lịch
	var btnOkInsertSchedule = document.getElementById("btn-Ok-Insert-Schedule").addEventListener('click', createSchedule);

	// Thêm sự kiện click cho tất cả nút sửa trong bảng
	document.querySelectorAll('.bt-edit-in-table').forEach(button => {
		button.addEventListener('click', function(event) {
			openEditScheduleModal(event);
		});
	});

	//sửa lịch trình
	var btnOkEditSchedule = document.getElementById("btn-Ok-Update-Schedule").addEventListener('click', updateSchedule);

	//xóa lịch trình
	var deleteButtons = document.querySelectorAll(".btDelete-in-table");
	deleteButtons.forEach(function(button) {
		button.addEventListener('click', function(event) {
			Swal.fire({
				title: 'Bạn có chắc chắn muốn xóa lịch trình này không?',
				text: 'Lịch trình sẽ không thể phục hồi sau khi xóa!',
				icon: 'warning',
				showCancelButton: true,
				confirmButtonText: 'Xóa',
				cancelButtonText: 'Hủy',
				reverseButtons: true
			}).then((result) => {
				// xác nhận xóa (isConfirmed nút OK)
				if (result.isConfirmed) {
					deleteSchedule(event);  
				} else {
					console.log("Thao tác xóa đã bị hủy.");
				}
			});
		});
	});

});

// Hàm mở modal tạo lịch
function openCreateScheduleModal() {
	document.querySelector('#createModal').style.display = 'block';
	document.querySelector('#modalOverlay').style.display = 'block';
}

// Hàm mở modal sửa lịch trình
let selectedScheduleId = null;
async function openEditScheduleModal() {
	const row = event.target.closest('tr');
	selectedScheduleId = row.querySelector('td:nth-child(1)').innerText;
	const step = row.querySelector('td:nth-child(2)').innerText;
	const activity = row.querySelector('td:nth-child(3)').innerText;
	const location = row.querySelector('td:nth-child(4)').innerText;

	document.getElementById('schedule-id').innerText = selectedScheduleId;
	document.getElementById('step-input-edit').innerText = step;
	document.getElementById('activity-input-edit').innerText = activity;
	document.getElementById('location-input-edit').value = location;

	document.querySelector('#editModal').style.display = 'block';
	document.querySelector('#modalOverlay').style.display = 'block';

}

// Hàm đóng modal
function closeModal() {
	document.querySelector('#createModal').style.display = 'none';
	document.querySelector('#editModal').style.display = 'none';
	document.querySelector('#modalOverlay').style.display = 'none';
}

// Tạo mới Schedule
async function createSchedule() {
	const tourId = document.getElementById("tourId").getAttribute("data-id");
	///const step = document.querySelector('#step-input').value;
	const activity = encodeURIComponent(document.querySelector('#activity-input').value);
	const location = encodeURIComponent(document.querySelector('#location-input').value);

	const url = `http://localhost:8080/create-schedule?tourId=${tourId}&step=${0}&activity=${activity}&location=${location}`;

	try {
		const response = await fetch(url, {
			method: 'POST'
		});

		if (response.status === 409) {
			alert("Step đã tồn tại cho Tour này.");
		} else if (response.ok) {
			alert("Lịch trình đã được tạo thành công.");
			document.querySelector('#createModal').style.display = 'none';
			document.querySelector('#modalOverlay').style.display = 'none';
			window.location.href = `/api-edit-schedule/${tourId}`; // tải lại trang

		} else {
			console.error("Có lỗi xảy ra khi tạo lịch trình:", response);
		}

	} catch (error) {
		console.error("Lỗi trong createSchedule:", error);
	}
}


// Cập nhật Schedule
async function updateSchedule() {
	const tourId = document.getElementById("tourId").getAttribute("data-id");
	/*const scheduleId = document.getElementById("scheduleId").getAttribute("data-id");*/
	const scheduleId = selectedScheduleId;
	console.log(scheduleId, tourId);
	const activity = encodeURIComponent(document.querySelector('#activity-input-edit').value);
	const location = encodeURIComponent(document.querySelector('#location-input-edit').value);

	const url = `http://localhost:8080/update-schedule/${scheduleId}?tourId=${tourId}&activity=${activity}&location=${location}`;

	try {
		const response = await fetch(url, {
			method: 'PUT'
		});

		if (response.ok) {
			alert("Lịch trình đã được cập nhật.");
			document.querySelector('#editModal').style.display = 'none';
			window.location.href = `/api-edit-schedule/${tourId}`; // tải lại trang
		} else {
			console.error("Có lỗi xảy ra khi cập nhật lịch trình:", response);
		}
	} catch (error) {
		console.error("Lỗi trong updateSchedule:", error);
	}
}


// Xóa Schedule
async function deleteSchedule(event) {
	console.log("hàm deleteSchedule được gọi");
	const scheduleId = event.target.closest('tr').querySelector('[data-id]').getAttribute("data-id");
	const tourId = document.getElementById("tourId").getAttribute("data-id");
	const url = `http://localhost:8080/delete-schedule/${scheduleId}?tourId=${tourId}`;

	try {
		const response = await fetch(url, {
			method: 'DELETE'
		});

		if (response.ok) {
			alert("Lịch trình đã được xóa.");
			window.location.href = `/api-edit-schedule/${tourId}`;
		} else {
			console.error("Có lỗi xảy ra khi xóa lịch trình:", response);
		}
	} catch (error) {
		console.error("Lỗi trong deleteSchedule:", error);
	}
}
