package WebApplication.WebTour.Model;

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
    
    @Column(name = "transport")
    private String transport;
    
    @Column(name = "departure")
    private String departure;
    
    @Column(name = "destination")
    private String destination;
    
    
    @Column(name = "detail")
    private String detail;
    
    @Column(name = "start_date")
    private Date startDate;
    
    @Column(name = "end_date")
    private Date endDate;
    
    @Column(name = "price")
    private float price;
    
    @Column(name = "people_max")
    private int peopleMax;
    
    @Column(name = "status")
    private boolean status;

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

	

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		detail = detail;
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
		return price;
	}

	public void setPrice(float price) {
		price = price;
	}

	public int getPeopleMax() {
		return peopleMax;
	}

	public void setPeopleMax(int peopleMax) {
		this.peopleMax = peopleMax;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		status = status;
	}

	public Long getTourId() {
		return tourId;
	}
    
}
