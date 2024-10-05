package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>{

}
