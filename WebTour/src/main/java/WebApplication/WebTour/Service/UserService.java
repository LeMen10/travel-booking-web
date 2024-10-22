package WebApplication.WebTour.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Respository.UserRepository;

import java.util.List;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;

    public List<Object[]> getUsersOrderedByBookings(String sort) {
        if (sort.equalsIgnoreCase("asc")) {
            return userRepository.findAllUsersOrderedByBookingsAsc();
        } else {
            return userRepository.findAllUsersOrderedByBookingsDesc();
        }
    }
}
