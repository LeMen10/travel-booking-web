package WebApplication.WebTour.Respository;

import org.springframework.stereotype.Repository;
import WebApplication.WebTour.Model.Point;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
	
	@Query("SELECT p FROM Point p WHERE p.userId = :userId")
	List<Point> findByUserId(@Param("userId") Long userId);
    
}
