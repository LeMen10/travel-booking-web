package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Account;

@Repository
public interface AccountRespository extends JpaRepository<Account, Long>{
	Account findByUserNameAndPassword(String userName, String Password);
}