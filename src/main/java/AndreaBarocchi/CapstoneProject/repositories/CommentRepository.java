package AndreaBarocchi.CapstoneProject.repositories;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AndreaBarocchi.CapstoneProject.entities.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID>{
	 List<Comment> findByUserUserId(UUID userId);
	 Page<Comment> findAllByArticleArticleId(UUID articleId, Pageable pageable);
	 List<Comment> findByArticleArticleId(UUID articleId);
}
