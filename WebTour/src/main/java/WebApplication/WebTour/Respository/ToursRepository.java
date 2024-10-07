package WebApplication.WebTour.Respository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Tours;


@Repository
public interface ToursRepository extends JpaRepository<Tours, Long>{

	
}
