// Gọi hàm khi trang được tải xong
document.addEventListener("DOMContentLoaded", function(){
	formatPriceColumn();
	
});

let currentPage=0;
async function loadTourById(tourId) {
    const url = `http://localhost:8080/api-get-tour-by-id?tourId=` + tourId;

    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            console.error("Có lỗi xảy ra khi lấy dữ liệu tour:", response.status);
            return;
        }

        const data = await response.json(); // Parse JSON từ phản hồi
        console.log("Dữ liệu tour:", data);

        // Hiển thị dữ liệu lên form
		document.getElementById("tourName-update").value = data.tourName || "";
		document.getElementById("startDate-update").value = data.startDate || "";
		document.getElementById("endDate-update").value = data.endDate || "";
		document.getElementById("detail-update").value = data.detail || "";
		document.getElementById("transport-update").value = data.transport || "";
		document.getElementById("peopleMax-update").value = data.peopleMax || "";
		document.getElementById("price-update").value = data.price || "";
		document.getElementById("quantity-update").value = data.quantity;
		
		const departureSelect = document.getElementById("departureSelect-update");
		const departureValue = data.departure || "";
		Array.from(departureSelect.options).forEach(option => {
		    if (option.text === departureValue) {
		        option.selected = true;
		    }
		});


		// Xử lý hiển thị giá trị "Điểm đến"
		const destinationSelect = document.getElementById("destinationSelect-update");
		const destinationValue = data.destination || "";
		Array.from(destinationSelect.options).forEach(option => {
		    if (option.text === destinationValue) {
		        option.selected = true;
		    }
		});

    } catch (error) {
        console.error("Lỗi trong loadTourById:", error);
    }
}

function editTour(element) {
	
    const tourId = element.getAttribute("data-tourid");
	loadTourById(tourId);
	tourID = tourId;
    // Thực hiện các thao tác chỉnh sửa với tourId
    console.log("Editing tour with ID:", tourId);
	document.getElementById("overlay").style.display = "block";
	document.getElementById("form-popup-update-tour").style.display = "block";
	document.getElementById("btn-close").addEventListener("click", closeForm);
	document.getElementById("overlay").addEventListener("click", closeForm);
			// Ẩn form và vùng tối
	function closeForm() {
		document.getElementById("overlay").style.display = "none";
		document.getElementById("form-popup-update-tour").style.display = "none";
	}

	existingImages = [];
	selectedImagesUpdate = [];
	loadTourImages(tourId);
	selectedBackgroundCreate=null;
	const imagePreviewContainer = document.getElementById(`createImagePreviewContainer`);
	imagePreviewContainer.innerHTML = "";
	
	loadSchedule(tourId);
	loadReview(tourId);
	
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

async function loadSchedule(tourId) {
		fetch(`http://localhost:8080/api-get-schedule?tourId=` + tourId)
	                .then(response => response.json())
	                .then(data => {
						var listSchedule = document.getElementById("tour-table");
						listSchedule.innerHTML = "";
						data.forEach(schedule => {
							const row = document.createElement("tr");
							
						    	row.innerHTML = `
								<td class="td-table tour-name">${schedule.step}</td>
							       <td class="td-table">${schedule.activity}</td>
							       <td class="td-table">${schedule.location}</td>
								   <td><button type="button" id="edit-btn-schedule" class="edit-btn" 
								   onclick="OpenEditModel(${schedule.scheduleId}, '${schedule.step}', '${encodeURIComponent(schedule.activity)}', '${schedule.location}')">Sửa</button></td>
								   <td><button type="button" class="delete-btn-edit" onclick="deleteSchedule(${schedule.scheduleId})">Xóa</button></td>
						    `;
						   listSchedule.appendChild(row);
						});
	   	});
    	

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

	
	let selectedImagesCreate = []; // Ảnh trong form tạo
	let selectedImagesUpdate = []; // Ảnh trong form cập nhật
	let selectedBackgroundCreate = null; // Background trong form tạo
	let selectedBackgroundUpdate = null; // Background trong form cập nhật
	let existingImages = [];
	let luuBackgroundID = null;
	let deletedImageIds = [];
	function previewImages(context) {
	    // Xác định mảng và DOM dựa trên context
	    const imageInput = document.getElementById(`${context}ImageInput`);
	    const imagePreviewContainer = document.getElementById(`${context}ImagePreviewContainer`);

	    const selectedImages = context === "create" ? selectedImagesCreate : selectedImagesUpdate;
	    const selectedBackground = context === "create" ? selectedBackgroundCreate : selectedBackgroundUpdate;

	    // Duyệt qua các file được chọn và thêm vào mảng selectedImages
	    Array.from(imageInput.files).forEach((file) => {
	        selectedImages.push(file);
	    });
		
	    // Xóa nội dung cũ và hiển thị lại toàn bộ ảnh trong selectedImages
	   	imagePreviewContainer.innerHTML = "";
		existingImages = context === "create" ? [] : existingImages;
		console.log(existingImages);
		if(existingImages.length>0){
			console.log(selectedBackgroundUpdate);
			existingImages.forEach((image,index) => {
						        const imageWrapper = document.createElement("div");
						        imageWrapper.classList.add("image-preview");

						        const img = document.createElement("img");
						        img.src = `/image/${image.imageId}`;
								img.alt = `Image ${image.imageId}`;
								 // Hiển thị ảnh từ server

						        // Nút xóa
						        const removeButton = document.createElement("button");
						        removeButton.classList.add("remove-image");
						        removeButton.type = "button";
						        removeButton.innerHTML = "×";
						        removeButton.onclick = function () {
									deletedImageIds.push(image.imageId); // Lưu ID ảnh bị xóa
															if(existingImages.length>0){
																console.log("trcccccc"+selectedBackgroundUpdate);
																if (selectedBackgroundUpdate === index) {
																        selectedBackgroundUpdate = null;
																    } else if (selectedBackgroundUpdate > index) {
																        selectedBackgroundUpdate--; // Điều chỉnh lại index background nếu ảnh phía trước bị xóa
																    }
																	console.log("sauuuu"+selectedBackgroundUpdate);
																existingImages.splice(index, 1);
																previewImages("update");
															}
								    };
						        // Nút chọn background
						        const backgroundToggle = document.createElement("button");
						        backgroundToggle.classList.add("background-toggle");
						        backgroundToggle.type = "button";
						        backgroundToggle.innerHTML = "✔";
								if(selectedBackgroundUpdate==index){
									imageWrapper.classList.add("selected");
								}
						        
								
						        backgroundToggle.onclick = function () {							
									selectBackground(index, context);
									luuBackgroundID=image.imageId;
									
								}
						        imageWrapper.appendChild(img);
						        imageWrapper.appendChild(removeButton);
						        imageWrapper.appendChild(backgroundToggle);
						        imagePreviewContainer.appendChild(imageWrapper);
						    });
		}
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
					console.log("trc"+selectedBackgroundCreate);
	                removeImage(index, context);
					console.log("lalalala"+context);
					console.log("sau"+selectedBackgroundCreate);
	            };

	            // Nút chọn background
				
	            const backgroundToggle = document.createElement("button");
	            backgroundToggle.classList.add("background-toggle");
	            backgroundToggle.type = "button";
	            backgroundToggle.innerHTML = "✔";
	            backgroundToggle.onclick = function() {
	                selectBackground(index + existingImages.length, context);
					luuBackgroundID = null;
	            };
				console.log("newwww"+selectedBackgroundCreate);
	            // Thêm class nếu ảnh này là background
	            if (selectedBackgroundCreate == index) {
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

	    // Cập nhật giá trị của selectedBackground cho context
	    if (context === "create") {
	        selectedBackgroundCreate = selectedBackground;
	    } else {
	        selectedBackgroundUpdate = selectedBackground;
	    }
	}

	
	function removeImage(index, context) {
	    const selectedImages = context === "create" ? selectedImagesCreate : selectedImagesUpdate;
	    let selectedBackground = context === "create" ? selectedBackgroundCreate : selectedBackgroundUpdate;
		console.log(selectedImages);
		console.log(selectedBackground);
		console.log(index);
	    selectedImages.splice(index, 1); // Xóa ảnh khỏi mảng selectedImages
		
	    // Điều chỉnh selectedBackground nếu ảnh background bị xóa
	    if (selectedBackground === index) {
	        selectedBackground = null;
	    } else if (selectedBackground > index) {
	        selectedBackground--; // Điều chỉnh lại index background nếu ảnh phía trước bị xóa
	    }

	    // Cập nhật lại selectedBackground theo context
	    if (context === "create") {
	        selectedBackgroundCreate = selectedBackground;
	    } else {
	        selectedBackgroundUpdate = selectedBackground;
	    }

	    previewImages(context); // Hiển thị lại ảnh
	}


	function selectBackground(index, context) {
		const imagePreviews = document.querySelectorAll(".image-preview");
		console.log(imagePreviews);
		console.log(index);
		console.log(selectedBackgroundUpdate);
		
		
	    if (context === "create") {
			console.log("aaaa");
			// Bỏ chọn ảnh background hiện tại nếu có
			if (selectedBackgroundCreate !== null) {
				        imagePreviews[selectedBackgroundCreate].classList.remove("selected");
			}
	        selectedBackgroundCreate = index;
			imagePreviews[selectedBackgroundCreate].classList.add("selected");
	    } else {
			console.log("kkk");
			if (selectedBackgroundUpdate !== null) {
				        imagePreviews[selectedBackgroundUpdate].classList.remove("selected");
				    }
	        selectedBackgroundUpdate = index;
			imagePreviews[selectedBackgroundUpdate].classList.add("selected");
	    }	 
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
		if (!selectedBackgroundCreate && selectedImagesCreate.length === 0) {
		        throw new Error("Không có ảnh nào để upload!");
		    }
		const backgroundImage = selectedBackgroundCreate !== null ? selectedImagesCreate[selectedBackgroundCreate] : null;
		const otherImages = selectedImagesCreate.filter((_, index) => index !== selectedBackgroundCreate);

		console.log(backgroundImage);
		console.log(otherImages);
		const compressedBackground = await compressImage(backgroundImage);
		const compressedOthers = await Promise.all(otherImages.map(file => compressImage(file)));
		const formData = new FormData();
		formData.append("backgroundImage", compressedBackground);
		compressedOthers.forEach(file => formData.append("otherImages", file));

	    // Gửi yêu cầu đến server
		 const response = await fetch(`/image/upload/${tourId}`, {
		            method: "POST",
		            body: formData,
		        });
				if (!response.ok) {
				        throw new Error("Không thể upload ảnh. Vui lòng kiểm tra và thử lại.");
				    }
	}
	var tourID = null;
	async function submitForm() {
		try {
	    // Lấy giá trị từ các trường input trong form
		if(selectedImagesCreate.length === 0) {
			alert("Vui lòng chọn ảnh");
			return;
		}
		if (selectedBackgroundCreate === null) {
				    alert("Vui lòng chọn ảnh nền trước khi tạo tour.");
				    return;
				}
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
		const quantity = document.getElementById('quantity').value;
		if(!validateInput(quantity)){
			alert("Số lượng phải lớn hơn 0.");
			return;
		}else if(!validateInput(price)){
			alert("Giá phải lớn hơn 0.");
			return;
		}else if(!validateInput(peopleMax)){
			alert("Số người phải lớn hơn 0.");
			return;
		}
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
		formData.append('quantity', quantity);
	    // Gửi dữ liệu qua API
	    const tourResponse = await fetch('/api-create-tours', {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/x-www-form-urlencoded'  // Định dạng dữ liệu là x-www-form-urlencoded
	        },
	        body: formData  // Gửi dữ liệu dưới dạng x-www-form-urlencoded
	    })
		if (!tourResponse.ok) {
		            throw new Error("Không thể tạo tour. Vui lòng kiểm tra thông tin và thử lại.");
		        }

		        const tourId = await tourResponse.json();

		        // Upload ảnh
		        await saveImagesToServer(tourId);

		        // Thêm lịch trình (nếu có)
		        if (schedules.length > 0) {
		            await CreateSchedule(tourId);
		        }

		        alert("Tạo tour thành công!");
		    } catch (error) {
		        console.error("Lỗi:", error.message);
		        alert("Đã xảy ra lỗi: " + error.message);
		    }
	}
	function validateInput(value) {
	    if (value < 0) {
	        return false;
	    }
	    return true;
	}
	async function creatTour(){
		submitForm();
	}
		const okButtonUpdate = document.getElementById("btn-Ok-Insert-Schedule-Update");
		okButtonUpdate.addEventListener("click", () => {
			createSchedule(tourID);
		});
	    const createButton = document.querySelector(".btn-insert-schedule");
		const editBtnSchedule = document.getElementById("edit-btn-schedule");
		const createButtonSchedule = document.querySelector(".btn-insert-schedule-in-update-tour");
	    const createmodal = document.getElementById("createModal");
		const createmodalSchedule = document.getElementById("createModalSchedule");
		const editmodal = document.getElementById("editModal");
		const editmodalSchedule = document.getElementById("editModalSchedule");
	    const overlay = document.getElementById("overlay1");
		const overlay2 = document.getElementById("overlay2");
	    const cancelButtons = document.querySelectorAll(".btn-cancel");
		const btnOkUpdateSchedule = document.getElementById("btn-Ok-Update-Schedule-Edit");
		const btnCancleUpdateSchedule = document.querySelectorAll(".btn-cancel-edit");
		function UpdateScheduleBtn(){
			const scheduleId = btnOkUpdateSchedule.dataset.scheduleId;
			console.log(scheduleId);
			updateSchedule(scheduleId);
		}
		
		function OpenEditModel(scheduleId,step,activityURI, location){
			const activity = decodeURIComponent(activityURI);
			editmodalSchedule.style.display = "block";
			overlay2.style.display = "block";
			document.getElementById("step-input-edit-schedule").textContent = step; // Gán giá trị cho <span>
			document.getElementById("activity-input-edit-schedule").value = activity; // Gán giá trị cho <textarea>
			document.getElementById("location-input-edit-schedule").value = location;
			btnOkUpdateSchedule.dataset.scheduleId = scheduleId; // Gán giá trị cho <input>
		}
		console.log(editBtnSchedule)
	    // Hiển thị modal và overlay
	    createButton.addEventListener("click", () => {
	        createmodal.style.display = "block";
	        overlay.style.display = "block";
	    });
		createButtonSchedule.addEventListener("click", () => {
		    createmodalSchedule.style.display = "block";
		    overlay2.style.display = "block";
		});

	    // Ẩn modal và overlay
	    const closeModal = () => {
	        createmodal.style.display = "none";
			editmodalSchedule.style.display = "none";
			createmodalSchedule.style.display = "none";
	        overlay.style.display = "none";
			overlay2.style.display = "none";
	    };

	    // Xử lý nút Cancel
	    cancelButtons.forEach(button => {
	        button.addEventListener("click", closeModal);
	    });
		btnCancleUpdateSchedule.forEach(button => {
			        button.addEventListener("click", closeModal);
			    });
	    // Ẩn modal khi click vào overlay
	    overlay.addEventListener("click", closeModal);
		overlay2.addEventListener("click", closeModal);
		
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
					<td style="display: flex"><button type="button" class="edit-btn">Sửa</button></td>
					<td ><button class="delete-btn">Xóa</button></td>
				
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
			async function CreateSchedule(TourId) {
				const scheduleTableBody = document.querySelector("#schedule-table tbody");
				
			    // Tạo object chứa cả TourId và schedules
				if(scheduleTableBody==null){
					return;
				}
			    const dataToSend = {
			        tourId: TourId,
			        schedules: schedules // Mảng các lịch trình
			    };



			    const response = await fetch("/api-schedule-tour-management", {
			        method: "POST",
			        headers: {
			            "Content-Type": "application/json",
			        },
			        body: JSON.stringify(dataToSend), // Gửi dữ liệu bao gồm TourId và schedules
			    });
				if (!response.ok) {
				        throw new Error("Không thể thêm lịch trình.");
				    }
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
			
			async function loadTourImages(tourId) {
			    const response = await fetch(`/api-get-image-by-id?tourId=${tourId}`);
			    if (!response.ok) {
			        console.error("Failed to load images");
			        return;
			    }

			    const images = await response.json();
				
				existingImages=images;
			    const imagePreviewContainer = document.getElementById("updateImagePreviewContainer");
			    imagePreviewContainer.innerHTML = ""; // Xóa nội dung cũ

			    images.forEach((image,index) => {
			        const imageWrapper = document.createElement("div");
			        imageWrapper.classList.add("image-preview");

			        const img = document.createElement("img");
			        img.src = `/image/${image.imageId}`;
					img.alt = `Image ${image.imageId}`;
					 // Hiển thị ảnh từ server

			        // Nút xóa
			        const removeButton = document.createElement("button");
			        removeButton.classList.add("remove-image");
			        removeButton.type = "button";
			        removeButton.innerHTML = "×";
			        removeButton.onclick = function () {
					 	deletedImageIds.push(image.imageId); // Lưu ID ảnh bị xóa
						if(existingImages.length>0){
							console.log("trcccccc"+selectedBackgroundUpdate);
							if (selectedBackgroundUpdate === index) {
							        selectedBackgroundUpdate = null;
							    } else if (selectedBackgroundUpdate > index) {
							        selectedBackgroundUpdate--; // Điều chỉnh lại index background nếu ảnh phía trước bị xóa
							    }
								console.log("sauuuu"+selectedBackgroundUpdate);
							existingImages.splice(index, 1);
							previewImages("update");
						}
					};
			        // Nút chọn background
			        const backgroundToggle = document.createElement("button");
			        backgroundToggle.classList.add("background-toggle");
			        backgroundToggle.type = "button";
			        backgroundToggle.innerHTML = "✔";

			        if (image.background) {
			            imageWrapper.classList.add("selected");
						selectedBackgroundUpdate = index;
						luuBackgroundID= image.imageId;// Đánh dấu ảnh đang làm background
			        }
			        backgroundToggle.onclick= function () {	
						console.log(index)	;					
						selectBackground(index, "update");
						luuBackgroundID=image.imageId;
						
					}
			        imageWrapper.appendChild(img);
			        imageWrapper.appendChild(removeButton);
			        imageWrapper.appendChild(backgroundToggle);
			        imagePreviewContainer.appendChild(imageWrapper);
			    });
			}
			async function deleteImage(imageId) {
			    const response = await fetch(`/api-update-status-image?imageId=${imageId}`, { method: "DELETE" });
			    if (!response.ok) {
			        alert("Failed to delete image");
			        return;
			    }

			    alert("Image deleted successfully");
			    loadTourImages(currentTourId); // Cập nhật lại danh sách ảnh
			}
			async function updateTour(){
				if (selectedBackgroundUpdate == null) {
						alert("Vui lòng chọn ảnh nền trước khi lưu.");
						return;
					}
					const formData = new FormData();
					const backgroundImage = selectedBackgroundUpdate !== null ? selectedImagesUpdate[selectedBackgroundUpdate-existingImages.length] : null;
					var otherImages = selectedImagesUpdate;
					var compressedBackground;
					var compressedOthers =[];
					if(	otherImages.length > 0 ){
						compressedOthers = await Promise.all(otherImages.map(file => compressImage(file)));
					}
					if(selectedBackgroundUpdate>=existingImages.length){
						otherImages = selectedImagesUpdate.filter((_, index) => index !== selectedBackgroundUpdate-existingImages.length);
						compressedOthers = await Promise.all(otherImages.map(file => compressImage(file)));
						compressedBackground = await compressImage(backgroundImage);
					}
					/*else{
						compressedBackground=luuBackgroundID;					
					}*/
					console.log(selectedImagesUpdate);
					console.log(deletedImageIds);
					console.log(backgroundImage);
					
					console.log(otherImages);
					console.log(compressedOthers);
					console.log(compressedBackground);
					console.log(tourID);
					console.log(selectedBackgroundUpdate);
					
					
					if (compressedBackground!=null) {
					    formData.append("backgroundImage", compressedBackground); // File ảnh
					} else {
					    formData.append("backgroundImageId", luuBackgroundID);
						console.log("ádasd"); // ID ảnh
					}
				
					
					deletedImageIds.forEach(id => formData.append("deletedImageIds", id));;
							
					compressedOthers.forEach(file => formData.append("otherImages", file));

						    // Gửi yêu cầu đến server
					const response = await fetch(`/image/update/${tourID}`, {
						method: "POST",
						body: formData,
					});
					if (!response.ok) {
						throw new Error("Không thể upload ảnh. Vui lòng kiểm tra và thử lại.");
					}else{
						updateInformationTour();
					}
					
				}

				async function updateInformationTour() {
						try {
					    const tourName = document.getElementById('tourName-update').value;
					    	
						var departureSelect = document.getElementById('departureSelect-update');
						var departure = departureSelect.options[departureSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
						var destinationSelect = document.getElementById('destinationSelect-update');
						var destination = destinationSelect.options[destinationSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
												
					    const startDate = document.getElementById('startDate-update').value;
					    const endDate = document.getElementById('endDate-update').value;
					    const detail = document.getElementById('detail-update').value;
					    const peopleMax = document.getElementById('peopleMax-update').value;
					    const price = document.getElementById('price-update').value;
						const transport = document.getElementById('transport-update').value;
						const quantity = document.getElementById('quantity-update').value;

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
						formData.append('quantity', quantity);
					    // Gửi dữ liệu qua API
					    const tourResponse = await fetch(`/api-update-tours/${tourID}`, {
					        method: 'POST',
					        headers: {
					            'Content-Type': 'application/x-www-form-urlencoded'  // Định dạng dữ liệu là x-www-form-urlencoded
					        },
					        body: formData  // Gửi dữ liệu dưới dạng x-www-form-urlencoded
					    })
						if (!tourResponse.ok) {
						            throw new Error("Không thể update tour. Vui lòng kiểm tra thông tin và thử lại.");
						        }

						        const tourId = await tourResponse.json();

						        // Upload ảnh
						       /* await saveImagesToServer(tourId)*/;

						        // Thêm lịch trình (nếu có)
						        /*if (schedules.length > 0) {
						            await CreateSchedule(tourId);
						        }*/
						        alert("Update tour thành công!");
								loadPage(currentPage);
						    } catch (error) {
						        console.error("Lỗi:", error.message);
						        alert("Đã xảy ra lỗi: " + error.message);
						    }
					}
					// Tạo mới Schedule
					async function createSchedule(tourId) {
						///const step = document.querySelector('#step-input').value;
						const activity = document.getElementById("activity-input-schedule").value.trim();
						const location = document.getElementById("location-input-schedule").value.trim();
						console.log("activity"+activity)
						console.log("location"+location)
						const url = `http://localhost:8080/create-schedule?tourId=${tourId}&step=${0}&activity=${activity}&location=${location}`;

						try {
							const response = await fetch(url, {
								method: 'POST'
							});

							if (response.status === 409) {
								alert("Step đã tồn tại cho Tour này.");
							} else if (response.ok) {
								alert("Lịch trình đã được tạo thành công.");
								loadSchedule(tourId);
							} else {
								console.error("Có lỗi xảy ra khi tạo lịch trình:", response);
							}

						} catch (error) {
							console.error("Lỗi trong createSchedule:", error);
						}
					}


					// Cập nhật Schedule
					async function updateSchedule(scheduleId) {
						
						/*const scheduleId = document.getElementById("scheduleId").getAttribute("data-id");*/					
						
						const activity = encodeURIComponent(document.getElementById("activity-input-edit-schedule").value);
						const location = encodeURIComponent(document.getElementById("location-input-edit-schedule").value);

						const url = `http://localhost:8080/update-schedule/${scheduleId}?tourId=${tourID}&activity=${activity}&location=${location}`;

						try {
							const response = await fetch(url, {
								method: 'PUT'
							});

							if (response.ok) {
								alert("Lịch trình đã được cập nhật.");
								loadSchedule(tourID);
							
							} else {
								console.error("Có lỗi xảy ra khi cập nhật lịch trình:", response);
							}
						} catch (error) {
							console.error("Lỗi trong updateSchedule:", error);
						}
					}


					// Xóa Schedule
					async function deleteSchedule(scheduleId) {
						console.log(scheduleId)
						const url = `http://localhost:8080/delete-schedule/${scheduleId}?tourId=${tourID}`;

						try {
							const response = await fetch(url, {
								method: 'DELETE'
							});

							if (response.ok) {
								alert("Lịch trình đã được xóa.");
								loadSchedule(tourID);
							} else {
								console.error("Có lỗi xảy ra khi xóa lịch trình:", response);
							}
						} catch (error) {
							console.error("Lỗi trong deleteSchedule:", error);
						}
					}
					async function loadReview(tourId) {
					    const url = `http://localhost:8080/api-get-review?tourId=${tourId}`;
					    try {
					        const response = await fetch(url);
					        console.log("Phản hồi từ server:", response);

					        if (!response.ok) {
					            console.error("Có lỗi xảy ra:", response);
					            return;
					        }

					        const data = await response.json();
					        console.log("Dữ liệu nhận được:", data);

					        const listReview = document.querySelector(".list-review");
					        listReview.innerHTML = ""; // Xóa nội dung cũ

					        // Lặp qua từng review và thêm vào HTML
					        data.forEach(review => {
					            listReview.innerHTML += `
					                <div class="review">
					                    <div class="review-header">
					                        <div class="name">
					                            <span>${review.fullName}</span>
					                        </div>
					                        <div class="date">
					                            <i class="far fa-clock"></i>
					                            ${formatDate(review.reviewDate)}
					                        </div>
					                    </div>
					                    <div class="stars">
					                        ${renderStars(review.rate)}
					                    </div>
					                    <div class="review-body-container">
					                        <div class="review-body">
					                            <span>${review.comment}</span>
					                        </div>
					                        <button type="button" class="btn-cancel-review" onclick="deleteReview(${review.reviewsId})">Xóa</button>
					                    </div>
					                </div>
					            `;
					        });
					    } catch (error) {
					        console.error("Lỗi trong loadReview:", error);
					    }
					}

					// Hàm render sao theo số lượng
					function renderStars(rate) {
					    const fullStars = Math.floor(rate);
					    const halfStar = rate % 1 >= 0.5 ? 1 : 0;

					    return (
					        '<i class="fas fa-star"></i>'.repeat(fullStars) +
					        (halfStar ? '<i class="fas fa-star-half-alt"></i>' : '') +
					        '<i class="far fa-star"></i>'.repeat(5 - fullStars - halfStar)
					    );
					}
					function formatDate(isoDateString) {
					    const date = new Date(isoDateString);
					    const formatter = new Intl.DateTimeFormat('vi-VN', {
					        day: '2-digit',
					        month: '2-digit',
					        year: 'numeric',
					    });
					    return formatter.format(date);
					}
					async function deleteReview(reviewId) {
					    const url = `http://localhost:8080/api-delete-review?reviewId=${reviewId}`;
					    try {
					        const response = await fetch(url, {
					            method: 'GET', // Hoặc 'POST' nếu API được chỉnh sửa để nhận bằng POST
					        });

					        if (response.ok) {
					            const message = await response.text();
					            console.log(message); // Hiển thị phản hồi từ server
					            alert("Review deleted successfully!");
					            // Cập nhật giao diện nếu cần
					           loadReview(tourID);
					        } else {
					            console.error("Error deleting review:", response.status);
					            alert("Failed to delete the review. Please try again.");
					        }
					    } catch (error) {
					        console.error("Error:", error);
					        alert("An error occurred. Please check the console for details.");
					    }
					}
					function OpenCloseSchedule() {
					    const scheduleDiv = document.querySelector('.schdule');  // Chọn div có class 'schdule'
					    const icon = document.getElementById('schedule-toggle-icon');  // Chọn icon trong tiêu đề

					    // Kiểm tra và thay đổi lớp 'show' để kích hoạt hiệu ứng
					    if (scheduleDiv.classList.contains('show')) {
					        scheduleDiv.classList.remove('show');  // Ẩn div với hiệu ứng
					        icon.classList.remove('fa-chevron-up');
					        icon.classList.add('fa-chevron-down');  // Đổi mũi tên lên thành mũi tên xuống
					    } else {
					        scheduleDiv.classList.add('show');  // Hiển thị div với hiệu ứng
					        icon.classList.remove('fa-chevron-down');
					        icon.classList.add('fa-chevron-up');  // Đổi mũi tên xuống thành mũi tên lên
					    }
					}

					function OpenCloseReview() {
					    const reviewDiv = document.querySelector('.list-review');  // Chọn div có class 'list-review'
					    const icon = document.getElementById('review-toggle-icon');  // Chọn icon trong tiêu đề

					    // Kiểm tra và thay đổi lớp 'show' để kích hoạt hiệu ứng
					    if (reviewDiv.classList.contains('show')) {
					        reviewDiv.classList.remove('show');  // Ẩn div với hiệu ứng
					        icon.classList.remove('fa-chevron-up');
					        icon.classList.add('fa-chevron-down');  // Đổi mũi tên lên thành mũi tên xuống
					    } else {
					        reviewDiv.classList.add('show');  // Hiển thị div với hiệu ứng
					        icon.classList.remove('fa-chevron-down');
					        icon.classList.add('fa-chevron-up');  // Đổi mũi tên xuống thành mũi tên lên
					    }
					}


