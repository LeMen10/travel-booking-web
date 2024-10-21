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
@Table(name = "promotiondetail")
@Data
public class Promotiondetail implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "promotion_detail_id")
    private Long promotionDetailId;

    @Column(name = "tourId")
    private int tourId;
    
    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "promotion_id", nullable = true)
    private Promotions promotions;
    
    @Column(name = "status")
    private boolean status = true;

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}


	public Promotions getPromotions() {
		return promotions;
	}

	public void setPromotions(Promotions promotions) {
		this.promotions = promotions;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		status = status;
	}

	public Long getPromotionDetailId() {
		return promotionDetailId;
	}
    
}
