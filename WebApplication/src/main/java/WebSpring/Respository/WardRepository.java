package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Ward;

@Repository
public interface WardRepository extends JpaRepository<Ward, Long>{

}
