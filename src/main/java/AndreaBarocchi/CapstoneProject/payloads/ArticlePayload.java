package AndreaBarocchi.CapstoneProject.payloads;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ArticlePayload {
	
	@Pattern(regexp = "^[^<>{}$`]*$", message = "Title contains invalid characters")
    @NotEmpty(message = "Title is required")
    private String title;

    @Pattern(regexp = "^[^<>{}$`]*$", message = "This article contains invalid characters")
    @NotEmpty(message = "Content is required")
    private String content;

    private LocalDate publicationDate;

    @Pattern(regexp = "^[^<>{}$`]*$", message = "Category name contains invalid characters")
    @NotEmpty(message = "Category name is required")
    private String categoryName;
	
	@Pattern(regexp = "^[A-Za-z0-9\\-/]*$", message = "Invalid image URL")
	private String imageUrl;
	
}
