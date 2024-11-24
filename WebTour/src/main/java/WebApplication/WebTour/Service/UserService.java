package WebApplication.WebTour.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import WebApplication.WebTour.Respository.UserRepository;

import java.util.List;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;
	
	public Page<Object[]> getUsersByRoleId(int roleId, Pageable pageable) {
        return userRepository.findByRoleId(roleId, pageable);
    }

    public List<Object[]> getUsersOrderedByBookings(String sort) {
        if (sort.equalsIgnoreCase("asc")) {
            return userRepository.findAllUsersOrderedByBookingsAsc();
        } else {
            return userRepository.findAllUsersOrderedByBookingsDesc();
        }
    }
}
