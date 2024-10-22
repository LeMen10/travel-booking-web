package WebApplication.WebTour.Model;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "reviews")
@Data
public class Reviews implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reviews_id")
    private Long reviewsId;

	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
    
	@ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tour_id", nullable = true)
    private Tours tours;
    
    
    @Column(name = "comment")
    private String comment;
    
    @Column(name = "rate")
    private int rate;
    
    @Column(name = "review_date")
    private Date reviewDate;
    
    @Column(name = "status")
    private boolean status;

	public Long getReviewsId() {
		return reviewsId;
	}

	public void setReviewsId(Long reviewsId) {
		this.reviewsId = reviewsId;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Tours getTours() {
		return tours;
	}

	public void setTours(Tours tours) {
		this.tours = tours;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(java.util.Date reviewDate2) {
		this.reviewDate = reviewDate2;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	
    
}
