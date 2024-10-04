package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Reviews;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long>{

}
