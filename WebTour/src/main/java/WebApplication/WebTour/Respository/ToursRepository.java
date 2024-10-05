package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Tours;

@Repository
public interface ToursRepository extends JpaRepository<Tours, Long>{

}
