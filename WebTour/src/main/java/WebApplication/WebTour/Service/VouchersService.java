package WebApplication.WebTour.Service;

import org.springframework.data.domain.Page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Point;
import WebApplication.WebTour.Model.Promotions;
import WebApplication.WebTour.Respository.PointRepository;
import WebApplication.WebTour.Respository.PromotionsRepository;

import org.springframework.data.domain.Pageable;

@Service
public class VouchersService {

	@Autowired
    private PromotionsRepository promotionsRepository;
	@Autowired
    private PointRepository pointRepository;
    
    public Page<Promotions> showDataTable(Long userId, Pageable pageable) {
    	List<Point> points = pointRepository.findByUserId(userId);
    	int userPoints = points.stream().mapToInt(Point::getPoint).sum();
        return promotionsRepository.findPromotionsByUserIdAndPoints(userPoints, userId, pageable);
    }	
}
