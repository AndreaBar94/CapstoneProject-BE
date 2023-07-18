package AndreaBarocchi.CapstoneProject.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.exceptions.NotFoundException;
import AndreaBarocchi.CapstoneProject.payloads.ArticlePayload;
import AndreaBarocchi.CapstoneProject.services.ArticleService;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<Article>> getArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publicationDate") String sortBy) {
        Page<Article> articles = articleService.findAllArticles(page, size, sortBy);
        return ResponseEntity.ok(articles);
    }

    @PostMapping("")
    public ResponseEntity<Article> createArticle(Authentication authentication,
            @RequestBody ArticlePayload articlePayload) throws NotFoundException {
        User user = (User) authentication.getPrincipal();
        Article newArticle = articleService.createArticle(user, articlePayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(newArticle);
    }

    
    @GetMapping("/search/{filter}/{keyword}")
    public ResponseEntity<List<Article>> searchArticles(@PathVariable String filter, @PathVariable String keyword) {
        List<Article> articles;

        if (filter.equalsIgnoreCase("title")) {
            articles = articleService.searchByTitle(keyword);
        } else if (filter.equalsIgnoreCase("user")) {
            articles = articleService.searchByUser(keyword);
        } else if (filter.equalsIgnoreCase("category")) {
            articles = articleService.searchByCategory(keyword);
        } else {
            articles = new ArrayList<>();
        }

        return ResponseEntity.ok(articles);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable UUID id) throws NotFoundException {
        Article article = articleService.findArticleById(id);
        return ResponseEntity.ok(article);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable UUID id, @RequestBody ArticlePayload articlePayload,
            Authentication authentication) throws NotFoundException {
        Article updatedArticle = articleService.updateArticle(id, articlePayload, authentication);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable UUID id, Authentication authentication)
            throws NotFoundException {
        articleService.deleteArticle(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
