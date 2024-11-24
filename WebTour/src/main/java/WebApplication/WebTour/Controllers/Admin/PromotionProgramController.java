package WebApplication.WebTour.Controllers.Admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String promotionaProgramPage(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "2") int size, Model model) {
		Page<Object[]> promotionPrograms = promotionProgramService.getPromotionPrograms(PageRequest.of(page, size));
        model.addAttribute("promotionPrograms", promotionPrograms.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", promotionPrograms.getTotalPages());
		return "/Admin/promotion_program";
	}
	
	
    @PostMapping("/admin/create-promotion-program")
    public ResponseEntity<?> createPromotion(@RequestBody PromotionProgram newPromotion) {
        try {
            promotionProgramService.createPromotion(newPromotion);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
    		response.put("status", 200);
    		return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi tạo chương trình khuyến mãi");
        }
    }
}
