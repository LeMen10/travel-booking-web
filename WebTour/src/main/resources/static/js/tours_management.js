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
