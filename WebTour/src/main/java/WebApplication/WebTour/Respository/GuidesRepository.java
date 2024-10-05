package WebSpring.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebSpring.Model.Guides;

@Repository
public interface GuidesRepository extends JpaRepository<Guides, Long>{

}
