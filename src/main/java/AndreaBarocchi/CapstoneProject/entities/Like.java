package AndreaBarocchi.CapstoneProject.entities;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import AndreaBarocchi.CapstoneProject.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
