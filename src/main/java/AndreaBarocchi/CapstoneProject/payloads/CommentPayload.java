package AndreaBarocchi.CapstoneProject.payloads;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentPayload {
	@NotEmpty(message = "You can't submit an empty comment")
	private String content;
}
