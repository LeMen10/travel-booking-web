package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;

import WebApplication.WebTour.Model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
