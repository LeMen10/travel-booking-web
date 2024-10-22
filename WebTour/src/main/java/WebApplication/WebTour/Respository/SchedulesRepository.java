package WebApplication.WebTour.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Schedules;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Long>{
	@Query(value = "SELECT * FROM Schedules s WHERE s.tour_id = :tourId", nativeQuery = true)
    List<Schedules> findSchedulesByTourId(@Param("tourId") int tourId);
}
