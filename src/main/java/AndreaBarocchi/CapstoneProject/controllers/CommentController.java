package AndreaBarocchi.CapstoneProject.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import AndreaBarocchi.CapstoneProject.entities.Comment;
import AndreaBarocchi.CapstoneProject.payloads.CommentPayload;
import AndreaBarocchi.CapstoneProject.services.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@GetMapping("/article/{articleId}")
	public ResponseEntity<Page<Comment>> getCommentsByArticleId(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "username") String sortBy,
	        @PathVariable UUID articleId) {
	    Page<Comment> comments = commentService.findCommentsByArticleId(articleId, page, size, sortBy);
	    return ResponseEntity.ok(comments);
	}


	@PostMapping("/article/{articleId}")
    public ResponseEntity<Comment> createComment(
            @PathVariable UUID articleId,
            @RequestBody CommentPayload commentPayload,
            Authentication authentication) throws NotFoundException {
        Comment newComment = commentService.createComment(articleId, commentPayload, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
    }


	@GetMapping("/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable UUID id) throws NotFoundException {
		Comment comment = commentService.findCommentById(id);
		return ResponseEntity.ok(comment);
	}

	@PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable UUID id, @RequestBody CommentPayload commentPayload,
                                                 Authentication authentication) throws NotFoundException {
        Comment updatedComment = commentService.updateComment(id, commentPayload, authentication);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id, Authentication authentication)
            throws NotFoundException {
        commentService.deleteComment(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
