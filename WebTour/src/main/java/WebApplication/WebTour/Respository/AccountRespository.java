package WebApplication.WebTour.Respository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Account;

@Repository
public interface AccountRespository extends JpaRepository<Account, Long>{
	@Query("SELECT a FROM Account a WHERE a.userName = :name")
    Optional<Account> findByUserName(@Param("name") String name);
}