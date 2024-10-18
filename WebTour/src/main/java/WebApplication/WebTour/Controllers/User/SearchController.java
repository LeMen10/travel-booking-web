package WebApplication.WebTour.Controllers.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.ToursRepository;


@Controller
public class SearchController {
	
	@Autowired
    private ToursRepository toursRepository;

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
	    Page<Tours> searchResults = toursRepository.findTours(
	            tourName != null && !tourName.isEmpty() ? tourName : null,
	            sqlStartDate, // Sử dụng biến đã chuyển đổi
	            departure != null && !departure.isEmpty() ? departure : null,
	            destination != null && !destination.isEmpty() ? destination : null,
	            pageable
	    );
	    
	    // Đưa kết quả vào model để đẩy lên view
	    model.addAttribute("searchResults", searchResults);
	    model.addAttribute("tourName", tourName);
	    model.addAttribute("startDate", startDate);
	    model.addAttribute("departure", departure);
	    model.addAttribute("destination", destination);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", searchResults.getTotalPages());

	    return "/User/search"; // Trả về trang search.html
	}
	
}
