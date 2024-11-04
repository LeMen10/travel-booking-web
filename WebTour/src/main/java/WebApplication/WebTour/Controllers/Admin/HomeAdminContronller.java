package WebApplication.WebTour.Controllers.Admin;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import WebApplication.WebTour.Service.StatictisService;

@Controller
public class HomeAdminContronller {

	@Autowired
	StatictisService statictisService;
	
    @GetMapping("/admin")
    public String navigateAdminHomePage(Model model) {
        return "/Admin/home";
    }

    @GetMapping("/api-get-header-admin")
    public String getHeaderAdminHTML(Model model) {
    		return "/components/headerAdmin";
    }
    
    @GetMapping("/api-get-side-bar-admin")
    public String getSideAdminHTML(Model model) {
    		return "/components/sidebarAdmin";
    }
}
