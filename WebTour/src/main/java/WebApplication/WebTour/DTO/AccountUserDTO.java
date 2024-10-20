package WebApplication.WebTour.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountUserDTO {
	private String userName;
    private String fullName;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Password")
    private String password;

    public AccountUserDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	@Override
	public String toString() {
		return "AccountUserDTO [userName=" + userName + ", fullName=" + fullName + ", email=" + email + ", phone="
				+ phone + ", gender=" + gender + ", password=" + password + "]";
	}
    
}

