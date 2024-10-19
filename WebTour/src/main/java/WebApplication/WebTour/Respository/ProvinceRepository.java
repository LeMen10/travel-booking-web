package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long>{
	
}
