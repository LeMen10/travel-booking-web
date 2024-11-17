package WebApplication.WebTour.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.PromotionProgram;
import WebApplication.WebTour.Respository.PromotionProgramRepository;

@Service
public class PromotionProgramService {

	@Autowired
    private PromotionProgramRepository promotionProgramRepository;

    public PromotionProgram createPromotion(PromotionProgram promotion) {
        return promotionProgramRepository.save(promotion);
    }
    
    public List<PromotionProgram> getPromotionPrograms() {
        return promotionProgramRepository.getPromotionProgramsActive();
    }
}
