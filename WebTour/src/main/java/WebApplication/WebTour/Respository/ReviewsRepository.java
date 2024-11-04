package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>{
	//lấy các review thuộc về tour
	@Query(value= "SELECT * FROM Reviews r WHERE r.tour_id = :tourId", nativeQuery = true)
    List<Reviews> findReviewsByTourId(@Param("tourId") Long tourId);
	
	@Query(value = "SELECT COALESCE(SUM(r.rate), 0), m.month, YEAR(CURDATE()) AS year "
			+ "FROM (SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION "
			+ " SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION "
			+ " SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12) AS m "
			+ "LEFT JOIN Reviews r ON m.month = MONTH(r.review_date) AND YEAR(r.review_date) = YEAR(CURDATE()) AND r.status = 1 "
			+ "LEFT JOIN Tours t ON r.tour_id = t.tour_id AND t.status = 1 "
			+ "WHERE m.month <= MONTH(CURDATE()) "
			+ "GROUP BY  m.month ORDER BY  m.month DESC LIMIT 7", nativeQuery = true)
	Optional<List<Object>> getSatisticsRateAllTourLast7Months();
}
