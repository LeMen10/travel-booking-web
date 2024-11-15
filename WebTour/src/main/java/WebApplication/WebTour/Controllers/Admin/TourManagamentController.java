package WebApplication.WebTour.Controllers.Admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import WebApplication.WebTour.Model.Image;
//import ch.qos.logback.core.model.Model;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.ImageRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller
public class TourManagamentController {
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	ImageRepository imageRepository;

	@GetMapping("/admin/get-tours")
	public ResponseEntity<Map<String, Object>> getTours(@RequestParam Long promotionId) {
	    List<Tours> tours = toursRepository.getToursAboutToBegin(promotionId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("tours", tours);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/admin/tours-management")
    public String toursManagementPage(
    		@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "8") int size,
	        Model model) {
    	 	Pageable pageable = PageRequest.of(page, size);
    		Page<Tours> searchResults = toursRepository.findAllByOriginalIdIsNull(pageable);
    		model.addAttribute("tours", searchResults);
    		model.addAttribute("currentPage", page);
    	    model.addAttribute("totalPages", searchResults.getTotalPages());
    		return "/Admin/tours_management";
    }
	@GetMapping("/admin/update-status")
	public ResponseEntity<String> updateTourStatus(@RequestParam Long tourId) {
	    try {
	        // Giả sử updateStatusByTourId là phương thức trong repository thực hiện việc cập nhật
	        toursRepository.updateStatusByTourId(tourId);
	        return new ResponseEntity<>("Xóa thành công", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("Xóa thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	@GetMapping("/api-get-tour-management")
	@ResponseBody
	public Page<Tours> getToursManagementPage(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "8") int size,
	        Model model) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Tours> searchResults = toursRepository.findAllByOriginalIdIsNull(pageable);
		return searchResults; 
	}
	
}
