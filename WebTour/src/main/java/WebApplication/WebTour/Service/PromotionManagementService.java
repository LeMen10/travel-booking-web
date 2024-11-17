package WebApplication.WebTour.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.Promotiondetail;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Respository.PromotiondetailRepository;

@Service
public class PromotionManagementService {

    @Autowired
    private PromotionsRepository promotionsRepository;
    
    @Autowired
    private PromotiondetailRepository promotionDetailRepository;

    public List<Promotions> getPromotionsActive() {
        return promotionsRepository.getPromotionsActive();
    }

    public ResponseEntity<Map<String, Object>> createPromotion(Promotions promotion) {
        Map<String, Object> response = new HashMap<>();
        
        boolean exists = promotionsRepository.existsByCode(promotion.getCode());
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

    public ResponseEntity<Map<String, Object>> createPromotionDetail(Promotiondetail promotionDetail) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer exists = promotionDetailRepository.existsByTourIdAndPromotion(promotionDetail.getTourId(), 
                    promotionDetail.getPromotions().getPromotionId());

            if (exists > 0) {
                response.put("status", 400);
                response.put("message", "Promotion detail for this tour and promotion already exists");
                return ResponseEntity.status(400).body(response);
            }

            Promotions promotion = promotionsRepository.findById(promotionDetail.getPromotions().getPromotionId()).get();
            promotionDetail.setPromotions(promotion);
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

    public ResponseEntity<Map<String, Object>> getPromotionsByDate(String startDateString, String endDateString) {
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
