// Gọi hàm khi trang được tải xong
document.addEventListener("DOMContentLoaded", formatPriceColumn);
let currentPage=0;
function editTour(element) {
    const tourId = element.getAttribute("data-tourid");
    // Thực hiện các thao tác chỉnh sửa với tourId
    console.log("Editing tour with ID:", tourId);
}

function deleteTour(element) {
	const tourId = element.getAttribute("data-tourid");
	    console.log("Deleting tour with ID:", tourId);

	    const url = '/admin/update-status?tourId=' + tourId;
	    fetch(url)
	        .then(response => {
	            if (!response.ok) {
	                throw new Error("Network response was not ok");
	            }
	            return response.text(); // Kỳ vọng một chuỗi văn bản (text) làm phản hồi
	        })
	        .then(data => {
	            alert(data); // Thông báo thành công hoặc thất bại
				loadPage(currentPage); // Load lại trang sau khi xóa thành công
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert("Lỗi khi cập nhật status");
	        });
}
// Hàm định dạng số thành kiểu "1.000.000"
function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

// Lặp qua từng ô trong cột giá và định dạng lại
function formatPriceColumn() {
    // Lấy tất cả các ô trong cột giá (cột thứ 9)
    const priceCells = document.querySelectorAll("#user-table tr td:nth-child(9)");
    
    priceCells.forEach(cell => {
        // Chuyển giá trị thành số và định dạng lại
        let price = parseFloat(cell.innerText);
        cell.innerText = formatPrice(price);
    });
}
document.getElementById("add-tours-btn").addEventListener("click", function() {
    document.getElementById("overlay").style.display = "block";
    document.getElementById("form-popup").style.display = "block";
});
// Đóng form khi nhấn vào vùng tối
document.getElementById("overlay").addEventListener("click", closeForm);
// Ẩn form và vùng tối
function closeForm() {
    document.getElementById("overlay").style.display = "none";
    document.getElementById("form-popup").style.display = "none";
}


function abbreviate(text, maxLength) {
    return text.length > maxLength ? text.substring(0, maxLength) + "..." : text;
}
async function loadPage(page) {
    	const size = 8;
    	const url = `http://localhost:8080/api-get-tour-management?page=${page}&size=${size}`; // Endpoint mới
		console.log(url)
		currentPage=page;
    	const request = new Request(url, {
    		method: "GET",
    		headers: {
    			"Content-Type": "application/json",
    		},
    	});

    	try {
    		const response = await fetch(request);
    		console.log("Phản hồi từ server:", response);

    		if (!response.ok) {
    			console.log("Có lỗi xảy ra trong fetchTours:", response);
    			return null;
    		} else {
    			const data = await response.json();
				const listTour = document.getElementById("user-table");
				           listTour.innerHTML = ""; // Clear current table data
				           data.content.forEach(tour => {
								const row = document.createElement("tr");
				               	row.innerHTML = `
				                   
				                       <td>${tour.tourId}</td>
				                       
									   <td class="col-tour-name" title="${tour.tourName}">${abbreviate(tour.tourName, 36)}</td>
									   <td class="col-departure" title="${tour.departure}">${abbreviate(tour.departure, 36)}</td>
									   <td class="col-destination" title="${tour.destination}">${abbreviate(tour.destination, 25)}</td>
									   <td>${tour.startDate}</td>
				                       <td>${tour.endDate}</td>
				                       <td>${tour.transport}</td>
				                       <td>${tour.peopleMax}</td>
				                       <td>${tour.price}</td>
				                       <td>
				                           <a href="javascript:void(0);" data-tourid="${tour.tourId}" onclick="editTour(this)" class="icon-action edit-icon">
				                               <i class="fas fa-pencil-alt"></i>
				                           </a>
				                           <a href="javascript:void(0);" data-tourid="${tour.tourId}" onclick="deleteTour(this)" class="icon-action delete-icon">
				                               <i class="fas fa-trash-alt"></i>
				                           </a>
				                       </td>
				                   
				               `;
							   listTour.appendChild(row);
				           });
    			
    			updatePerPage(data.totalPages, page);
    			console.log(data.totalPages+"aaaa"+ page)
    		}
    	} catch (error) {
    		console.error("Lỗi trong fetchTours:", error);
    	}
    }
	function updatePerPage(totalPages, currentPage) {
	    const pagination = document.getElementById("pagination");
	    pagination.innerHTML = ""; // Xóa nội dung phân trang cũ

	    // Nút trước đó
	    if (currentPage > 0) {
	        const prevButton = document.createElement("li");
	        prevButton.innerHTML = `
	            <button onclick="loadPage(${currentPage - 1})">
	                <i class="fas fa-chevron-left icon"></i>
	            </button>
	        `;
	        pagination.appendChild(prevButton);
	    }

	    // Tạo các nút trang
	    for (let i = 0; i < totalPages; i++) {
	        const pageButton = document.createElement("li");
	        pageButton.innerHTML = `
	            <a onclick="loadPage(${i})" class="${i === currentPage ? 'current' : ''}">
	                ${i + 1}
	            </a>
	        `;
	        pagination.appendChild(pageButton);
	    }

	    // Nút tiếp theo
	    if (currentPage + 1 < totalPages) {
	        const nextButton = document.createElement("li");
	        nextButton.innerHTML = `
	            <button onclick="loadPage(${currentPage + 1})">
	                <i class="fas fa-chevron-right icon"></i>
	            </button>
	        `;
	        pagination.appendChild(nextButton);
	    }
	}

	
	let selectedBackground = null; // Lưu trữ ảnh background
	let selectedImages = []; // Mảng lưu trữ các ảnh đã chọn

	function previewImages() {
	    const imageInput = document.getElementById("imageInput");
	    const imagePreviewContainer = document.getElementById("imagePreviewContainer");

	    // Duyệt qua các file được chọn và thêm vào mảng selectedImages
	    Array.from(imageInput.files).forEach((file) => {
	        selectedImages.push(file);
	    });

	    // Xóa nội dung cũ và hiển thị lại toàn bộ ảnh trong selectedImages
	    imagePreviewContainer.innerHTML = "";
	    selectedImages.forEach((file, index) => {
	        const reader = new FileReader();

	        reader.onload = function(event) {
	            const imageWrapper = document.createElement("div");
	            imageWrapper.classList.add("image-preview");

	            const img = document.createElement("img");
	            img.src = event.target.result;

	            const removeButton = document.createElement("button");
	            removeButton.classList.add("remove-image");
	            removeButton.type = "button";
	            removeButton.innerHTML = "×";
	            removeButton.onclick = function() {
	                removeImage(index);
	            };

	            // Nút chọn background
	            const backgroundToggle = document.createElement("button");
	            backgroundToggle.classList.add("background-toggle");
	            backgroundToggle.type = "button";
	            backgroundToggle.innerHTML = "✔";
	            backgroundToggle.onclick = function() {
	                selectBackground(index);
	            };

	            // Thêm class nếu ảnh này là background
	            if (selectedBackground === index) {
	                imageWrapper.classList.add("selected");
	            }

	            imageWrapper.appendChild(img);
	            imageWrapper.appendChild(removeButton);
	            imageWrapper.appendChild(backgroundToggle);
	            imagePreviewContainer.appendChild(imageWrapper);
	        };

	        reader.readAsDataURL(file);
	    });

	    // Reset input để người dùng có thể chọn lại file khác nếu muốn
	    imageInput.value = "";
	}

	function removeImage(index) {
	    selectedImages.splice(index, 1); // Xóa ảnh khỏi mảng selectedImages

	    // Điều chỉnh selectedBackground nếu ảnh background bị xóa
	    if (selectedBackground === index) {
	        selectedBackground = null;
	    } else if (selectedBackground > index) {
	        selectedBackground--; // Điều chỉnh lại index background nếu ảnh phía trước bị xóa
	    }

	    previewImages(); // Hiển thị lại ảnh
	}

	function selectBackground(index) {
	    const imagePreviews = document.querySelectorAll(".image-preview");

	    // Bỏ chọn ảnh background hiện tại nếu có
	    if (selectedBackground !== null) {
	        imagePreviews[selectedBackground].classList.remove("selected");
	    }

	    // Chọn ảnh mới làm background
	    selectedBackground = index;
	    imagePreviews[selectedBackground].classList.add("selected");
	}
	//Hàm giảm kích thước ảnh
	async function compressImage(file) {
	    return new Promise((resolve, reject) => {
	        const reader = new FileReader();
	        reader.onload = () => {
	            const img = new Image();
	            img.src = reader.result;
	            img.onload = () => {
	                const canvas = document.createElement("canvas");
	                // Giữ nguyên kích thước gốc
	                canvas.width = img.width;
	                canvas.height = img.height;

	                const ctx = canvas.getContext("2d");
	                ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

	                // Giảm chất lượng để giảm kích thước
	                canvas.toBlob(
	                    (blob) => {
	                        if (blob.size > 40 * 1024 * 1024) {
	                            console.warn("Kích thước ảnh vượt quá 40 MiB sau khi nén!");
	                        }
	                        resolve(new File([blob], file.name, { type: "image/jpeg" }));
	                    },
	                    "image/jpeg",
	                    0.5 // Chất lượng nén (0 - 1)
	                );
	            };
	        };
	        reader.onerror = (error) => reject(error);
	        reader.readAsDataURL(file);
	    });
	}
	// Hàm lưu trữ ảnh
	async function saveImagesToServer(tourId) {
		console.log('Tour created with ID:', tourId);
		const backgroundImage = selectedBackground !== null ? selectedImages[selectedBackground] : null;
			    const otherImages = selectedImages.filter((_, index) => index !== selectedBackground);

			    console.log("Background Image:", backgroundImage);
			    console.log("Other Images:", otherImages);
	    if (!selectedBackground && selectedImages.length === 0) {
	        alert("No images to save!");
	        return;
	    }

		const compressedBackground = await compressImage(backgroundImage);
		const compressedOthers = await Promise.all(otherImages.map(file => compressImage(file)));
		const formData = new FormData();
		formData.append("backgroundImage", compressedBackground);
		compressedOthers.forEach(file => formData.append("otherImages", file));

	    // Gửi yêu cầu đến server
		 await fetch(`/image/upload/${tourId}`, {
		            method: "POST",
		            body: formData,
		        })
	        .then((response) => {
	            if (response.ok) {
	                alert("Tạo tour thành công");
	            } else {
	                alert("Failed to upload images.");
	            }
	        })
	        .catch((error) => {
	            console.error("Error uploading images:", error);
	            alert("Error uploading images.");
	        });
	}
	var tourID = null;
	async function submitForm() {
	    // Lấy giá trị từ các trường input trong form
	    const tourName = document.getElementById('tourName').value;
	    		
		var departureSelect = document.getElementById('departureSelect');
		var departure = departureSelect.options[departureSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
		var destinationSelect = document.getElementById('destinationSelect');
		var destination = destinationSelect.options[destinationSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
								
	    const startDate = document.getElementById('startDate').value;
	    const endDate = document.getElementById('endDate').value;
	    const detail = document.getElementById('detail').value;
	    const peopleMax = document.getElementById('peopleMax').value;
	    const price = document.getElementById('price').value;
		const transport = document.getElementById('transport').value;
		

	    // Chuyển dữ liệu thành x-www-form-urlencoded
	    const formData = new URLSearchParams();
	    formData.append('tourName', tourName);
	    formData.append('departure', departure);
	    formData.append('destination', destination);
	    formData.append('startDate', startDate);
	    formData.append('endDate', endDate);
	    formData.append('detail', detail);
	    formData.append('peopleMax', peopleMax);
	    formData.append('price', price);
		formData.append('transport', transport);
	    // Gửi dữ liệu qua API
	    fetch('/api-create-tours', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/x-www-form-urlencoded'  // Định dạng dữ liệu là x-www-form-urlencoded
	        },
	        body: formData  // Gửi dữ liệu dưới dạng x-www-form-urlencoded
	    })
	    .then(response => response.json())  // Nhận phản hồi từ server
	    .then(tourId => {
			tourID = tourId;
			console.log('Tour created with ID:', tourId);
			CreateSchedule(tourId);
			saveImagesToServer(tourId);
	    })
	    .catch((error) => {
	        console.error('Error:', error);
	    });
	}
	async function creatTour(){
		submitForm();
		console.log('Tour created with ID 111:', tourID);
	}

	    const createButton = document.querySelector(".btn-insert-schedule");
	    const createmodal = document.getElementById("createModal");
		const editmodal = document.getElementById("editModal");
	    const overlay = document.getElementById("overlay1");
	    const cancelButtons = document.querySelectorAll(".btn-cancel");

	    // Hiển thị modal và overlay
	    createButton.addEventListener("click", () => {
	        createmodal.style.display = "block";
	        overlay.style.display = "block";
	    });

	    // Ẩn modal và overlay
	    const closeModal = () => {
	        createmodal.style.display = "none";
	        overlay.style.display = "none";
	    };

	    // Xử lý nút Cancel
	    cancelButtons.forEach(button => {
	        button.addEventListener("click", closeModal);
	    });

	    // Ẩn modal khi click vào overlay
	    overlay.addEventListener("click", closeModal);
		
		//==============================
		const scheduleTableBody = document.querySelector("#schedule-table tbody");
		    const okButton = document.getElementById("btn-Ok-Insert-Schedule");
		    const activityInput = document.getElementById("activity-input");
		    const locationInput = document.getElementById("location-input");
			
			const activityInputEdit = document.getElementById("activity-input-edit");
			const locationInputEdit = document.getElementById("location-input-edit");
					
			const okButtonEdit = document.getElementById("btn-Ok-Update-Schedule");
			const cancelButtonEdit = document.getElementById("btn-Cancel-Update-Schedule");
		    let schedules = []; // Lưu dữ liệu lịch trình
			
		    overlay.addEventListener("click", closeModal);

		    // Thêm dữ liệu vào bảng
		    okButton.addEventListener("click", () => {
		        const activity = activityInput.value.trim();
		        const location = locationInput.value.trim();

		        if (activity === "" || location === "") {
		            alert("Vui lòng điền đầy đủ thông tin.");
		            return;
		        }

		        const step = schedules.length + 1;

		        const schedule = { step, activity, location };
		        schedules.push(schedule); // Lưu vào mảng

		        // Thêm dòng dữ liệu mới vào bảng
		        const row = document.createElement("tr");
		        row.innerHTML = `
		            <td>${step}</td>
		            <td>${activity}</td>
		            <td>${location}</td>
		            <td><button type="button" class="edit-btn">Sửa</button></td>
		            <td><button class="delete-btn">Xóa</button></td>
		        `;
		        scheduleTableBody.appendChild(row);

		        

		        // Xử lý nút sửa
				row.querySelector(".edit-btn").addEventListener("click", () => {
							document.getElementById("step-input-edit").textContent = schedule.step;
				            activityInputEdit.value = schedule.activity;
				            locationInputEdit.value = schedule.location;
				            editmodal.style.display = "block";
				            overlay.style.display = "block";

				            okButtonEdit.onclick = () => {
				                schedule.activity = activityInputEdit.value.trim();
				                schedule.location = locationInputEdit.value.trim();

				                if (schedule.activity === "" || schedule.location === "") {
				                    alert("Vui lòng điền đầy đủ thông tin.");
				                    return;
				                }

				                row.children[1].textContent = schedule.activity;
				                row.children[2].textContent = schedule.location;
								console.log(schedule);
								updateTable();
								editmodal.style.display = "none";
								overlay.style.display = "none";
				            };
							cancelButtonEdit.onclick = () => {
								editmodal.style.display = "none";
								overlay.style.display = "none";
							};
							overlay.onclick = () => {
								editmodal.style.display = "none";
								overlay.style.display = "none";
							};
				        });

		        // Xử lý nút xóa
		        row.querySelector(".delete-btn").addEventListener("click", () => {
		            const index = schedules.indexOf(schedule);
		            schedules.splice(index, 1); // Xóa khỏi mảng
		            row.remove();

		            // Cập nhật lại step trong bảng
		            Array.from(scheduleTableBody.children).forEach((r, idx) => {
		                r.children[0].textContent = idx + 1;
		                schedules[idx].step = idx + 1;
		            });
		        });
		    });
		    // Gửi dữ liệu qua controller
			function CreateSchedule(TourId) {
				const scheduleTableBody = document.querySelector("#schedule-table tbody");
			    // Tạo object chứa cả TourId và schedules
				if(scheduleTableBody==null){
					return;
				}
			    const dataToSend = {
			        tourId: TourId,
			        schedules: schedules // Mảng các lịch trình
			    };

			    console.log("Dữ liệu gửi đi:", JSON.stringify(dataToSend)); // Log lại dữ liệu trước khi gửi

			    fetch("/api-schedule-tour-management", {
			        method: "POST",
			        headers: {
			            "Content-Type": "application/json",
			        },
			        body: JSON.stringify(dataToSend), // Gửi dữ liệu bao gồm TourId và schedules
			    })
			    .then(response => response.json())
			    .then(data => {
			        console.log("Dữ liệu đã gửi thành công:", data);
			    })
			    .catch(error => {
			        console.error("Lỗi khi gửi dữ liệu:", error);
			    });
			}

			function updateTable() {
			    // Xóa toàn bộ nội dung bảng
			    scheduleTableBody.innerHTML = "";

			    // Lặp qua mảng schedules để tạo lại bảng
			    schedules.forEach((schedule, index) => {
			        schedule.step = index + 1; // Cập nhật step
			        const row = document.createElement("tr");
			        row.innerHTML = `
			            <td>${schedule.step}</td>
			            <td>${schedule.activity}</td>
			            <td>${schedule.location}</td>
			            <td><button type="button" class="edit-btn">Sửa</button></td>
			            <td><button class="delete-btn">Xóa</button></td>
			        `;
			        scheduleTableBody.appendChild(row);

			        // Xử lý nút sửa
			        row.querySelector(".edit-btn").addEventListener("click", () => {
						document.getElementById("step-input-edit").textContent = schedule.step;
						activityInputEdit.value = schedule.activity;
						locationInputEdit.value = schedule.location;
			            editmodal.style.display = "block";
			            overlay.style.display = "block";

			            okButtonEdit.onclick = () => {
			                schedule.activity = activityInputEdit.value.trim();
			                schedule.location = locationInputEdit.value.trim();

			                if (schedule.activity === "" || schedule.location === "") {
			                    alert("Vui lòng điền đầy đủ thông tin.");
			                    return;
			                }

			                editmodal.style.display = "none";
			                overlay.style.display = "none";
			                updateTable();
							cancelButtonEdit.onclick = () => {
								editmodal.style.display = "none";
								overlay.style.display = "none";
							};
							overlay.onclick = () => {
								editmodal.style.display = "none";
								overlay.style.display = "none";
							};
			            };
			        });

			        // Xử lý nút xóa
			        row.querySelector(".delete-btn").addEventListener("click", () => {
			            schedules.splice(index, 1); // Xóa khỏi mảng
			            updateTable(); // Cập nhật lại bảng sau khi xóa
			        });
			    });
			}



