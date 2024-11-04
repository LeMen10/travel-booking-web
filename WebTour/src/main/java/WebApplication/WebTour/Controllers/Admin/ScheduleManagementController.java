package WebApplication.WebTour.Controllers.Admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import WebApplication.WebTour.Model.Schedules;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.SchedulesRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Service.ScheduleManagementService;
import WebApplication.WebTour.Service.TourService;

@Controller
public class ScheduleManagementController {
	@Autowired
	SchedulesRepository schedulesRepository;
	@Autowired
	ToursRepository toursRepository;
	@Autowired
	private TourService toursService;
	@Autowired
	ScheduleManagementService scheduleManagementService;
	
	
	// hiển thị dữ liệu lên trang schedule management (admin)
	@GetMapping("/admin/schedule_management")
	public String scheduleManagementPage(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, Model model) {
		Pageable pageable = PageRequest.of(page, size);
	    Page<Tours> tourPage = toursService.getToursByPage(pageable);

	    if (tourPage.isEmpty()) {
	        model.addAttribute("error", "Không có tour nào tồn tại!");
	    } else {
	        model.addAttribute("tourPage", tourPage);
	        model.addAttribute("currentPage", page);
	        model.addAttribute("totalPages", tourPage.getTotalPages());
	    }

	    return "admin/schedule_management"; 
	}
	
	
	@GetMapping("/admin/schedule_management/data")
	@ResponseBody
	public Page<Tours> getToursData(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    return toursService.getToursByPage(pageable);
	}


	// tìm kiếm tên tour 
    @GetMapping("/admin/schedule_management-search/search")
    @ResponseBody
    public Page<Tours> searchTour(
            @RequestParam("tourName") String tourName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return toursService.searchTourName(tourName, pageable);
    }
    
    //chuyển đến trang chỉnh sửa tour
    @GetMapping("/api-edit-schedule/{tourId}")
    public String editSchedulePage(@PathVariable Long tourId, Model model) {
    	Tours tour = toursService.getTourById(tourId);
        List<Schedules> schedules = scheduleManagementService.getScheduleByTourId(tourId);
        model.addAttribute("tour", tour);
        model.addAttribute("schedules", schedules);
        return "admin/editSchedule";
    }
}
