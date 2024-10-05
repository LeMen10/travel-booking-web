package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Schedules;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Long>{

}
