package AndreaBarocchi.CapstoneProject.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserLoginPayload {
	@Email(message = "Insert a valid email format")
	private String email;
	
	@Pattern(regexp = "^(?=.[0-9])(?=.[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$" , 
			message = "Password must be at least 8 characters long and contain at least one digit, one letter, and one special character")
	private String password;
}
