package AndreaBarocchi.CapstoneProject.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import AndreaBarocchi.CapstoneProject.entities.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID>{
	List<Like> findByUserUserId(UUID userId);
	Like findByArticleArticleIdAndUserUserId(UUID article, UUID user);
	List<Like> findByArticleArticleId(UUID articleId);
}
