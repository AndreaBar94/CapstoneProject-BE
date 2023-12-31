package AndreaBarocchi.CapstoneProject.entities;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue
	private UUID categoryId;
	
	private String categoryName;
	
	@OneToMany(mappedBy = "category")
	@JsonIgnore
	private List<Article> articles;
	
}
