package AndreaBarocchi.CapstoneProject.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import AndreaBarocchi.CapstoneProject.enums.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@JsonIgnoreProperties({ "password" })
public class User implements UserDetails{
	
	@Id
	@GeneratedValue
	private UUID userId;
	
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String password;
	@Column(columnDefinition = "text")
	private String profileImgUrl;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"user", "comments", "likes"})
	private List<Like> likes;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user", "comments"})
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"user"})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> comments;

	public User(String username, String firstname, String lastname, String email, String password, List<Article> articles, String profileImgUrl) {
		super();
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.role = UserRole.USER;
		this.articles = articles;
		this.profileImgUrl = profileImgUrl;
	}
	
	public void addArticle(Article article) {
        this.articles.add(article);
        article.setUser(this);
    }

    public void removeArticle(Article article) {
        this.articles.remove(article);
        article.setUser(null);

        article.getComments().clear();

        article.getLikes().clear();
    }
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}

	
	
}
