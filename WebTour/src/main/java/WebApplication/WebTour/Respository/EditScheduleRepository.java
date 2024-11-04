package WebApplication.WebTour.Respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Schedules;

@Repository
public interface EditScheduleRepository extends JpaRepository<Schedules, Long> {
	@Query(value = "SELECT * FROM schedules WHERE tour_id = :tourId AND step = :step AND status = true", nativeQuery = true)
	Optional<Schedules> findByTourIdAndStep(@Param("tourId") Long tourId, @Param("step") int step);

	@Query(value = "SELECT count(step) FROM schedules WHERE tour_id = :tourId AND status = true ORDER BY step", nativeQuery = true)
	Optional<Integer> countStep(@Param("tourId") Long tourId);

	
	//sửa lịch trình
	@Transactional
	@Modifying
	@Query(value = "UPDATE schedules SET activity = :activity , location = :location WHERE schedule_id = :scheduleId  AND tour_id = :tourId", nativeQuery = true)
	void updateSchedule(@Param("scheduleId") Long scheduleId, 
			@Param("tourId") Long tourId,
			@Param("activity") String activity,
			@Param("location") String location);

	//xóa lịch trình
	@Transactional
	@Modifying
	@Query(value = "UPDATE schedules SET status = false WHERE schedule_id = :scheduleId AND tour_id = :tourId", nativeQuery = true)
	void softDeleteSchedule(@Param("scheduleId") Long scheduleId, @Param("tourId") Long tourId);

	//sắp xếp lại các step khi xóa lịch trình
	// Repository
	@Query(value = "SELECT * FROM schedules WHERE tour_id = :tourId AND status = true ORDER BY step", nativeQuery = true)
	List<Schedules> findAllByTourIdAndStatusTrueOrderByStep(@Param("tourId") Long tourId);


//	@Query(value = "SELECT s.step FROM schedules s WHERE schedule_id = :scheduleId AND tour_id = :tourId AND status = true", nativeQuery = true)
//	Integer findStepByScheduleIdAndTourId(@Param("scheduleId") Long scheduleId, @Param("tourId") Long tourId);

}
