package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Promotiondetail;

@Repository
public interface PromotiondetailRepository extends JpaRepository<Promotiondetail, Long>{
	
}
