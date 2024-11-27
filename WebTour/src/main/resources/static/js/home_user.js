const searchBox = document.getElementById('searchBox');
            const emptyBox = document.getElementById('emptyBox');
            const searchBtn = document.querySelector('.btn-search');
        
            // Hàm để hiển thị hoặc ẩn search-box với hiệu ứng
            const toggleSearchBox = () => {
                if (searchBox.classList.contains('active')) {
                    // Nếu đang hiển thị, ẩn đi
                    searchBox.classList.remove('active');
                    setTimeout(() => {
                        searchBox.style.display = 'none'; // Ẩn hoàn toàn sau khi hiệu ứng kết thúc
                    }, 300); // Thời gian trùng khớp với thời gian hiệu ứng CSS
                } else {
                    // Nếu đang ẩn, hiển thị
                    searchBox.style.display = 'flex'; // Hiển thị trước khi thêm lớp active
                    setTimeout(() => {
                        searchBox.classList.add('active'); // Thêm lớp active để hiển thị hiệu ứng
                    }, 10); // Thời gian nhỏ để đảm bảo display: flex đã được áp dụng
                }
            };
        
            // Hiển thị search-box khi nhấn vào nút tìm kiếm
            searchBtn.addEventListener('click', toggleSearchBox);
        
            // Ẩn search-box khi nhấn vào nền (empty-box)
            emptyBox.addEventListener('click', () => {
                searchBox.classList.remove('active'); // Xóa lớp active khi nhấn vào nền
                setTimeout(() => {
                    searchBox.style.display = 'none'; // Ẩn hoàn toàn sau khi hiệu ứng kết thúc
                }, 300); // Thời gian trùng khớp với thời gian hiệu ứng CSS
            });
             // Lấy các phần tử
                const searchInput = document.getElementById('searchInput');
                const mainsearchInput = document.getElementById('mainSearchInput');
                const searchIcon = document.getElementById('searchIcon');
        		
                // Hàm chuyển hướng
                function redirectToSearch() {
                    const query = searchInput.value.trim(); // Lấy giá trị nhập vào và loại bỏ khoảng trắng thừa
                    if (query) {
                        // Chuyển hướng đến URL mới, có thể tùy chỉnh URL này theo nhu cầu
                        window.location.href = `http://localhost:8080/search?tourName=${encodeURIComponent(query)}`;
                    }else{
                	    alert("Vui lòng nhập thông tin tìm kiếm");
                	    return;
                    }
                }
                // Sự kiện nhấn Enter trong input
                searchInput.addEventListener('keydown', function (event) {
                    if (event.key === 'Enter') {
                        redirectToSearch();
                    }
                });
        
                // Sự kiện click vào icon tìm kiếm
                searchIcon.addEventListener('click', redirectToSearch);
                //Chỉnh placeholder cho chọn ngày đi
                flatpickr("#departureDate", {
                    dateFormat: "d-m-Y", // Định dạng ngày hiển thị
                    onChange: function(selectedDates, dateStr, instance) {
                        // Cập nhật giá trị cho ô input khi chọn ngày
                        document.getElementById('departureDate').value = dateStr;
                    }
                });
                //Khởi tạo Select2
                $(document).ready(function() {
                    $('.location-select').each(function() {
                var type = $(this).data('type'); // Lấy type từ thuộc tính data-type
                var placeholderText = type === 'departure' ? "Chọn điểm đi" : "Chọn điểm đến"; // Đặt placeholder tương ứng
        
                $(this).select2({
                    placeholder: placeholderText,
                    allowClear: true
                        });
                    });
                });
                $('.location-select').on('select2:open', function(e) {
                    $('.select2-results__options').hide().fadeIn(200); // 300ms là thời gian hiệu ứng
                });
        
                // Hiệu ứng Fade Out khi đóng select2
                $('.location-select').on('select2:close', function(e) {
                    $('.select2-results__options').fadeOut(200, function() {
                        $(this).hide(); // Sau khi hiệu ứng kết thúc, ẩn dropdown
                    });
                });
             // xử lý sự kiện ấn nút "tìm kiếm"
                document.getElementById('main-btn-search').addEventListener('click', function() {
                // Lấy giá trị từ các ô input và select
	                var tourName = document.getElementById('mainSearchInput').value;
	                var startDate = document.getElementById('departureDate').value;
	             // Chuyển đổi định dạng ngày từ dd-MM-yyyy sang yyyy-MM-dd
	                var formattedStartDate = "";
	                if (startDate) {
	                    var dateParts = startDate.split('-'); // Tách ngày, tháng, năm
	                    if (dateParts.length === 3) {
	                        formattedStartDate = dateParts[2] + '-' + dateParts[1] + '-' + dateParts[0]; // Chuyển đổi sang yyyy-MM-dd
	                    }
	                }
	             // Lấy text từ departureSelect
	                var departureSelect = document.getElementById('departureSelect');
	                var departureText = departureSelect.options[departureSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
	                var departure = (departureText === "Chọn điểm đi") ? "" : departureText; // Nếu chọn "Chọn điểm đi" thì gán rỗng
	            
	                // Lấy text từ destinationSelect
	                var destinationSelect = document.getElementById('destinationSelect');
	                var destinationText = destinationSelect.options[destinationSelect.selectedIndex].text; // Lấy text của tùy chọn đã chọn
	                var destination = (destinationText === "Chọn điểm đến") ? "" : destinationText; // Nếu chọn "Chọn điểm đến" thì gán rỗng // Lấy text của tùy chọn đã chọn
	                
	                console.log("Tour Name: ", tourName);
	                console.log("Start Date: ", formattedStartDate);
	                console.log("Departure: ", departure);
	                console.log("Destination: ", destination);
	        
	                // Tạo URL với các tham số
	                var searchUrl = '/search?'; // Đường dẫn tới trang tìm kiếm
	        
	                // Thêm các điều kiện vào URL nếu có giá trị
	                if (tourName) {
	                    searchUrl += 'tourName=' + encodeURIComponent(tourName) + '&';
	                }
	                if (startDate) {
	                    searchUrl += 'startDate=' + encodeURIComponent(formattedStartDate) + '&';
	                }
	                if (departure) {
	                    searchUrl += 'departure=' + encodeURIComponent(departure) + '&';
	                }
	                if (destination) {
	                    searchUrl += 'destination=' + encodeURIComponent(destination) + '&';
	                }
	        
	                // Loại bỏ dấu "&" cuối cùng
	                searchUrl = searchUrl.slice(0, -1);
	        
	                // Redirect đến URL mới
	                if(tourName=="" && startDate=="" && departure=="" && destination==""){
	        	    	alert("Vui lòng nhập thông tin tìm kiếm");
	        	    	return;
	        	    }else{
	        	    	window.location.href = searchUrl;	
	        	    }
	                
                });
             
                async function getUser(){
                	let userId = sessionStorage.getItem("userId");
	                
	                if (userId) {
	                    // URL để gọi API
	                    const url = `http://localhost:8080/api-get-user?userId=${userId}`;
	                    try {
	                        // Thực hiện request tới API
	                        const request = new Request(url, {
	                            method: "GET",
	                            headers: {
	                                "Content-Type": "application/json",
	                            }
	                        });
	
	                        // Gọi API và đợi kết quả trả về
	                        const response = await fetch(request);
	
	                        // Kiểm tra nếu có lỗi khi gọi API
	                        if (!response.ok) {
	                            console.log("Error with API response", response);
	                            return;
	                        }
	
	                        // Lấy dữ liệu user từ API
	                        const user = await response.json();
	                        console.log(user);
	                        var userName = user.fullName;
	                        console.log(userName);
	                        
	                        var login = document.getElementById("login");
	                        login.innerHTML=userName;
	                        login.href = "/account?userId="+userId;
	                        var register = document.getElementById("register");
	                        register.innerHTML='Đăng xuất';
	                        register.href = "/home";
	                        register.addEventListener('click', function(event) {
	                        	sessionStorage.removeItem('userId');
	               
	                        });
	                        
	                        
	                    } catch (error) {
	                        console.error("Error fetching user data:", error);
	                    }
	                } else {
	                    console.log("userId is null or empty");
	                }
                }
                getUser();
                fetch("/api-get-footer")
        		.then(response => response.text())
        		.then(data => {
        			document.getElementById("footer").innerHTML = data;
        		})