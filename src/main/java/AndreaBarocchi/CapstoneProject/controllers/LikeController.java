package AndreaBarocchi.CapstoneProject.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import AndreaBarocchi.CapstoneProject.entities.Like;
import AndreaBarocchi.CapstoneProject.services.LikeService;

@RestController
@RequestMapping("/likes")
public class LikeController {
    
    @Autowired
    private LikeService likeService;

    @PostMapping("/{articleId}/{userId}")
    public Like addLike(@RequestBody Like like, @PathVariable("articleId") UUID articleId, @PathVariable("userId") UUID userId) throws NotFoundException {
        return likeService.addLike(like, articleId, userId);
    }
    
	@DeleteMapping("/{articleId}/{userId}")
	public void deleteLike(@PathVariable("articleId") UUID articleId, @PathVariable("userId") UUID userId) throws NotFoundException {
	    likeService.deleteLike(articleId, userId);
	}
}
