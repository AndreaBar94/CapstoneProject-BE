package AndreaBarocchi.CapstoneProject.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.Category;
import AndreaBarocchi.CapstoneProject.entities.Comment;
import AndreaBarocchi.CapstoneProject.entities.Like;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.enums.UserRole;
import AndreaBarocchi.CapstoneProject.exceptions.NotFoundException;
import AndreaBarocchi.CapstoneProject.exceptions.UnauthorizedException;
import AndreaBarocchi.CapstoneProject.payloads.ArticlePayload;
import AndreaBarocchi.CapstoneProject.repositories.ArticleRepository;
import AndreaBarocchi.CapstoneProject.repositories.CategoryRepository;
import AndreaBarocchi.CapstoneProject.utils.CustomPageable;
import jakarta.transaction.Transactional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    
    public Article createArticle(User user, ArticlePayload payload) throws NotFoundException {

        Category category = categoryRepo.findByCategoryName(payload.getCategoryName());
        if (category == null) {
            throw new NotFoundException("Category not found");
        }
        
        Article article = new Article(
        		payload.getTitle(),
        		payload.getContent(),
        		LocalDate.now(),
        		user,
                new ArrayList<>(),
                new ArrayList<>(),
                category,
                payload.getImageUrl());

        Article savedArticle = articleRepo.save(article);

        user.addArticle(savedArticle);

        return savedArticle;
    }

    
    public Page<Article> findAllArticles(int page, int size, String sortBy) {
        if (size < 0)
            size = 10;
        if (size > 100)
            size = 20;

        Pageable pageable;
        if (sortBy.equals("likes")) {
            pageable = PageRequest.of(page, size);
            return articleRepo.findAllArticlesOrderByLikes(pageable);
        } else if (sortBy.equals("publicationDate")) {
            pageable = PageRequest.of(page, size);
            return articleRepo.findAllArticlesOrderByPublicationDate(pageable);
        }else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return articleRepo.findAll(pageable);
        }
    }

    public Article findArticleById(UUID id) throws NotFoundException {
        return articleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Article with ID " + id + " not found"));
    }
    
    public List<Article> searchByTitle(String keyword) {
        return articleRepo.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Article> searchByUser(String keyword) {
        return articleRepo.findByUserUsernameContainingIgnoreCase(keyword);
    }

    public List<Article> searchByCategory(String keyword) {
        return articleRepo.findByCategoryCategoryNameContainingIgnoreCase(keyword);
    }
    
    public Article updateArticle(UUID articleId, ArticlePayload articlePayload, Authentication authentication)
            throws NotFoundException {
        Article existingArticle = findArticleById(articleId);
        User authenticatedUser = (User) authentication.getPrincipal();

        if (!existingArticle.getUser().getUserId().equals(authenticatedUser.getUserId()) && !authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            throw new UnauthorizedException("Unauthorized to update this article");
        }

        existingArticle.setTitle(articlePayload.getTitle());
        existingArticle.setContent(articlePayload.getContent());
        existingArticle.setImageUrl(articlePayload.getImageUrl());
        existingArticle.setPublicationDate(existingArticle.getPublicationDate());
        return articleRepo.save(existingArticle);
    }
    
    @Transactional
    public void deleteArticle(UUID articleId, Authentication authentication) throws NotFoundException {
        Article existingArticle = findArticleById(articleId);
        User authenticatedUser = (User) authentication.getPrincipal();

        if (!existingArticle.getUser().getUserId().equals(authenticatedUser.getUserId()) && !authenticatedUser.getRole().equals(UserRole.ADMIN)) {
            throw new UnauthorizedException("Unauthorized to delete this article");
        }
        // Remove pairing with comments
        for (Comment comment : existingArticle.getComments()) {
            comment.setArticle(null);
        }
        existingArticle.getComments().clear();

        // Remove pairing with likes
        for (Like like : existingArticle.getLikes()) {
            like.setArticle(null);
        }
        existingArticle.getLikes().clear();
        
        //Remove pairing with user
        existingArticle.getUser().getArticles().remove(existingArticle);
       
        articleRepo.delete(existingArticle);

    }
}
