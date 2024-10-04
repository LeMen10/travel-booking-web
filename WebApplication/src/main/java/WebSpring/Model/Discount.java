package WebSpring.Model;

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
	
	private static final long serialVersionUID = 1L;

	@Id
 	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "discount_id ")
    private Long discountId ;

    @Column(name = "customer_id")
    private int customerId;
    
    @Column(name = "point")
    private int Point;
    
    @Column(name="reward")
    private String Reward;
    
    @Column(name="status")
    private boolean Status;

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getPoint() {
		return Point;
	}

	public void setPoint(int point) {
		Point = point;
	}

	public String getReward() {
		return Reward;
	}

	public void setReward(String reward) {
		Reward = reward;
	}

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public Long getDiscountId() {
		return discountId;
	}
}
