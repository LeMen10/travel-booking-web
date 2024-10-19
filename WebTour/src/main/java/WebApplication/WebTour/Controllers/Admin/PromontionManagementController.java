package WebApplication.WebTour.Controllers.Admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Respository.PromotionsRepository;

@Controller
public class PromontionManagementController {

	@Autowired
	PromotionsRepository promotionsRepository;

    @GetMapping("/admin/promotion-management")
    public String customerManagementPage(Model model) {
    	 	List<Promotions> promotions = promotionsRepository.getPromotionsActive();
    	 	model.addAttribute("promotions", promotions);
    		return "/Admin/promotion_management";
    }
    
//    @PostMapping("/admin/add-promotion")
//    public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody Promotions promotion) {
//        promotionsRepository.save(promotion);
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//        response.put("status", 200);
//        return ResponseEntity.ok(response);
//        
//        
//    }
    
    @PostMapping("/admin/add-promotion")
    public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody Promotions promotion) {
        boolean exists = promotionsRepository.existsByCode(promotion.getCode());
        
        Map<String, Object> response = new HashMap<>();
        
        if (exists) {
            response.put("success", false);
            response.put("message", "Mã khuyến mãi đã tồn tại!");
            response.put("status", 400);
            return ResponseEntity.badRequest().body(response);
        }

        promotionsRepository.save(promotion);
        response.put("success", true);
        response.put("status", 200);
        return ResponseEntity.ok(response);
    }

    
}
