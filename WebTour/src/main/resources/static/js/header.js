document.addEventListener('DOMContentLoaded', function () {
    const breadcrumb = document.getElementById('breadcrumb');
    
    function updateBreadcrumb(page) {
        breadcrumb.innerHTML = `<span>Trang chủ</span>  >  <span><b>${page}</b></span>`;
    }

    document.getElementById('about-link').addEventListener('click', function () {
        updateBreadcrumb('Giới thiệu');
    });
    
    document.getElementById('tour-link').addEventListener('click', function () {
        updateBreadcrumb('Tour du lịch');
    });
    
    document.getElementById('news-link').addEventListener('click', function () {
        updateBreadcrumb('Tin tức');
    });
    
    document.getElementById('faq-link').addEventListener('click', function () {
        updateBreadcrumb('FAQs');
    });
    
    document.getElementById('contact-link').addEventListener('click', function () {
        updateBreadcrumb('Liên hệ');
    });
});



