package WebApplication.WebTour.Service;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.DTO.AccountUserDTO;
import WebApplication.WebTour.DTO.EmailRequest;
import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Model.Role;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.RoleRepository;
import WebApplication.WebTour.Respository.ToursRepository;
import WebApplication.WebTour.Respository.UserRepository;

@Service
public class HomeService {

	@Autowired
	AccountRespository accountRespository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProvinceRepository provinceRepository;
	
	@Autowired
	ToursRepository toursRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
    @Autowired
    private JavaMailSender mailSender;
    
    public Account getAccountByUserName(String username) {
        System.out.println(username);
        Optional<Account> account = accountRespository.findByUserName(username);
        
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }
    
    public Account getAccountBygoogleId(String googleId) {
        Optional<Account> account = accountRespository.findByGoogleId(googleId);
        
        if (account.isPresent()) {
            return account.get();
        }
        return null;
    }
    
    public Account addAccoutUserGoogle(String googleId, String userName, String email) {
    	
    	Account newAccount = new Account();
    	newAccount.setGoogleId(googleId);
    	newAccount.setStatus(true);
    	newAccount.setCreateOn(new Date(System.currentTimeMillis()));
    	User newUser = new User();
    	newUser.setEmail(email);
    	newUser.setRole(roleRepository.findById(3l).get());
    	newUser.setFullName(userName);
    	newAccount.setUser(newUser);
    	userRepository.save(newUser);
    	Account accountCreated = accountRespository.save(newAccount);
    	return accountCreated;
    
    }
    public Account addAccoutUser(AccountUserDTO userDTO) {
        
    	System.out.println(userDTO);
        if (userDTO != null) {
        	Role role = roleRepository.findById(3l).get();
        	if(role == null) return null;
        	Date currentDate = new Date(System.currentTimeMillis());
        	User user = new User(role, userDTO.getFullName(), userDTO.getEmail(),userDTO.getPhone(), userDTO.getGender(), null);
        	User userCreated = userRepository.save(user);
        	Account newAccount = new Account(userCreated, userDTO.getUserName(), userDTO.getPassword(), currentDate);
        	Account accountCreated = accountRespository.save(newAccount);
            return accountCreated;
        } else {
            return null;
        }
    }
    
    public boolean updateNewPassword(Long accountId, String newPassword)
    {
    	Optional<Account> account = accountRespository.findById(accountId);
    	if(account.isPresent())
    	{
    		try {
    		Account accountDTO = account.get();
    		accountDTO.setPassword(newPassword);
    		accountRespository.save(accountDTO);
    		return true;
    		}
    		catch(Exception exception)
    		{
    			System.out.println(exception);
    		}
    	}
    	return false;
    }
    
    public boolean sendEmail(EmailRequest emailRequest) {
    	try {
    		SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(emailRequest.getTo());
	        message.setSubject(emailRequest.getSubject());
	        message.setText(emailRequest.getBody());
	        mailSender.send(message);
        return true;
    	}catch(Exception ex)
    	{
    		return false;
    	}
        
    }
}
