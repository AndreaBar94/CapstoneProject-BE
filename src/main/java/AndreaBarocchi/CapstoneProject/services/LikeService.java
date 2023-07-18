package AndreaBarocchi.CapstoneProject.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.Like;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.repositories.ArticleRepository;
import AndreaBarocchi.CapstoneProject.repositories.LikeRepository;

@Service
public class LikeService {
	
	@Autowired
	private LikeRepository likeRepo;
	@Autowired
	private ArticleRepository articleRepo;
	
	@Autowired
	@Lazy
	private UserService userService;
	
	public Like addLike(Like like, UUID articleId, UUID userId) throws NotFoundException {
	    Article article = articleRepo.findById(articleId).orElse(null);
	    User user = userService.findUserById(userId);
	    if (article != null) {
	    	
	        Like existingLike = likeRepo.findByArticleArticleIdAndUserUserId(articleId, userId);
	        if (existingLike != null) {
	        	deleteLike(articleId, userId);
	        	return null;
	        }
	        like.setArticle(article);
	        like.setUser(user);
	        return likeRepo.save(like);
	    }
	    return null;
	}

	
	public void deleteLike(UUID articleId, UUID userId) {
	    Like like = likeRepo.findByArticleArticleIdAndUserUserId(articleId, userId);
			 if (like != null) {
				 like.getArticle().getLikes().remove(like);

				 likeRepo.delete(like);
		}
	   
	}
	
}