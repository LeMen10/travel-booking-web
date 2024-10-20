package WebApplication.WebTour.Respository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import WebApplication.WebTour.Model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Transactional
	@Query(value = "SELECT u FROM User WHERE User.role.roleId = :roleId", nativeQuery = true)
    List<User> findByRoleId(@Param("roleId") int roleId);
	
}