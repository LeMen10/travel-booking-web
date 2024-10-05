package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Guides;

@Repository
public interface GuidesRepository extends JpaRepository<Guides, Long>{

}
