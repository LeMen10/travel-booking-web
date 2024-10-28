package WebApplication.WebTour.Controllers.Admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

//import ch.qos.logback.core.model.Model;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller
public class TourManagamentController {
	@Autowired
	ToursRepository toursRepository;

	@GetMapping("/admin/get-tours")
	public ResponseEntity<Map<String, Object>> getTours(@RequestParam Long promotionId) {
	    List<Tours> tours = toursRepository.getToursAboutToBegin(promotionId);

	    Map<String, Object> response = new HashMap<>();
	    response.put("tours", tours);

	    return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/admin/tours-management")
    public String toursManagementPage(Model model) {
    	 	List<Tours> tours = toursRepository.findAll();
    	 	
    	 	model.addAttribute("tours", tours);
    		return "/Admin/tours_management";
    }

}
