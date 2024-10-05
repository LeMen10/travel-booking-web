package WebApplication.WebTour.Respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import WebApplication.WebTour.Model.Employees;

@Repository
public interface EmployeesRespository extends JpaRepository<Employees, Long>{
	
}
