package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Province;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long>{

}
