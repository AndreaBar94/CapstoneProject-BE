package AndreaBarocchi.CapstoneProject.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.Comment;
import AndreaBarocchi.CapstoneProject.entities.Like;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.exceptions.EmailAlreadyExistsException;
import AndreaBarocchi.CapstoneProject.exceptions.UnauthorizedException;
import AndreaBarocchi.CapstoneProject.payloads.ArticlePayload;
import AndreaBarocchi.CapstoneProject.payloads.UserRegistrationPayload;
import AndreaBarocchi.CapstoneProject.repositories.ArticleRepository;
import AndreaBarocchi.CapstoneProject.repositories.CommentRepository;
import AndreaBarocchi.CapstoneProject.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ArticleRepository articleRepo;
    
    public Page<User> findAllUsers(int page, int size, String sortBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepo.findAll(pageable);
    }

    public User createUser(UserRegistrationPayload uPld) {
        userRepo.findByEmail(uPld.getEmail()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email " + user.getEmail() + " already exists");
        });
        List<Article> articles = convertToArticles(uPld.getArticles());
        User newUser = new User(uPld.getUsername(), uPld.getFirstname(), uPld.getLastname(), uPld.getEmail(),
                uPld.getPassword(), articles, uPld.getProfileImgUrl());
        return userRepo.save(newUser);
    }

    private List<Article> convertToArticles(List<ArticlePayload> articlePayloads) {
        List<Article> articles = new ArrayList<>();
        if (articlePayloads != null) {
            for (ArticlePayload payload : articlePayloads) {
                Article article = new Article(payload.getTitle(), payload.getContent(), payload.getPublicationDate(), null, null, null, null, null);
                articles.add(article);
            }
        }
        return articles;
    }
    
    public User findUserById(UUID id) throws NotFoundException {
        return userRepo.findById(id).orElseThrow(() -> new NotFoundException());
    }

    public User updateUser(UUID userId, UserRegistrationPayload uPld, Authentication authentication)
            throws NotFoundException {
        User foundUser = findUserById(userId);
        String authenticatedUserEmail = ((User) authentication.getPrincipal()).getEmail();
        if (!foundUser.getEmail().equals(authenticatedUserEmail.toString())) {
            throw new UnauthorizedException("Unauthorized to update this user");
        }

        foundUser.setUsername(uPld.getUsername());
        foundUser.setFirstname(uPld.getFirstname());
        foundUser.setLastname(uPld.getLastname());
        foundUser.setEmail(uPld.getEmail());
        foundUser.setProfileImgUrl(uPld.getProfileImgUrl());
        return userRepo.save(foundUser);
    }
    
    public User getDefaultUser() {
        User defaultUser = userRepo.getUserByEmail("defaultUser@email.it");
        return defaultUser;
    }

    
    @Transactional
    public void deleteUser(UUID userId, Authentication authentication) throws NotFoundException {
    	
        User foundUser = findUserById(userId);
        User authenticatedUser = (User) authentication.getPrincipal();

        if (!foundUser.getEmail().equals(authenticatedUser.getEmail())) {
            throw new UnauthorizedException("Unauthorized to delete this user");
        }
        
        User defaultUser = getDefaultUser();
        
        //remove pairing with comments
        for (Comment comment : foundUser.getComments()) {
        	comment.setUser(defaultUser);
        	
        }
        defaultUser.setComments(foundUser.getComments());
        
        foundUser.getComments().clear();
        
        //remove pairing with likes
        for (Like like : foundUser.getLikes()) {
            like.setUser(null);
        }
        foundUser.getLikes().clear();
        
        //set defaultUser as owner of foundUser's articles (don't wanna lost all my nice articles)
        for (Article article : foundUser.getArticles()) {
        	article.setUser(defaultUser);
        	defaultUser.addArticle(article);
        	articleRepo.save(article);
        }
        foundUser.getArticles().clear();
        
        userRepo.delete(foundUser);
    }

    public User findUserByEmail(String email) throws NotFoundException {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException();
        }
    }

    public void deleteAllUsers() {
        userRepo.deleteAll();
    }
}
