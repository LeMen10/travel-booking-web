package WebApplication.WebTour.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Model.Promotiondetail;
import WebApplication.WebTour.Respository.PromotionsRepository;
import WebApplication.WebTour.Respository.PromotiondetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
public class PromotionManagementService {

    @Autowired
    private PromotionsRepository promotionsRepository;
    
    @Autowired
    private PromotiondetailRepository promotionDetailRepository;

    public Page<Object[]> getPromotionsActive(Pageable pageable) {
        return promotionsRepository.getPromotionsActive(pageable);
    }

    public boolean existsByCode(String code) {
        return promotionsRepository.existsByCode(code);
    }

    public void savePromotion(Promotions promotion) {
        promotionsRepository.save(promotion);
    }

    public boolean existsByTourIdAndPromotion(int tourId, Long promotionId) {
        return promotionDetailRepository.existsByTourIdAndPromotion(tourId, promotionId) > 0;
    }

    public Promotions findPromotionById(Long promotionId) {
        return promotionsRepository.findById(promotionId)
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + promotionId));
    }

    public void savePromotionDetail(Promotiondetail promotionDetail) {
        promotionDetailRepository.save(promotionDetail);
    }

    public List<Promotions> getPromotionsByDateRange(Date startDate, Date endDate) {
        return promotionsRepository.findPromotionsByDateRange(startDate, endDate);
    }
}
