package WebApplication.WebTour.Controllers.Admin;

import java.io.IOException;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Guides;
import WebApplication.WebTour.Model.Image;
import WebApplication.WebTour.Model.Schedules;
//import ch.qos.logback.core.model.Model;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.GuidesRepository;
import WebApplication.WebTour.Respository.ImageRepository;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.ReviewsRepository;
import WebApplication.WebTour.Respository.SchedulesRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller
public class TourManagamentController {
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	ImageRepository imageRepository;

	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	SchedulesRepository schedulesRepository;
	
	@Autowired
	GuidesRepository guidesRepository;
	
	@Autowired
	ReviewsRepository reviewsRepository;
	
	@GetMapping("/admin/get-tours")
	public ResponseEntity<Map<String, Object>> getTours(@RequestParam Long promotionId) {
	    List<Tours> tours = toursRepository.getToursAboutToBegin(promotionId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("tours", tours);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/api-get-tour-by-id")
	public ResponseEntity<Tours> getTourById(@RequestParam Long tourId) {
	    Optional<Tours> tourOptional = toursRepository.findById(tourId);

	    if (tourOptional.isPresent()) {
	        return new ResponseEntity<>(tourOptional.get(), HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	@GetMapping("/api-get-image-by-id")
	public ResponseEntity<List<Image>> getImageById(@RequestParam Long tourId) {
		Optional<Tours> tour = toursRepository.findById(tourId);
	    List<Image> imageOptional = imageRepository.getImageOfTour(tour.get().getTourId());

	    if (imageOptional!=null) {
	        return new ResponseEntity<>(imageOptional, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}
	@GetMapping("/api-update-status-image")
	public ResponseEntity<?> deleteImage(@RequestParam Long imageId) {
	    try {
	        imageRepository.updateStatusByImageId(imageId);
	        return ResponseEntity.ok("Image deleted successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
	    }
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
    	    model.addAttribute("province",provinceRepository.findAll());
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
	 
	@PostMapping("/api-create-tours")
	public ResponseEntity<Long> createTour(
	        @RequestParam String tourName,
	        @RequestParam String departure,
	        @RequestParam String destination,
	        @RequestParam String startDate,
	        @RequestParam String endDate,
	        @RequestParam String detail,
	        @RequestParam int peopleMax,
	        @RequestParam float price,
	        @RequestParam String transport,
	        @RequestParam int quantity) {

	    // Chuyển đổi String thành Date
	    Date sqlStartDate = Date.valueOf(startDate);
	    Date sqlEndDate = Date.valueOf(endDate);

	    // Tạo đối tượng Tour và set giá trị từ các tham số
	    Tours newTour = new Tours();
	    newTour.setTourName(tourName);
	    newTour.setDeparture(departure);
	    newTour.setDestination(destination);
	    newTour.setStartDate(sqlStartDate);  // Set giá trị startDate
	    newTour.setEndDate(sqlEndDate);      // Set giá trị endDate
	    newTour.setDetail(detail);
	    newTour.setPeopleMax(peopleMax);
	    newTour.setPrice(price);
	    newTour.setOriginalPrice(price);
	    newTour.setStatus(true);
	    newTour.setTransport(transport);
	    newTour.setQuantity(quantity);
	    newTour.setOriginalPrice(price);
	    
	    Optional<Guides> guide = guidesRepository.findById((long) 1);
	    newTour.setGuides(guide.get());

	    // Lưu Tour mới vào cơ sở dữ liệu
	    toursRepository.save(newTour);
	    System.out.println("aaaaaaaaaaa"+newTour.getTourId());
	    // Trả về TourId sau khi lưu
	    return ResponseEntity.ok(newTour.getTourId());
	}
	@PostMapping("/api-update-tours/{tourId}")
	public ResponseEntity<Long> updateTour(
			@PathVariable Long tourId,
	        @RequestParam String tourName,
	        @RequestParam String departure,
	        @RequestParam String destination,
	        @RequestParam String startDate,
	        @RequestParam String endDate,
	        @RequestParam String detail,
	        @RequestParam int peopleMax,
	        @RequestParam float price,
	        @RequestParam String transport,
	        @RequestParam int quantity) {

	    // Chuyển đổi String thành Date
	    Date sqlStartDate = Date.valueOf(startDate);
	    Date sqlEndDate = Date.valueOf(endDate);

	    // Tạo đối tượng Tour và set giá trị từ các tham số
	    Optional<Tours> newTour = toursRepository.findById(tourId);
	    newTour.get().setTourName(tourName);
	    newTour.get().setDeparture(departure);
	    newTour.get().setDestination(destination);
	    newTour.get().setStartDate(sqlStartDate);  // Set giá trị startDate
	    newTour.get().setEndDate(sqlEndDate);      // Set giá trị endDate
	    newTour.get().setDetail(detail);
	    newTour.get().setPeopleMax(peopleMax);
	    newTour.get().setPrice(price);
	    newTour.get().setOriginalPrice(price);
	    newTour.get().setStatus(true);
	    newTour.get().setTransport(transport);
	    newTour.get().setQuantity(quantity);
	    Optional<Guides> guide = guidesRepository.findById((long) 1);
	    newTour.get().setGuides(guide.get());

	    // Lưu Tour mới vào cơ sở dữ liệu
	    toursRepository.save(newTour.get());
	    
	    // Trả về TourId sau khi lưu
	    return ResponseEntity.ok(newTour.get().getTourId());
	}
	@PostMapping("/api-schedule-tour-management")
    public ResponseEntity<?> saveSchedules(@RequestBody Map<String, Object> requestData) {
        // Lấy tourId từ requestData
		Long tourId = ((Number) requestData.get("tourId")).longValue();


        // Lấy schedules từ requestData (schedules là một List)
        List<Map<String, Object>> schedules = (List<Map<String, Object>>) requestData.get("schedules");

        // In thông tin để kiểm tra
        Optional<Tours> tour = toursRepository.findById(tourId);
        schedules.forEach(schedule -> {
        	Schedules newSchedule = new Schedules();
            Integer step = (Integer) schedule.get("step");
            String activity = (String) schedule.get("activity");
            String location = (String) schedule.get("location");

            newSchedule.setTours(tour.get());
            newSchedule.setActivity(activity);
            newSchedule.setStep(step);
            newSchedule.setStatus(true);
            newSchedule.setLocation(location);
            // In thông tin của từng schedule
            schedulesRepository.save(newSchedule);
        });
        
        return ResponseEntity.ok("Dữ liệu nhận thành công");
    }
	@GetMapping("/api-get-schedule")
	@ResponseBody
	public List<Schedules> getSchedule(@RequestParam("tourId") Long tourId) {
		System.out.println(tourId+"ádasd");
		List<Schedules> schedules = schedulesRepository.getScheduleByTourId(tourId);
	        return schedules;
	}
	
	@GetMapping("/api-get-review")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getReview(@RequestParam("tourId") Long tourId) {
	    List<Object[]> results = reviewsRepository.getReviewByTourId(tourId).orElse(Collections.emptyList());
	    
	    // Chuyển đổi Object[] thành Map để trả về JSON cấu trúc
	    List<Map<String, Object>> reviews = results.stream().map(row -> {
	        Map<String, Object> review = new HashMap<>();
	        review.put("reviewsId", row[0]);
	        review.put("tourId", row[1]);
	        review.put("rate", row[2]);
	        review.put("comment", row[3]);
	        review.put("reviewDate", row[4]);
	        review.put("fullName", row[5]);
	        return review;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(reviews);
	}
	@GetMapping("/api-delete-review")
	public ResponseEntity<?> deleteReview(@RequestParam Long reviewId) {
	    try {
	    	reviewsRepository.deleteReviewById(reviewId);
	        return ResponseEntity.ok("Image deleted successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
	    }
	}
}
