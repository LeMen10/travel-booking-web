package WebApplication.WebTour.Respository;

import java.util.List;

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
}
