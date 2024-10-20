document.addEventListener("DOMContentLoaded", function () {
    fetch("/api-get-header")
        .then(response => response.text())
        .then(data => {
            document.getElementById("header").innerHTML = data;
            const searchBox = document.getElementById('searchBox');
            const emptyBox = document.getElementById('emptyBox');
            const searchBtn = document.querySelector('.btn-search');
            const searchIcon = document.getElementById('searchIcon');
            
            const toggleSearchBox = () => {
                if (searchBox.classList.contains('active')) {
                    searchBox.classList.remove('active');
                    setTimeout(() => {
                        searchBox.style.display = 'none';
                    }, 300);
                } else {
                    // Nếu đang ẩn, hiển thị
                    searchBox.style.display = 'flex';
                    setTimeout(() => {
                        searchBox.classList.add('active');
                    }, 10);
                }
            };
            searchBtn.addEventListener('click', toggleSearchBox);
            emptyBox.addEventListener('click', () => {
                searchBox.classList.remove('active');
                setTimeout(() => {
                    searchBox.style.display = 'none';
                }, 300);
            });

            const searchInput = document.getElementById('searchInput');
            searchIcon.addEventListener('click', redirectToSearch);
            searchInput.addEventListener('keydown', function (event) {
                if (event.key === 'Enter') {
                    redirectToSearch();
                }
            });
            function redirectToSearch() {
                const query = searchInput.value.trim();
                if (query) {
                    window.location.href = `http://localhost:8080/search?s=${encodeURIComponent(query)}`;
                }
            }
        });
});







