package WebApplication.WebTour.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "discount")
@Data
public class Discount {
	
	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "discount_id ")
    private Long discountId ;

    @Column(name = "user_id")
    private int userId;
    
    @Column(name = "point")
    private int point;
    
    @Column(name="reward")
    private String reward;
    
    @Column(name="status")
    private boolean status;

	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		status = status;
	}

	public Long getDiscountId() {
		return discountId;
	}
}
