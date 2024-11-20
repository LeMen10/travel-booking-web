package WebApplication.WebTour.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import WebApplication.WebTour.Model.Tours;
import WebApplication.WebTour.Respository.ToursRepository;

@Service
public class TourService {
	@Autowired
    private ToursRepository toursRepository;

    //hiển thị tour trang schedule management (admin) có phân trang
    public Page<Tours> getToursByPage(Pageable pageable) {
        return toursRepository.ShowTour(pageable);
    }
    
    //tìm kiếm tên tour có phân trang
    public Page<Tours> searchTourName(String tourName, Pageable pageable){
    	return toursRepository.searchTourName(tourName,pageable);
    }
    
    //hiển thị thông tin lên trang edit lịch trình với id của tour
    public Tours getTourById(Long tourId) {
        Optional<Tours> tour = toursRepository.findById(tourId);
        return tour.orElse(null);  
    }
}
