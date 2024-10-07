package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Tours;


@Repository
public interface ToursRepository extends JpaRepository<Tours, Long>{

}
