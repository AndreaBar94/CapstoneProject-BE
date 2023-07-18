package AndreaBarocchi.CapstoneProject.services;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.Comment;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.enums.UserRole;
import AndreaBarocchi.CapstoneProject.exceptions.NotFoundException;
import AndreaBarocchi.CapstoneProject.exceptions.UnauthorizedException;
import AndreaBarocchi.CapstoneProject.payloads.CommentPayload;
import AndreaBarocchi.CapstoneProject.repositories.ArticleRepository;
import AndreaBarocchi.CapstoneProject.repositories.CommentRepository;
import AndreaBarocchi.CapstoneProject.repositories.UserRepository;

@Service
public class CommentService {
	
	@Autowired
    private CommentRepository commentRepo;
	@Autowired
    private ArticleRepository articleRepo;
	@Autowired
    private UserRepository userRepo;


	public Comment createComment(UUID articleId, CommentPayload commentPayload, Authentication authentication)
	        throws NotFoundException {
	    Article article = articleRepo.findById(articleId)
	            .orElseThrow(() -> new NotFoundException("Article not found with ID: " + articleId));
	    User user = userRepo.findByUsername(authentication.getName())
	            .orElseThrow(() -> new NotFoundException("User not found"));

	    Comment comment = new Comment();
	    comment.setContent(commentPayload.getContent());
	    comment.setPublicationDate(LocalDate.now());
	    comment.setUser(user);
	    comment.setArticle(article);

	    return commentRepo.save(comment);
	}


    public Comment findCommentById(UUID commentId) throws NotFoundException {
        return commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found with ID: " + commentId));
    }

    public Page<Comment> findCommentsByArticleId(UUID articleId, int page, int size, String sortBy) {
    	if (size < 0)
			size = 10;
		if (size > 100)
			size = 20;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    	return commentRepo.findAllByArticleArticleId(articleId, pageable);
    }
    
    public Comment updateComment(UUID commentId, CommentPayload commentPayload, Authentication authentication)
            throws NotFoundException {
    	//trovo il commento
        Comment existingComment = findCommentById(commentId);
        User authenticatedUser = (User) authentication.getPrincipal();
        
        // Verifica se l'utente autenticato è l'autore del commento
        if (!existingComment.getUser().getEmail().equals(((User) authentication.getPrincipal()).getEmail())&& !authenticatedUser.getRole().equals(UserRole.ADMIN)) {
        	//passo le info dello user non autorizzato al messaggio di errore
            throw new UnauthorizedException(authenticatedUser.getFirstname() + " is not authorized to update this comment");
        }
        
        existingComment.setContent(commentPayload.getContent());
        return commentRepo.save(existingComment);
    }

    public void deleteComment(UUID commentId, Authentication authentication) throws NotFoundException {
        Comment comment = findCommentById(commentId);
        
        User authenticatedUser = (User) authentication.getPrincipal();
        
        // Verifica se l'utente autenticato è l'autore del commento
        if (!comment.getUser().getEmail().equals(((User) authentication.getPrincipal()).getEmail())&& !authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            throw new UnauthorizedException(authenticatedUser.getFirstname() + " is not authorized to delete this comment");
        }
        comment.getArticle().getComments().remove(comment);

        commentRepo.delete(comment);
    }


	public Comment censorComment(UUID commentId, Authentication authentication) throws NotFoundException {
		
		Comment comment = findCommentById(commentId); 
        User authenticatedUser = (User) authentication.getPrincipal();
        
        if (!comment.getUser().getEmail().equals(((User) authentication.getPrincipal()).getEmail())&& !authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            throw new UnauthorizedException(authenticatedUser.getFirstname() + " is not authorized to blame this comment");
        }
        
        if(comment.isCensored()) {
        	comment.setCensored(false);
        }else {
        	comment.setCensored(true);
        }
        
       return commentRepo.save(comment);
	}
}
