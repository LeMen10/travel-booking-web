package WebSpring.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    
    @Column(name = "promotion_id")
    private int promotionId;
    
    @Column(name = "status")
    private boolean Status;

	public int getTourId() {
		return tourId;
	}

	public void setTourId(int tourId) {
		this.tourId = tourId;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getPromotionDetailId() {
		return promotionDetailId;
	}
    
}
