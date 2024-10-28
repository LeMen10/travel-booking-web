package WebApplication.WebTour.Controllers.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import WebApplication.WebTour.Respository.SchedulesRepository;

@Controller
public class ScheduleManagementController {
	@Autowired
	SchedulesRepository schedulesRepository;
	
	
	@GetMapping("/admin/schedule_management")
	public String scheduleManagementPage( Model model) {
		
		return "/Admin/schedule_management";
	}
}
