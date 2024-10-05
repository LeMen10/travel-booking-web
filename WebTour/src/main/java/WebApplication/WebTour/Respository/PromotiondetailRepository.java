package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Promotiondetail;

@Repository
public interface PromotiondetailRepository extends JpaRepository<Promotiondetail, Long>{
	
}
