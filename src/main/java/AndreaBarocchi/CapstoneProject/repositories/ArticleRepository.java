package AndreaBarocchi.CapstoneProject.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AndreaBarocchi.CapstoneProject.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID>{
    List<Article> findByTitleContainingIgnoreCase(String keyword);
    List<Article> findByUserUsernameContainingIgnoreCase(String keyword);
    List<Article> findByCategoryCategoryNameContainingIgnoreCase(String keyword);
}

