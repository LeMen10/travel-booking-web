package WebApplication.WebTour.Service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Page<Object[]> getPromotionsByDateRange(Date startDate, Date endDate, Pageable pageable) {
        return promotionsRepository.findPromotionsByDateRange(startDate, endDate, pageable);
    }
    
    public Optional<Promotions> getPromotionById(Long id) {
        return promotionsRepository.findById(id);
    }
    
    public Optional<Promotions> findByCode(String code) {
        return promotionsRepository.findByCode(code);
    }
    
    public void deletePromotion(Long id) {
        promotionsRepository.deleteById(id);
    }
    
    public Optional<Promotions> findById(Long id) {
        return promotionsRepository.findById(id);
    }
}
