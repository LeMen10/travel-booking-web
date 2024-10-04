package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.District;

@Repository
public interface DistrictRespository extends JpaRepository<District, Long>{

}
