package AndreaBarocchi.CapstoneProject.payloads;


import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationPayload {
	@Size(min = 3, max = 20, message = "Username must be at most 20 characters long")
    private String username;
	
	@Size(min = 3, max = 20, message = "Name must be at most 20 characters long")
    @NotEmpty(message = "Name is required")
    private String firstname;

	@Size(min = 3, max = 20, message = "Surname must be at most 20 characters long")
    @NotEmpty(message = "Surname is required")
    private String lastname;
	
	
    @Email(message = "Insert a valid email format")
    private String email;
    
//	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\bd)(?=.*[@$!%*#?&])[A-Za-z\bd@$!%*#?&]{8,}$", 
//			message = "Password must be at least 8 characters long and contain at least one digit, one letter, and one special character")
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;
	
	private List<ArticlePayload> articles;
}
