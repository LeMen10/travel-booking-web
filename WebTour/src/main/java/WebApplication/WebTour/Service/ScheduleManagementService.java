package WebApplication.WebTour.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Schedules;
import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.SchedulesRepository;
import WebApplication.WebTour.Respository.ToursRepository;

@Service
public class ScheduleManagementService {
	@Autowired
	private ToursRepository toursRepository;
	@Autowired
	SchedulesRepository schedulesRepository;

	
	public List<Tours> getAllTours() {
		return toursRepository.findAll();
	}

	public List<Schedules> getScheduleByTourId(Long tourId) {
		return schedulesRepository.getScheduleByTourId(tourId);
	}
}
