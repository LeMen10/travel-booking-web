package WebApplication.WebTour.Model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tour_id", nullable = true)
    private Tours Tours;
	
    @Column(name = "activity")
    private String activity;
    
    @Column(name = "step")
    private int step;
    
    @Column(name = "location")
    private String location;
       
    @Column(name = "status")
    private boolean status;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Long getScheduleId() {
		return scheduleId;
	}
    
}
