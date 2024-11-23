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

	@Query("SELECT t.tourId, t.departure, t.tourName, t.price, t.endDate, t.startDate, i.imageId, t.originalPrice"
			+ " FROM Tours t JOIN Image i ON i.tours.tourId = t.tourId WHERE "
			+ "(:tourName IS NULL OR LOWER(t.tourName) LIKE LOWER(CONCAT('%', :tourName, '%'))) "
			+ "AND (:startDate IS NULL OR t.startDate = :startDate) "
			+ "AND (:departure IS NULL OR LOWER(t.departure) LIKE LOWER(CONCAT('%', :departure, '%'))) "
			+ "AND (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%'))) "
			+ "AND t.status = true " 
			//+ "AND t.originalId IS NULL " 
			+ "AND i.isBackground = true " + "GROUP BY t.tourId")
	Page<Object[]> findTours(@Param("tourName") String tourName, @Param("startDate") Date startDate,
			@Param("departure") String departure, @Param("destination") String destination, Pageable pageable);

	// Get tour for home page
	@Query(value = "SELECT t FROM Tours t WHERE t.status = true ORDER BY t.price ASC LIMIT 8")
	List<Tours> listOfCheapestTours();

	// lấy ra những tour chưa bắt đầu và những tour đó chưa được áp mã đang xem xét
	// áp dụng
	@Query("SELECT t FROM Tours t WHERE t.startDate > CURRENT_DATE AND t.originalId IS NULL AND t.tourId NOT IN  "
			+ "(SELECT pd.tourId FROM Promotiondetail pd WHERE pd.promotions.promotionId = :promotionId)")
	List<Tours> getToursAboutToBegin(@Param("promotionId") long promotionId);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM tours\r\n" + "ORDER BY tours.tour_id ASC", nativeQuery = true)
	List<Object[]> getTours();

	// hiện tour với phân trang ở trang schedule-management
	@Transactional
	@Query(value = "SELECT * FROM tours t WHERE t.original_id IS NULL  AND status = true", nativeQuery = true)
	Page<Tours> ShowTour(Pageable pageable);

	// tìm kiếm tên tour với phân trang ở trang schedule-management
	@Transactional
	@Query(value = "SELECT * FROM tours t WHERE t.tour_name LIKE CONCAT('%', :tourName, '%') AND t.original_id IS NULL AND status = true", nativeQuery = true)
	Page<Tours> searchTourName(@Param("tourName") String tourName, Pageable pageable);

	@Query("SELECT t FROM Tours t WHERE t.originalId IS NULL AND t.status = true")
	Page<Tours> findAllByOriginalIdIsNull(Pageable pageable);

	@Query("SELECT t FROM Tours t WHERE t.originalId IS NULL AND t.status = true")
	List<Tours> findAllByOriginalIdIsNullNoPage();

	@Transactional
	@Modifying
	@Query("UPDATE Tours t SET t.status = false WHERE t.tourId = :tourId")
	void updateStatusByTourId(@Param("tourId") Long tourId);

	// sắp xếp giá  tour tăng
	@Query(value = "SELECT t.tour_id, t.departure, t.tour_name, t.price, t.end_date, t.start_date, i.image_id "
	        + "FROM tours t "
	        + "LEFT JOIN image i ON t.tour_id = i.tour_id "
	        + "WHERE t.status = true AND t.original_id IS NULL "
	        + "GROUP BY t.tour_id, t.departure, t.tour_name, t.price, t.end_date, t.start_date, t.original_id "
	        + "ORDER BY t.price ASC", nativeQuery = true)
	Page<Object[]> findToursSortedByPriceAsc(Pageable pageable);
	// sắp xếp giá  tour  giảm
	@Query(value = "SELECT t.tour_id, t.departure, t.tour_name, t.price, t.end_date, t.start_date, i.image_id "
	        + "FROM tours t "
	        + "LEFT JOIN image i ON t.tour_id = i.tour_id "
	        + "WHERE t.status = true AND t.original_id IS NULL "
	        + "GROUP BY t.tour_id, t.departure, t.tour_name, t.price, t.end_date, t.start_date, t.original_id "
	        + "ORDER BY t.price DESC", nativeQuery = true)
	Page<Object[]> findToursSortedByPriceDesc(Pageable pageable);

}
