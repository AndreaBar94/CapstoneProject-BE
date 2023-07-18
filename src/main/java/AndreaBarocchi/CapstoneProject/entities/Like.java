package AndreaBarocchi.CapstoneProject.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Like {
	
	@Id
	@GeneratedValue
	private UUID likeId;
	
	private LocalDate interactionDate;
	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	@ManyToOne
	@JsonBackReference(value = "article-likes")
	private Article article;
	
	 public Like(LocalDate interactionDate, User user, Article article) {
	        this.interactionDate = interactionDate;
	        this.user = user;
	        this.article = article;
	    }

}
