package WebSpring.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import WebSpring.Model.Account;
import WebSpring.Respository.AccountRespository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class AuthController {
	
	@Autowired
    private AccountRespository accountRepository;

    // Hiển thị trang đăng nhập
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpServletResponse response) {
        Account account = accountRepository.findByUserNameAndPassword(username, password);
        System.out.println(account.getUserName()+account.getPassword());
        if (account != null) {
            // Tạo cookie lưu tên người dùng
            Cookie cookie = new Cookie("userName", account.getUserName());
            
            cookie.setMaxAge(60 * 60); // 1 giờ
            response.addCookie(cookie);
            // Chuyển hướng đến trang home
            return "home";
        } else {
            // Thông báo đăng nhập thất bại
        	model.addAttribute("error", "Tài khoản đã tồn tại!");
            return "login";
        }
    }

    // Hiển thị trang home
    @GetMapping("/home")
    public String showHomePage(@CookieValue(value = "userName", defaultValue = "") String userName, Model model) {
        if (userName.isEmpty()) {
            return "login";
        }
        model.addAttribute("username", userName);
        return "home";
    }
}
