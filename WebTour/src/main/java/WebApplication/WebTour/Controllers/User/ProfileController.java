package WebApplication.WebTour.Controllers.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import WebApplication.WebTour.Model.Account;
import WebApplication.WebTour.Model.Address;
import WebApplication.WebTour.Model.District;
import WebApplication.WebTour.Model.Province;
import WebApplication.WebTour.Model.Ticket;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Model.User;
import WebApplication.WebTour.Model.Ward;
import WebApplication.WebTour.Respository.AccountRespository;
import WebApplication.WebTour.Respository.DistrictRespository;
import WebApplication.WebTour.Respository.ProvinceRepository;
import WebApplication.WebTour.Respository.UserRepository;
import WebApplication.WebTour.Respository.WardRepository;
import jakarta.servlet.http.HttpSession;


@Controller("sss")
public class ProfileController {
	
	@Autowired
	AccountRespository accountRespository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProvinceRepository provinceRepository;

	@Autowired
	private DistrictRespository districtRepository;
	
	@Autowired
	private WardRepository wardRepository;
	
	
	
	@GetMapping("/account")
	public String showPageProfile( @RequestParam(value = "userId", required = false) Long userId,Model model) {
		
		Optional<User> user = userRepository.findById(userId);
		Address address = user.get().getAddress();
		Province provinceUser = address.getProvince();
		Ward wardUser = address.getWard();
		District districtUser = address.getDistrict();
		List<Province> province = provinceRepository.findAll();
		List<District> district = districtRepository.findAll();
		List<Ward> ward = wardRepository.findAll();
		model.addAttribute("province",province);
		model.addAttribute("district",district);
		model.addAttribute("ward",ward);
		
		model.addAttribute("user", user.get());
		model.addAttribute("address", address);
		model.addAttribute("provinceUser", provinceUser);
		model.addAttribute("wardUser", wardUser);
		model.addAttribute("districtUser", districtUser);
		
		
        return "/User/profile";
	}
	
	@GetMapping("/account/change-password")
	public String showPageChangePassword(Model model) {
		return "/User/change-password";
	}
	
	@GetMapping("/api-get-user") // để sử dụng cho dấu cộng, trừ số lượng vé (trẻ em hay người lớn)
	public ResponseEntity<User> GetUser(@RequestParam("userId") Long userId, Model model) {
		Optional<User> user = userRepository.findById(userId);
		// Kiểm tra nếu có giá trị trong Optional
		
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());		
	}
	@GetMapping("/api-get-account")
	public ResponseEntity<Account> GetAcount(@RequestParam("userId") Long userId, Model model) {
		Optional<Account> account = accountRespository.findById(userId);
		// Kiểm tra nếu có giá trị trong Optional
		
		return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());		
	}
	@GetMapping("/districts-by-province")
	@ResponseBody
	public List<District> getDistrictsByProvince(@RequestParam("provinceId") int provinceId) {
	    return districtRepository.findByProvinceId(provinceId);
	}

	@GetMapping("/wards-by-district")
	@ResponseBody
	public List<Ward> getWardsByDistrict(@RequestParam("districtId") int districtId) {
	    return wardRepository.findByDistrictId(districtId);
	}
	
	@PutMapping("/api-update-user")
	public ResponseEntity<?> updateTotalPrice(@RequestParam("userId") Long userId,
			@RequestParam("fullName") String fullName,
			@RequestParam("gender") String gender,
			@RequestParam("email") String email,
			@RequestParam("phone") String phone,
			@RequestParam("address") String addressDetail,
			@RequestParam("provinceId") Province provinceId,
			@RequestParam("districtId") District districtId,
			@RequestParam("wardId") Ward wardId) 
	{	
		try {
			Optional<User> user = userRepository.findById(userId);
			
			user.get().setFullName(fullName);
	        user.get().setGender(gender);
	        user.get().setEmail(email);
	        user.get().setPhone(phone);

	        // Cập nhật địa chỉ
	        Address address = user.get().getAddress();
	        address.setDetail(addressDetail);
	        address.setProvince(provinceId);
	        address.setDistrict(districtId);
	        address.setWard(wardId);	

	        // Lưu thông tin cập nhật vào database
	        userRepository.save(user.get());
			
			
			return ResponseEntity.ok("Cập nhật User thành công");
			
		} catch (Exception e) {
			e.printStackTrace(); // In lỗi ra console để dễ debug
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi cập nhật: " + e.getMessage());
		}
	}
	@PostMapping("/update-password")
	public ResponseEntity<?> updatePassword(@RequestParam Long userId, @RequestBody Map<String, String> requestBody) {
        // Tìm tài khoản dựa trên userId
        Account account = accountRespository.findById(userId).get();
        
        // Kiểm tra xem tài khoản có tồn tại không
        if (account == null) {
            return ResponseEntity.notFound().build(); // Trả về mã 404 nếu không tìm thấy
        }

        // Lấy mật khẩu mới từ request body
        String newPassword = requestBody.get("password");
        System.out.println("New password: "+ newPassword);
        // Cập nhật mật khẩu mới cho tài khoản	
        account.setPassword(newPassword);
        accountRespository.save(account); // Lưu tài khoản với mật khẩu mới
        
        return ResponseEntity.ok("Cập nhật mật khẩu thành công!"); // Trả về thông báo thành công
    }
}
