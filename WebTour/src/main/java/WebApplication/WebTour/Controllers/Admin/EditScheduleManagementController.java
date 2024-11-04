package WebApplication.WebTour.Controllers.Admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import WebApplication.WebTour.Model.Schedules;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Service.EditScheduleService;

@Controller
public class EditScheduleManagementController {
	@Autowired
	EditScheduleService editScheduleService;

	@PostMapping("/create-schedule")
    public ResponseEntity<?> createSchedule(
        @RequestParam Long tourId,
        @RequestParam int step,
        @RequestParam String activity,
        @RequestParam String location) {
        
        if (editScheduleService.isStepExists(tourId, step)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Step đã tồn tại cho Tour này");
        }
        Tours tour = editScheduleService.getTourById(tourId);
        if (tour == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tour không tồn tại");
        }
        Schedules newSchedule = new Schedules();
        newSchedule.setTours(tour); 
        newSchedule.setStep(step);
        newSchedule.setActivity(activity);
        newSchedule.setLocation(location);
        newSchedule.setStatus(true);
        
        Schedules createdSchedule = editScheduleService.createSchedule(newSchedule);
        return ResponseEntity.ok(createdSchedule);
    }

    @PutMapping("/update-schedule/{scheduleId}")
    public ResponseEntity<?> updateSchedule(
        @PathVariable("scheduleId") Long scheduleId,
        @RequestParam("tourId") Long tourId,
        @RequestParam String activity,
        @RequestParam String location) {
        
        editScheduleService.updateSchedule(scheduleId, tourId,activity, location);
        return ResponseEntity.ok("Lịch trình đã được cập nhật");
    }

    @DeleteMapping("/delete-schedule/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable("scheduleId") Long scheduleId,
    		@RequestParam("tourId") Long tourId) {
        editScheduleService.deleteSchedule(scheduleId,tourId);
        return ResponseEntity.ok("Lịch trình đã được xóa");
    }
}
