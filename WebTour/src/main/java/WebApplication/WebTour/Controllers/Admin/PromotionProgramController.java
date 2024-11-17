package WebApplication.WebTour.Controllers.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import WebApplication.WebTour.Model.PromotionProgram;
import WebApplication.WebTour.Respository.PromotiondetailRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Service.PromotionProgramService;

@Controller
public class PromotionProgramController {

	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	PromotiondetailRepository promotionDetailRepository;
	@Autowired
    PromotionProgramService promotionProgramService;

	@GetMapping("/admin/promotion-program")
	public String promotionaProgramPage(Model model) {
		List<PromotionProgram> promotionPrograms = promotionProgramService.getPromotionPrograms();
        model.addAttribute("promotionPrograms", promotionPrograms);
		return "/Admin/promotion_program";
	}
	
	
    @PostMapping("/admin/create-promotion-program")
    public ResponseEntity<?> createPromotion(@RequestBody PromotionProgram newPromotion) {
        try {
            PromotionProgram savedPromotion = promotionProgramService.createPromotion(newPromotion);
            return ResponseEntity.ok(savedPromotion);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi tạo chương trình khuyến mãi");
        }
    }
}
