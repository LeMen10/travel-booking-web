package WebApplication.WebTour.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.PromotionProgram;
import WebApplication.WebTour.Respository.PromotionProgramRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Service
public class PromotionProgramService {

	@Autowired
    private PromotionProgramRepository promotionProgramRepository;
	@Autowired
	private ToursRepository toursRepository;

    public PromotionProgram createPromotion(PromotionProgram promotion) {
        return promotionProgramRepository.save(promotion);
    }
    
    public Page<Object[]> getPromotionPrograms(Pageable pageable) {
        return promotionProgramRepository.getPromotionProgramsActive(pageable);
    }
    
    public Page<Object[]> getPromotionsByDateRange(Date startDate, Date endDate, Pageable pageable) {
        return promotionProgramRepository.findPromotionProgramByDateRange(startDate, endDate, pageable);
    }
    
    public PromotionProgram getPromotionProgramsCurrent()
    {
    	Optional<PromotionProgram> promotionProgram = promotionProgramRepository.getPromotionProgramsCurrent();
    	if(promotionProgram.isPresent())
    	{
    		return promotionProgram.get();
    	}
    	return null;
    }
}
