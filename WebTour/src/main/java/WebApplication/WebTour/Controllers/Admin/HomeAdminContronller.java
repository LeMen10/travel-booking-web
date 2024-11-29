package WebApplication.WebTour.Controllers.Admin;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.DTO.PaymentHistoryDTO;
import WebApplication.WebTour.Service.StatictisService;

@Controller
public class HomeAdminContronller {

	@Autowired
	StatictisService statictisService;
	
    @GetMapping("/admin")
    public String navigateAdminHomePage(Model model) {
    	Page<PaymentHistoryDTO> listHistoryMomo = statictisService.getHistoryPayment(3, null, null, 0, null, null, 0, 1, 0);
    	Page<PaymentHistoryDTO> listHistoryPaypal = statictisService.getHistoryPayment(4, null, null, 0, null, null,0,1, 0);
    	List<Object[]> listTopTour = statictisService.getTopTour(4, 0);
    	model.addAttribute("listMomo", listHistoryMomo);
    	model.addAttribute("listPaypal", listHistoryPaypal);
    	model.addAttribute("totalPagesMomo", listHistoryMomo.getTotalPages());
    	model.addAttribute("currentPageMomo", 0);
    	model.addAttribute("totalPagesPaypal", listHistoryPaypal.getTotalPages());
    	model.addAttribute("currentPagePaypal", 0);
    	model.addAttribute("listTopTour", listTopTour);
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
    
//    @GetMapping("/api-get-data-history")
//	public ResponseEntity<?> getSatisticTopTour(@RequestParam("top") int top,
//			@RequestParam("total") int total)
//	{
//		List<Map<String, Object>> statisticsList = statictisService.getTopTour(top, total);
//		if(statisticsList == null) return ResponseEntity.notFound().build();
//		return ResponseEntity.ok(statisticsList);
//	}
    
}
