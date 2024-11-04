package WebApplication.WebTour.Respository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.Tours;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Long> {

	/*
	 * @Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))"
	 * ) List<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String
	 * tourName);
	 */

//	@Query("SELECT t FROM Tours t WHERE LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))")
//	Page<Tours> findByTourNameContainingIgnoreCase(@Param("tourName") String tourName, Pageable pageable);

	@Query("SELECT t FROM Tours t WHERE "
	        + "(:tourName IS NULL OR LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))) "
	        + "AND (:startDate IS NULL OR t.startDate = :startDate) "
	        + "AND (:departure IS NULL OR LOWER(t.departure) LIKE LOWER(CONCAT('%', :departure, '%'))) "
	        + "AND (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%')))")
	Page<Tours> findTours(@Param("tourName") String tourName,
	                      @Param("startDate") Date startDate,
	                      @Param("departure") String departure,
	                      @Param("destination") String destination,
	                      Pageable pageable);
	
	//Get tour for home page
    @Query(value = "SELECT t FROM Tours t WHERE t.status = true ORDER BY t.price ASC LIMIT 8")
	List<Tours> listOfCheapestTours();

	// lấy ra những tour chưa bắt đầu và những tour đó chưa được áp mã đang xem xét áp dụng
	@Query("SELECT t FROM Tours t WHERE t.startDate > CURRENT_DATE AND t.tourId NOT IN "
			+ "(SELECT pd.tourId FROM Promotiondetail pd WHERE pd.promotions.promotionId = :promotionId)")
	List<Tours> getToursAboutToBegin(@Param("promotionId") long promotionId);

	//tìm kiếm tên tour với phân trang
	@Transactional
	@Query(value = "SELECT * FROM tours t WHERE t.tour_name LIKE CONCAT('%', :tourName, '%') AND status = true", nativeQuery = true)
	Page<Tours> searchTourName(@Param("tourName") String tourName, Pageable pageable);
	

}
