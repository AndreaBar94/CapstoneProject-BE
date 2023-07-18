package AndreaBarocchi.CapstoneProject.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import AndreaBarocchi.CapstoneProject.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID>{
    List<Article> findByTitleContainingIgnoreCase(String keyword);
    List<Article> findByUserUsernameContainingIgnoreCase(String keyword);
    List<Article> findByCategoryCategoryNameContainingIgnoreCase(String keyword);
    
    @Query("SELECT a FROM Article a ORDER BY SIZE(a.likes) DESC")
    Page<Article> findAllArticlesOrderByLikes(Pageable pageable);
    
    @Query("SELECT a FROM Article a ORDER BY a.publicationDate DESC")
    Page<Article> findAllArticlesOrderByPublicationDate(Pageable pageable);
}

