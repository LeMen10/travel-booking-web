package WebApplication.WebTour.Controllers.User;

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

    @GetMapping("/search")
    public String showPageSearch(@RequestParam("s") String searchParam,
                                 @RequestParam(defaultValue = "0") int page, // Mặc định là trang 0
                                 @RequestParam(defaultValue = "8") int size, // Mặc định là 10 kết quả mỗi trang
                                 Model model) {

        // Tạo đối tượng Pageable để thực hiện phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Gọi repository để tìm kiếm theo từ khóa và phân trang
        Page<Tours> searchResults = toursRepository.findByTourNameContainingIgnoreCase(searchParam, pageable);

        // Đưa kết quả vào model để đẩy lên view
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("searchParam", searchParam);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", searchResults.getTotalPages());

        return "/User/search"; // Trả về trang search.html
    }
	
}
