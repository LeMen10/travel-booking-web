package WebApplication.WebTour.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Schedules;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.EditScheduleRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Service
public class EditScheduleService {
	@Autowired
	EditScheduleRepository editScheduleRepository;
	@Autowired
    ToursRepository toursRepository;
	
	public Schedules createSchedule(Schedules schedule) {
		
		Optional<Integer> countStep = editScheduleRepository.countStep(schedule.getTours().getTourId());
		if( countStep.isPresent()) {
			schedule.setStep(countStep.get() + 1);
		}
        return editScheduleRepository.save(schedule);
    }

    public boolean isStepExists(Long tourId, int step) {
        return editScheduleRepository.findByTourIdAndStep(tourId, step).isPresent();
    }

    public void updateSchedule(Long scheduleId, Long toutId, String activity, String location) {
        editScheduleRepository.updateSchedule(scheduleId,toutId, activity, location);
    }

    public void deleteSchedule(Long scheduleId, Long tourId) {
    	
    	editScheduleRepository.softDeleteSchedule(scheduleId, tourId);
        
        // Sắp xếp lại các step sau khi xóa
        reorderSteps(tourId);
    }
    public void reorderSteps(Long tourId) {
        //lấy tất cả các schedules có status = true theo tourId và sắp xếp theo step
        List<Schedules> schedules = editScheduleRepository.findAllByTourIdAndStatusTrueOrderByStep(tourId);
        
        // xếp lại step
        int newStep = 1;
        for (Schedules schedule : schedules) {
            schedule.setStep(newStep++);
        }
        
        // Lưu lại các thay đổi
        editScheduleRepository.saveAll(schedules);
    }
    
    
    public Tours getTourById(Long tourId) {
        return toursRepository.findById(tourId).orElse(null);
    }
}
