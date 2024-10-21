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

	/* boolean existsByTourIdAndPromotionId(int tourId, Long Long); */
	@Transactional
	@Modifying
	@Query(value = "SELECT pd.* FROM Promotiondetail pd JOIN Promotions p ON pd.promotion_id = p.promotion_id WHERE pd.tourId = :tourId "
			+ "AND pd.promotions.promotionId = :promotionId AND pd.status = true", nativeQuery = true)
	boolean existsByTourIdAndPromotionId(@Param("tourId") int tourId,@Param("promotionId") Long promotionId);

	// lấy promotion detail theo tour và id
	@Transactional
	@Modifying
	@Query(value = "SELECT pd.* FROM Promotiondetail pd JOIN Promotions p ON pd.promotion_id = p.promotion_id WHERE pd.tourId = :tourId "
			+ "AND p.name = :name AND pd.status = true", nativeQuery = true)
	Optional<Promotiondetail> getPromotionByTourIdAndPromotionName(@Param("tourId") Long tourId,
			@Param("name") String name);

}
