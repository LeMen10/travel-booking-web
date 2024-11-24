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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Service.PromotionManagementService;
import jakarta.mail.internet.ParseException;
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
			@RequestParam(value = "size", defaultValue = "3") int size, Model model) {
		Page<Object[]> promotions = promotionManagementService.getPromotionsActive(PageRequest.of(page, size));

		model.addAttribute("promotions", promotions.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", promotions.getTotalPages());

		return "/Admin/promotion_management";
	}

	@PostMapping("/admin/promotion-add")
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

	@PostMapping("/admin/promotion-detail-add")
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

			Promotions promotion = promotionManagementService
					.findPromotionById(promotionDetail.getPromotions().getPromotionId());
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
	public ResponseEntity<Page<Object[]>> getPromotionsByDate(
	        @RequestParam("startDate") String startDateString,
	        @RequestParam("endDate") String endDateString,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "2") int size) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> promotions = promotionManagementService.getPromotionsByDateRange(startDate, endDate, pageable);
            return ResponseEntity.ok(promotions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
	}
	
	@GetMapping("/admin/get-promotion-by-id")
	public ResponseEntity<?> getPromotionById(@RequestParam("id") Long promotionId) {
        try {
            Optional<Promotions> promotion = promotionManagementService.getPromotionById(promotionId);
            if (promotion.isPresent()) {
                return ResponseEntity.ok(promotion.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Promotion with ID " + promotionId + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching promotion details: " + e.getMessage());
        }
    }
	
	@PutMapping("/admin/promotion-edit")
	public ResponseEntity<Map<String, Object>> editPromotion(@RequestBody Promotions promotion) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	    	
	        Optional<Promotions> existingPromotion = promotionManagementService.findByCode(promotion.getCode());
	        if (existingPromotion.isPresent()) {
	            Promotions existing = existingPromotion.get();
	            
	            existing.setDiscount(promotion.getDiscount());
	            existing.setStartDate(promotion.getStartDate());
	            existing.setEndDate(promotion.getEndDate());
	            existing.setDescription(promotion.getDescription());
	            existing.setCumulativePoints(promotion.getCumulativePoints());

	            promotionManagementService.savePromotion(existing);
	            response.put("success", true);
	            response.put("status", 200);
	            response.put("message", "Cập nhật thành công!");
	            return ResponseEntity.ok(response);
	        }
	        response.put("success", false);
	        response.put("status", 404);
	        response.put("message", "Khuyến mãi không tồn tại!");
	        return ResponseEntity.status(404).body(response);

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("status", 500);
	        response.put("message", "Đã xảy ra lỗi trong quá trình xử lý!");
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(500).body(response);
	    }
	}
	
	@DeleteMapping("/admin/promotion-delete")
	public ResponseEntity<Map<String, Object>> deletePromotion(@RequestBody Map<String, Long> payload) {
	    Map<String, Object> response = new HashMap<>();

	    try {

	        Long promotionId = payload.get("promotionId");
	        Optional<Promotions> promotion = promotionManagementService.findById(promotionId);
	        if (promotion.isPresent()) {

	            promotionManagementService.deletePromotion(promotionId);

	            response.put("success", true);
	            response.put("status", 200);
	            response.put("message", "Xóa khuyến mãi thành công!");
	            return ResponseEntity.ok(response);
	        }

	        response.put("success", false);
	        response.put("status", 404);
	        response.put("message", "Khuyến mãi không tồn tại!");
	        return ResponseEntity.status(404).body(response);

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("status", 500);
	        response.put("message", "Đã xảy ra lỗi trong quá trình xóa khuyến mãi!");
	        response.put("error", e.getMessage());
	        return ResponseEntity.status(500).body(response);
	    }
	}


}
