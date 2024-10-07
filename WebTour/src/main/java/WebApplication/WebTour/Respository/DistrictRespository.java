package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.District;

@Repository
public interface DistrictRespository extends JpaRepository<District, Long>{

}