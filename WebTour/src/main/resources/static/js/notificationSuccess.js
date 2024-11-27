document.addEventListener('DOMContentLoaded', function() {
	document.getElementById("bt-continuteShop").addEventListener("click", backHomeForm);

	document.querySelector("#print-button").addEventListener("click", function() {
		// Chuyển đổi tất cả các màu sắc không hợp lệ thành các giá trị hợp lệ
		document.querySelectorAll('*').forEach(function(el) {
			const computedStyle = window.getComputedStyle(el);
			const propertiesToCheck = ['backgroundColor', 'color', 'borderColor', 'outlineColor', 'boxShadow'];

			propertiesToCheck.forEach(property => {
				if (computedStyle[property] && computedStyle[property].includes('oklch')) {
					// Chuyển đổi màu không hợp lệ thành màu hợp lệ
					let newColor = "#ffffff";  // Default to white color

					// Chuyển đổi nếu là màu nền
					if (property === 'backgroundColor') {
						el.style.backgroundColor = newColor; // Màu trắng cho nền
					}
					// Nếu là màu chữ
					else if (property === 'color') {
						el.style.color = '#000000'; // Màu đen cho chữ
					}
					// Nếu là màu viền
					else if (property === 'borderColor') {
						el.style.borderColor = '#cccccc'; // Màu xám cho viền
					}
					// Nếu là bóng viền
					else if (property === 'boxShadow') {
						el.style.boxShadow = 'none'; // Không có bóng viền
					}
				}
			});
		});

		// Ẩn các icon nếu có
		document.querySelectorAll('.icon-class').forEach(function(el) {
			el.style.display = 'none'; // Ẩn các icon
		});

		// Kiểm tra xem phần tử .container-content có tồn tại không trước khi gọi dom-to-image
		const contentElement = document.querySelector(".container-content");

		if (contentElement) {
			// Sử dụng dom-to-image để chuyển nội dung thành ảnh PNG
			domtoimage.toPng(contentElement)
				.then(function(dataUrl) {
					const { jsPDF } = window.jspdf;
					const doc = new jsPDF();

					// Tính toán kích thước ảnh sao cho phù hợp với trang PDF
					const imgWidth = doc.internal.pageSize.getWidth() - 20; // Tạo chiều rộng cố định cho ảnh
					const imgHeight = (contentElement.offsetHeight * imgWidth) / contentElement.offsetWidth; // Tính chiều cao tương ứng để giữ tỷ lệ

					// Đảm bảo hình ảnh được thêm vào PDF mà không bị lệch
					doc.addImage(dataUrl, 'PNG', 10, 10, imgWidth, imgHeight); // Thêm lề để căn chỉnh
					doc.save('hoa-don.pdf');
				})
				.catch(function(error) {
					console.error('Lỗi khi tạo ảnh từ DOM:', error);
				});
		} else {
			console.error('Phần tử .container-content không tìm thấy!');
		}
	});
	fomatPrice();
});

async function backHomeForm() {
	window.location.href = '/home';
}
function fomatPrice() {

	const priceElements = document.querySelectorAll('.price-tour');
	console.log(priceElements);
	priceElements.forEach(function(priceElement) {
		let price = parseFloat(priceElement.textContent); // Chuyển đổi sang số
		if (!isNaN(price)) {
			// Định dạng và loại bỏ khoảng trắng giữa số và ký tự ₫
			priceElement.textContent = price.toLocaleString('vi-VN');
		}
	});
	const originPriceElements = document.querySelectorAll('.tourOriginalPrice');
	originPriceElements.forEach(function(priceElement) {
		let price = parseFloat(priceElement.textContent); // Chuyển đổi sang số
		if (!isNaN(price)) {
			// Định dạng và loại bỏ khoảng trắng giữa số và ký tự ₫
			priceElement.textContent = price.toLocaleString('vi-VN') + '₫';
		}
	});
}
/*document.addEventListener('DOMContentLoaded', function() {
	document.getElementById("bt-continuteShop").addEventListener("click", backHomeForm);

	document.querySelector("#print-button").addEventListener("click", function () {
			// Chuyển đổi tất cả các màu sắc không hợp lệ thành các giá trị hợp lệ
			document.querySelectorAll('*').forEach(function(el) {
				const computedStyle = window.getComputedStyle(el);
				const propertiesToCheck = ['backgroundColor', 'color', 'borderColor', 'outlineColor', 'boxShadow'];

				propertiesToCheck.forEach(property => {
					if (computedStyle[property] && computedStyle[property].includes('oklch')) {
						// Chuyển đổi màu không hợp lệ thành màu hợp lệ
						let newColor = "#ffffff";  // Default to white color

						// Chuyển đổi nếu là màu nền
						if (property === 'backgroundColor') {
							el.style.backgroundColor = newColor; // Màu trắng cho nền
						}
						// Nếu là màu chữ
						else if (property === 'color') {
							el.style.color = '#000000'; // Màu đen cho chữ
						}
						// Nếu là màu viền
						else if (property === 'borderColor') {
							el.style.borderColor = '#cccccc'; // Màu xám cho viền
						}
						// Nếu là bóng viền
						else if (property === 'boxShadow') {
							el.style.boxShadow = 'none'; // Không có bóng viền
						}
					}
				});
			});

			// Kiểm tra xem phần tử .container-content có tồn tại không trước khi gọi dom-to-image
			const contentElement = document.querySelector(".container-content");
			if (contentElement) {
				// Sử dụng dom-to-image thay vì html2canvas
				domtoimage.toPng(contentElement)
					.then(function(dataUrl) {
						// Tải xuống hình ảnh PNG
						const link = document.createElement('a');
						link.href = dataUrl;
						link.download = 'hoa-don.png';
						link.click();
					})
					.catch(function(error) {
						console.error('Lỗi khi tạo ảnh từ DOM:', error);
					});
			} else {
				console.error('Phần tử .container-content không tìm thấy!');
			}
		});
});

async function backHomeForm() {
	window.location.href = '/home';
}*/
