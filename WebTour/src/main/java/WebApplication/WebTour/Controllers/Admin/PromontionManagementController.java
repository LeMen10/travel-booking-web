package WebApplication.WebTour.Controllers.Admin;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Model.Promotiondetail;
import WebApplication.WebTour.Respository.PromotiondetailRepository;


@Controller
public class PromontionManagementController {

	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	PromotiondetailRepository promotionDetailRepository;

	@GetMapping("/admin/promotion-management")
	public String customerManagementPage(Model model) {
		List<Promotions> promotions = promotionsRepository.getPromotionsActive();
		model.addAttribute("promotions", promotions);
		return "/Admin/promotion_management";
	}

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

	@PostMapping("/admin/add-promotion-detail")
	public ResponseEntity<Map<String, Object>> createPromotionDetail(@RequestBody Promotiondetail promotionDetail) {

		Map<String, Object> response = new HashMap<>();

		try {
			
			boolean exists = promotionDetailRepository.existsByTourIdAndPromotionId(promotionDetail.getTourId(), 
					promotionDetail.getPromotionId());
            if (exists) {
                response.put("status", 400);
                response.put("message", "Promotion detail for this tour and promotion already exists");
                return ResponseEntity.status(400).body(response);
            }
            
			promotionDetailRepository.save(promotionDetail);

			response.put("status", 200);
			response.put("message", "Promotion detail has been saved successfully");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "An error occurred while saving promotion detail");
			response.put("error", e.getMessage());

			return ResponseEntity.status(500).body(response);
		}
	}
	
	@GetMapping("/admin/get-promotion-by-date")
    public ResponseEntity<Map<String, Object>> getPromotionsByDate(
        @RequestParam("startDate") String startDateString, 
        @RequestParam("endDate") String endDateString) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = formatter.parse(startDateString);
            Date endDate = formatter.parse(endDateString);

            List<Promotions> promotions = promotionsRepository.findPromotionsByDateRange(startDate, endDate);

            response.put("status", 200);
            response.put("promotions", promotions);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", 500);
            response.put("message", "An error occurred while fetching promotions by date");
            response.put("error", e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }
}
