package WebApplication.WebTour.Controllers.Admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.PromotionProgram;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.PromotiondetailRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Service.PromotionProgramService;

@Controller
public class PromotionProgramController {

	@Autowired
	PromotionsRepository promotionsRepository;
	@Autowired
	PromotiondetailRepository promotionDetailRepository;
	@Autowired
	PromotionProgramService promotionProgramService;
	
	@Autowired
	ToursRepository toursRepository;

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

	@GetMapping("/admin/get-promotion-program-by-date")
	public ResponseEntity<Page<Object[]>> getPromotionsByDate(@RequestParam("startDate") String startDateString,
			@RequestParam("endDate") String endDateString, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "2") int size) {

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = dateFormat.parse(startDateString);
			Date endDate = dateFormat.parse(endDateString);
			Pageable pageable = PageRequest.of(page, size);
			Page<Object[]> promotions = promotionProgramService.getPromotionsByDateRange(startDate, endDate, pageable);
			return ResponseEntity.ok(promotions);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PostMapping("/api-update-price")
	public ResponseEntity<?> checkAndUpdatePrice() {
	    try {
	        // Lấy chương trình khuyến mãi hiện tại
	        PromotionProgram promotionProgram = promotionProgramService.getPromotionProgramsCurrent();
	        if (promotionProgram == null) {
	            return ResponseEntity.status(400).body("Không có chương trình khuyến mãi hiện tại");
	        }

	        // Tính tỷ lệ khuyến mãi
	        int rate = promotionProgram.getPromotionValue();

	        // Lấy danh sách tour rẻ nhất và áp dụng khuyến mãi
	        List<Tours> listTour = toursRepository.listOfCheapestTours();
	        if (listTour.isEmpty()) {
	            return ResponseEntity.status(404).body("Không có tour nào để cập nhật");
	        }

	        // Tính toán giá khuyến mãi và lưu hàng loạt
	        listTour.forEach(tour -> tour.setPrice(calculateDiscountedPrice(tour.getOriginalPrice(), rate)));
	        toursRepository.saveAll(listTour); // Lưu toàn bộ danh sách

	        return ResponseEntity.ok("Cập nhật giá thành công");
	    } catch (Exception e) {
	        // Ghi log lỗi (nếu có logging framework)
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Lỗi khi cập nhật giá tour");
	    }
	}

	private float calculateDiscountedPrice(double originalPrice, int discountRate) {
		System.out.println(discountRate);
	    return (float) (originalPrice * ((100 - discountRate) / 100F));
	}


}
