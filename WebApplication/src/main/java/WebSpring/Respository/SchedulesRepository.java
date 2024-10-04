package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Schedules;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Long>{

}
