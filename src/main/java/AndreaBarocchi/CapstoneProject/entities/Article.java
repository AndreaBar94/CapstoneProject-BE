package AndreaBarocchi.CapstoneProject.entities;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
public class Article {
	
	@Id
	@GeneratedValue
	private UUID articleId;
	
	private String title;
	@Column(columnDefinition = "text")
	private String content;
	
	private LocalDate publicationDate;
	
	@ManyToOne
	private User user; //author
	
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Comment> comments;
	
	
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "article-likes")
    private List<Like> likes;

	
    @ManyToOne
    @JoinColumn(name = "category_category_id")
    private Category category;


	public Article(String title, String content, LocalDate publicationDate, User user, List<Comment> comments,
			List<Like> likes, Category category) {
		super();
		this.title = title;
		this.content = content;
		this.publicationDate = publicationDate;
		this.user = user;
		this.comments = comments;
		this.likes = likes;
		this.category = category;
	}
	
	public Article(String articleId) {
	    this.articleId = UUID.fromString(articleId);
	}
	
}
