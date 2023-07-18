package AndreaBarocchi.CapstoneProject.payloads;


import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationPayload {
	
	@Pattern(regexp = "^[^<>{}$`]*$", message = "Username contains invalid characters")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @Pattern(regexp = "^[^<>{}$`]*$", message = "First name contains invalid characters")
    @Size(min = 3, max = 20, message = "First name must be between 3 and 20 characters")
    @NotEmpty(message = "First name is required")
    private String firstname;

    @Pattern(regexp = "^[^<>{}$`]*$", message = "Last name contains invalid characters")
    @Size(min = 3, max = 20, message = "Last name must be between 3 and 20 characters")
    @NotEmpty(message = "Last name is required")
    private String lastname;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[A-Za-z0-9\\-/]*$", message = "Invalid image URL")
    private String profileImgUrl;
    
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", 
            message = "Password must be at least 8 characters long and contain at least one digit, one letter, and one special character")
    private String password;
	
	private List<ArticlePayload> articles;
}
