package AndreaBarocchi.CapstoneProject.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue
	private UUID commentId;
	
	private String content;
	private LocalDate publicationDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"comments"})
    private User user;
    
    private boolean isCensored;
    
	public Comment(String content, LocalDate publicationDate, Article article, User user) {
		super();
		this.content = content;
		this.publicationDate = publicationDate;
		this.article = article;
		this.user = user;
	}

}
