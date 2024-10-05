package WebSpring.Model;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "schedules")
@Data
public class Schedules implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "tour_id")
    private int tourId;
    
    @Column(name = "activity")
    private String Activity;
    
    @Column(name = "step")
    private int Step;
    
    @Column(name = "location")
    private String Location;
       
    @Column(name = "status")
    private boolean Status;

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public String getActivity() {
		return Activity;
	}

	public void setActivity(String activity) {
		Activity = activity;
	}

	public int getStep() {
		return Step;
	}

	public void setStep(int step) {
		Step = step;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getScheduleId() {
		return scheduleId;
	}
    
}
