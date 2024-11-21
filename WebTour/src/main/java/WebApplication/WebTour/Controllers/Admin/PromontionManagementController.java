package WebApplication.WebTour.Controllers.Admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Service.PromotionManagementService;
import WebApplication.WebTour.Model.Promotiondetail;
import WebApplication.WebTour.Respository.PromotiondetailRepository;

@Controller
public class PromontionManagementController {

	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	PromotionManagementService promotionManagementService;
	@Autowired
	PromotiondetailRepository promotionDetailRepository;

	@GetMapping("/admin/promotion-management")
	public String showOrderPage(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "2") int size, Model model) {
		Page<Object[]> promotions = promotionManagementService.getPromotionsActive(PageRequest.of(page, size));

		model.addAttribute("promotions", promotions.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", promotions.getTotalPages());

		return "/Admin/promotion_management";
	}

	@PostMapping("/admin/add-promotion")
	public ResponseEntity<Map<String, Object>> createPromotion(@RequestBody Promotions promotion) {
		boolean exists = promotionManagementService.existsByCode(promotion.getCode());

		Map<String, Object> response = new HashMap<>();

		if (exists) {
			response.put("success", false);
			response.put("message", "Mã khuyến mãi đã tồn tại!");
			response.put("status", 400);
			return ResponseEntity.badRequest().body(response);
		}

		promotionManagementService.savePromotion(promotion);
		response.put("success", true);
		response.put("status", 200);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/admin/add-promotion-detail")
	public ResponseEntity<Map<String, Object>> createPromotionDetail(@RequestBody Promotiondetail promotionDetail) {

		Map<String, Object> response = new HashMap<>();

		try {
			boolean exists = promotionManagementService.existsByTourIdAndPromotion(promotionDetail.getTourId(),
					promotionDetail.getPromotions().getPromotionId());
			if (exists) {
				response.put("status", 400);
				response.put("message", "Promotion detail for this tour and promotion already exists");
				return ResponseEntity.status(400).body(response);
			}

			Promotions promotion = promotionManagementService.findPromotionById(promotionDetail.getPromotions().getPromotionId());
			promotionDetail.setPromotions(promotion);
			promotionManagementService.savePromotionDetail(promotionDetail);

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
	public ResponseEntity<Map<String, Object>> getPromotionsByDate(@RequestParam("startDate") String startDateString,
			@RequestParam("endDate") String endDateString) {

		Map<String, Object> response = new HashMap<>();

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = formatter.parse(startDateString);
			Date endDate = formatter.parse(endDateString);

			List<Promotions> promotions = promotionManagementService.getPromotionsByDateRange(startDate, endDate);

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
