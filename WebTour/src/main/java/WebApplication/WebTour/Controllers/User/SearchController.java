package WebApplication.WebTour.Controllers.User;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.ast.Instanceof;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.ReviewsRepository;
import WebApplication.WebTour.Respository.ToursRepository;


@Controller
public class SearchController {
	
	@Autowired
    private ToursRepository toursRepository;
	@Autowired
	private ReviewsRepository reviewsRepository;
	@Autowired
	private ProvinceRepository provinceRepository;

//    @GetMapping("/search")
//    public String showPageSearch(@RequestParam("s") String searchParam,
//                                 @RequestParam(defaultValue = "0") int page, // Mặc định là trang 0
//                                 @RequestParam(defaultValue = "8") int size, // Mặc định là 10 kết quả mỗi trang
//                                 Model model) {
//
//        // Tạo đối tượng Pageable để thực hiện phân trang
//        Pageable pageable = PageRequest.of(page, size);
//
//        // Gọi repository để tìm kiếm theo từ khóa và phân trang
//        Page<Tours> searchResults = toursRepository.findByTourNameContainingIgnoreCase(searchParam, pageable);
//
//        // Đưa kết quả vào model để đẩy lên view
//        model.addAttribute("searchResults", searchResults);
//        model.addAttribute("searchParam", searchParam);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", searchResults.getTotalPages());
//
//        return "/User/search"; // Trả về trang search.html
//    }
	@GetMapping("/search")
	public String showPageSearch(
	        @RequestParam(value = "tourName", required = false) String tourName,
	        @RequestParam(value = "startDate", required = false) String startDate,
	        @RequestParam(value = "departure", required = false) String departure,
	        @RequestParam(value = "destination", required = false) String destination,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "8") int size,
	        Model model) {

	    // Chuyển đổi startDate từ String sang java.sql.Date nếu cần
	    java.sql.Date sqlStartDate = null;
	    if (startDate != null && !startDate.isEmpty()) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày người dùng nhập
	            java.util.Date parsedDate = sdf.parse(startDate); // Phân tích ngày tháng
	            sqlStartDate = new java.sql.Date(parsedDate.getTime()); // Chuyển đổi sang java.sql.Date
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	    System.out.println("\n sqlStartDate"+startDate);
	    // Tạo đối tượng Pageable để thực hiện phân trang
	    Pageable pageable = PageRequest.of(page, size);

	    // Gọi repository để tìm kiếm theo các điều kiện nếu có
	    Page<Object[]> searchResults = toursRepository.findTours(
	            tourName != null && !tourName.isEmpty() ? tourName : null,
	            sqlStartDate, // Sử dụng biến đã chuyển đổi
	            departure != null && !departure.isEmpty() ? departure : null,
	            destination != null && !destination.isEmpty() ? destination : null,
	            pageable
	    );
	    List<Object[]> updatedResults = new ArrayList<>(searchResults.getContent()); // Lấy danh sách từ Page

	    // Cập nhật danh sách
	    for (int i = 0; i < updatedResults.size(); i++) {
	        Object[] tour = updatedResults.get(i);


	        // Tạo mảng mới với 1 phần tử thêm cho đánh giá
	        Object[] newTour = new Object[tour.length + 2];
	        System.arraycopy(tour, 0, newTour, 0, tour.length);
	        
	        List<Object[]> listRate = reviewsRepository.getRateOfTour((Long) tour[0]).get();
	        // Lấy đánh giá của tour
	        newTour[tour.length] = listRate.get(0)[1];
	        newTour[tour.length + 1] = listRate.get(0)[0];
	        
	        // Cập nhật lại phần tử trong danh sách
	        updatedResults.set(i, newTour);
	    }

	    // Tạo lại Page từ List sau khi cập nhật
	    Page<Object[]> updatedPage = new PageImpl<>(updatedResults, pageable, searchResults.getTotalElements());
	    
	    // Đưa kết quả vào model để đẩy lên view
	    model.addAttribute("searchResults", updatedPage);
	    model.addAttribute("tourName", tourName);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("departure", departure);
	    model.addAttribute("destination", destination);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", searchResults.getTotalPages());
	    model.addAttribute("province",provinceRepository.findAll());

	    return "/User/search"; // Trả về trang search.html
	}
	
	@GetMapping("/api-get-search-tour")
	@ResponseBody
	public Page<Object[]> getSearchToursData(@RequestParam(value = "tourName", required = false) String tourName,
	        @RequestParam(value = "startDate", required = false) String startDate,
	        @RequestParam(value = "departure", required = false) String departure,
	        @RequestParam(value = "destination", required = false) String destination,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "8") int size,
	        Model model) {
		java.sql.Date sqlStartDate = null;
	    if (startDate != null && !startDate.isEmpty()) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Định dạng ngày người dùng nhập
	            java.util.Date parsedDate = sdf.parse(startDate); // Phân tích ngày tháng
	            sqlStartDate = new java.sql.Date(parsedDate.getTime()); // Chuyển đổi sang java.sql.Date
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	    }
	    // Tạo đối tượng Pageable để thực hiện phân trang
	    Pageable pageable = PageRequest.of(page, size);

	    // Gọi repository để tìm kiếm theo các điều kiện nếu có
	    Page<Object[]> searchResults = toursRepository.findTours(
	            tourName != null && !tourName.isEmpty() ? tourName : null,
	            sqlStartDate, // Sử dụng biến đã chuyển đổi
	            departure != null && !departure.isEmpty() ? departure : null,
	            destination != null && !destination.isEmpty() ? destination : null,
	            pageable
	    );
	    List<Object[]> updatedResults = new ArrayList<>(searchResults.getContent()); // Lấy danh sách từ Page

	    // Cập nhật danh sách
	    for (int i = 0; i < updatedResults.size(); i++) {
	        Object[] tour = updatedResults.get(i);

	        // Tạo mảng mới với 1 phần tử thêm cho đánh giá
	        Object[] newTour = new Object[tour.length + 2];
	        System.arraycopy(tour, 0, newTour, 0, tour.length);
	        
	        List<Object[]> listRate = reviewsRepository.getRateOfTour((Long) tour[0]).get();
	        System.out.println(tour[7]);
	        // Lấy đánh giá của tour
	        newTour[tour.length] = listRate.get(0)[1];
	        newTour[tour.length + 1] = listRate.get(0)[0];
	        
	        // Cập nhật lại phần tử trong danh sách
	        updatedResults.set(i, newTour);
	    }

	    // Tạo lại Page từ List sau khi cập nhật
	    Page<Object[]> updatedPage = new PageImpl<>(updatedResults, pageable, searchResults.getTotalElements());
	    
	    return updatedPage;
	}
	
}
