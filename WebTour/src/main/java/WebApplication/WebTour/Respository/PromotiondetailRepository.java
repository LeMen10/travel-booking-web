package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.Bookings;
import WebApplication.WebTour.Model.Payments;
import WebApplication.WebTour.Model.Promotiondetail;

@Repository
public interface PromotiondetailRepository extends JpaRepository<Promotiondetail, Long> {

	@Transactional
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM Promotiondetail pd "
	             + "WHERE pd.tour_id = :tourId AND pd.promotion_id = :promotionId AND pd.status = true", nativeQuery = true)
	Integer existsByTourIdAndPromotion(@Param("tourId") int tourId, @Param("promotionId") Long promotionId);


	// lấy promotion detail theo tour và id

	
//	  @Query(value = "SELECT * FROM Promotiondetail   WHERE tour_id = :tourId " +
//	  "AND promotions.code = :name AND status = true", nativeQuery = true)
//	  Optional<Promotiondetail>
//	  getPromotionByTourIdAndPromotionName(@Param("tourId") int tourId, @Param("name") String name);

	@Query(value = "SELECT pd.* FROM promotiondetail pd " +
            "JOIN promotions p ON pd.promotion_id = p.promotion_id " +
            "WHERE pd.tour_id = :tourId AND p.code = :name AND pd.status = true", nativeQuery = true)
Optional<Promotiondetail> getPromotionByTourIdAndPromotionName(@Param("tourId") int tourId, @Param("name") String name);

}
