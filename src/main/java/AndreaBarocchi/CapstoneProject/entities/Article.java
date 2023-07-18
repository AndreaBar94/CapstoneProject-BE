package AndreaBarocchi.CapstoneProject.entities;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	
	@Column(columnDefinition = "text")
	private String imageUrl;
	@ManyToOne
	private User user; //author
	
	@OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Comment> comments;

	@OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonManagedReference(value = "article-likes")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Like> likes;

	
    @ManyToOne
    @JoinColumn(name = "category_category_id")
    private Category category;


	public Article(String title, String content, LocalDate publicationDate, User user, List<Comment> comments,
			List<Like> likes, Category category, String imageUrl) {
		super();
		this.title = title;
		this.content = content;
		this.publicationDate = publicationDate;
		this.user = user;
		this.comments = comments;
		this.likes = likes;
		this.category = category;
		this.imageUrl = imageUrl;
	}
	
	public Article(String articleId) {
	    this.articleId = UUID.fromString(articleId);
	}
	
}
