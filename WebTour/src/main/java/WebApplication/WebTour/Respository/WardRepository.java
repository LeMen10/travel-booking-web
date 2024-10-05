package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Ward;


@Repository
public interface WardRepository extends JpaRepository<Ward, Long>{

}
