package WebApplication.WebTour.Controllers.User;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.ToursRepository;

@Controller  // Use @Controller for returning views
public class DetailTourController {
	@Autowired
	ToursRepository toursRepository;
	
	@GetMapping("/detail-tour")
	public String GetDetailTour(Model model) {
		Optional<Tours> detailTour = toursRepository.findById(1l);
		model.addAttribute(detailTour);
		return "/User/detailTour";
	}
	
	@GetMapping("/detail-tour/{id}")
    public String getDetailTour(@PathVariable("id") Long id, Model model) {
        Optional<Tours> detailTour = toursRepository.findById(id);
        // Kiểm tra nếu có giá trị trong Optional
        if (detailTour.isPresent()) {
            model.addAttribute("tour", detailTour.get());
        } else {
            model.addAttribute("error", "Tour không tồn tại!");
        }
        return "/User/detailTour";
    }
	
}
