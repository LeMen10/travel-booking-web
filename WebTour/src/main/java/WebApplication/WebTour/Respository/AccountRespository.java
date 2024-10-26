package WebApplication.WebTour.Respository;

import java.sql.Date;
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
	
//	@Query("SELECT COUNT(accountId) FROM Account WHERE createOn BETWEEN :startDate AND :finishDate")
//	Integer countUserByCreateOn(@Param("startDate") Date startDate, @Param("finishDate") Date finishDate);
}