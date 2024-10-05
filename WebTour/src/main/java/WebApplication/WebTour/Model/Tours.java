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
@Table(name = "tours")
@Data
public class Tours implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tour_id")
    private Long tourId;

    @Column(name = "guided_id")
    private int guidedId;
    
    @Column(name = "tour_name")
    private String tourName;
    
    @Column(name = "destination")
    private String Destination;
    
    @Column(name = "detail")
    private String Detail;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Column(name = "price")
    private float Price;
    
    @Column(name = "people_max")
    private int peopleMax;
    
    @Column(name = "status")
    private boolean Status;

	public int getGuidedId() {
		return guidedId;
	}

	public void setGuidedId(int guidedId) {
		this.guidedId = guidedId;
	}

	public String getTourName() {
		return tourName;
	}

	public void setTourName(String tourName) {
		this.tourName = tourName;
	}

	public String getDestination() {
		return Destination;
	}

	public void setDestination(String destination) {
		Destination = destination;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEnd_Date() {
		return endDate;
	}

	public void setEnd_Date(Date end_Date) {
		this.endDate = end_Date;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public int getPeopleMax() {
		return peopleMax;
	}

	public void setPeopleMax(int peopleMax) {
		this.peopleMax = peopleMax;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getTourId() {
		return tourId;
	}
    
}
