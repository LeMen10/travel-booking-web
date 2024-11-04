package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Schedules;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Long>{
	//detailTour
	@Query(value = "SELECT * FROM Schedules s WHERE s.tour_id = :tourId AND status = true", nativeQuery = true)
    List<Schedules> findSchedulesByTourId(@Param("tourId") int tourId);

	//thực  hiện lấy schedule theo tourid ở trang edit schedule
	@Query(value = "SELECT * FROM Schedules s WHERE s.tour_id = :tourId AND status = true", nativeQuery = true)
    List<Schedules> getScheduleByTourId(@Param("tourId") Long tourId);
	
	
}
