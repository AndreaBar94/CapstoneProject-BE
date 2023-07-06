package AndreaBarocchi.CapstoneProject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import AndreaBarocchi.CapstoneProject.exceptions.NotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import AndreaBarocchi.CapstoneProject.entities.Article;
import AndreaBarocchi.CapstoneProject.entities.Category;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.exceptions.UnauthorizedException;
import AndreaBarocchi.CapstoneProject.payloads.ArticlePayload;
import AndreaBarocchi.CapstoneProject.payloads.UserRegistrationPayload;
import AndreaBarocchi.CapstoneProject.repositories.ArticleRepository;
import AndreaBarocchi.CapstoneProject.repositories.CategoryRepository;
import AndreaBarocchi.CapstoneProject.repositories.UserRepository;
import AndreaBarocchi.CapstoneProject.services.ArticleService;
import AndreaBarocchi.CapstoneProject.services.UserService;

@SpringBootTest
class CapstoneProjectApplicationTests {

	@Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ArticleService articleService;
    
    @InjectMocks
    private UserService userService;
    

    @Test
    public void testCreateUser() {
        // Mocking the repository
        UserRegistrationPayload payload = new UserRegistrationPayload();
        payload.setUsername("testuser");
        payload.setFirstname("Test");
        payload.setLastname("User");
        payload.setEmail("testuser@example.com");
        payload.setPassword("password");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        // Calling the service method
        User result = userService.createUser(payload);

        // Assertions
        assertNotNull(result);
        // Additional assertions based on your expected data

        // Verify repository method was called
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }


    @Test
    public void testFindUserById() throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testUpdateUser() throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException {
        UUID userId = UUID.randomUUID();
        UserRegistrationPayload payload = new UserRegistrationPayload();
        payload.setUsername("updateduser");
        payload.setFirstname("Updated");
        payload.setLastname("User");
        payload.setEmail("updateduser@example.com");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUsername("olduser");
        existingUser.setFirstname("Old");
        existingUser.setLastname("User");
        existingUser.setEmail("olduser@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(authentication.getPrincipal()).thenReturn(existingUser);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(userId, payload, authentication);

        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getUserId());
        assertEquals(payload.getUsername(), updatedUser.getUsername());
        assertEquals(payload.getFirstname(), updatedUser.getFirstname());
        assertEquals(payload.getLastname(), updatedUser.getLastname());
        assertEquals(payload.getEmail(), updatedUser.getEmail());
        assertEquals(payload.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void testUpdateUserUnauthorized() {
        UUID userId = UUID.randomUUID();
        UserRegistrationPayload payload = new UserRegistrationPayload();
        payload.setUsername("updateduser");
        payload.setFirstname("Updated");
        payload.setLastname("User");
        payload.setEmail("updateduser@example.com");
        payload.setPassword("updatedpassword");

        User existingUser = new User();
        existingUser.setUserId(userId);
        existingUser.setUsername("olduser");
        existingUser.setFirstname("Old");
        existingUser.setLastname("User");
        existingUser.setEmail("olduser@example.com");
        existingUser.setPassword("oldpassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        
        // Create an instance of AuthenticatedUser and set the necessary properties
        User authenticatedUser = new User();
        authenticatedUser.setEmail("user@example.com");

        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        assertThrows(UnauthorizedException.class, () -> {
            userService.updateUser(userId, payload, authentication);
        });
    }


    @Test
    public void testDeleteUser() throws org.springframework.data.crossstore.ChangeSetPersister.NotFoundException {
        UUID userId = UUID.randomUUID();

        User foundUser = new User();
        foundUser.setUserId(userId);
        foundUser.setEmail("testuser@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        when(authentication.getName()).thenReturn("testuser@example.com");

        userService.deleteUser(userId, authentication);

        verify(userRepository).delete(foundUser);
    }
    

    @Test
    public void testDeleteUserUnauthorized() {
        UUID userId = UUID.randomUUID();

        User foundUser = new User();
        foundUser.setUserId(userId);
        foundUser.setEmail("testuser@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(foundUser));
        when(authentication.getName()).thenReturn("otheruser@example.com");

        assertThrows(UnauthorizedException.class, () -> {
            userService.deleteUser(userId, authentication);
        });
    }

    
    @Test
    public void testCreateArticle() throws NotFoundException {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setUserId(userId);
        
        ArticlePayload payload = new ArticlePayload();
        payload.setTitle("Test Article");
        payload.setContent("This is a test article");
        payload.setCategoryName("Test Category");

        Category category = new Category();
        category.setCategoryName("Test Category");
        
        Article article = new Article();
        article.setTitle(payload.getTitle());
        article.setContent(payload.getContent());
        article.setPublicationDate(LocalDate.now());
        article.setUser(user);
        article.setLikes(new ArrayList<>());
        article.setComments(new ArrayList<>());
        article.setCategory(category);

        when(categoryRepository.findByCategoryName(anyString())).thenReturn(category);
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article createdArticle = articleService.createArticle(user, payload);

        assertNotNull(createdArticle);
        assertEquals(payload.getTitle(), createdArticle.getTitle());
        assertEquals(payload.getContent(), createdArticle.getContent());
        assertEquals(user, createdArticle.getUser());
        assertEquals(category, createdArticle.getCategory());
    }

    @Test
    public void testFindAllArticles() {
        int page = 0;
        int size = 10;
        String sortBy = "title";

        List<Article> articles = new ArrayList<>();
        articles.add(new Article());
        articles.add(new Article());

        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Article> articlePage = new PageImpl<>(articles, pageable, articles.size());

        when(articleRepository.findAll(pageable)).thenReturn(articlePage);

        Page<Article> result = articleService.findAllArticles(page, size, sortBy);

        assertNotNull(result);
        assertEquals(articles.size(), result.getContent().size());
    }


    @Test
    public void testFindArticleById() throws NotFoundException {
        UUID articleId = UUID.randomUUID();
        Article article = new Article();
        article.setArticleId(articleId);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        Article result = articleService.findArticleById(articleId);

        assertNotNull(result);
        assertEquals(articleId, result.getArticleId());
    }

    @Test
    public void testFindArticleByIdNotFound() {
        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            articleService.findArticleById(articleId);
        });
    }

    @Test
    public void testUpdateArticle() throws NotFoundException {
        UUID articleId = UUID.randomUUID();
        ArticlePayload payload = new ArticlePayload();
        payload.setTitle("Updated Article");
        payload.setContent("This is an updated article");
        payload.setPublicationDate(LocalDate.now());

        User user = new User();
        user.setUserId(UUID.randomUUID());

        Article existingArticle = new Article();
        existingArticle.setArticleId(articleId);
        existingArticle.setTitle("Old Article");
        existingArticle.setContent("This is an old article");
        existingArticle.setPublicationDate(LocalDate.now());
        existingArticle.setUser(user);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(authentication.getPrincipal()).thenReturn(user);
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Article updatedArticle = articleService.updateArticle(articleId, payload, authentication);

        assertNotNull(updatedArticle);
        assertEquals(articleId, updatedArticle.getArticleId());
        assertEquals(payload.getTitle(), updatedArticle.getTitle());
        assertEquals(payload.getContent(), updatedArticle.getContent());
        assertEquals(payload.getPublicationDate(), updatedArticle.getPublicationDate());
    }

    @Test
    public void testUpdateArticleNotFound() {
        UUID articleId = UUID.randomUUID();
        ArticlePayload payload = new ArticlePayload();
        payload.setTitle("Updated Article");
        payload.setContent("This is an updated article");
        payload.setPublicationDate(LocalDate.now());

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            articleService.updateArticle(articleId, payload, authentication);
        });
    }

    @Test
    public void testUpdateArticleUnauthorized() {
        UUID articleId = UUID.randomUUID();
        ArticlePayload payload = new ArticlePayload();
        payload.setTitle("Updated Article");
        payload.setContent("This is an updated article");
        payload.setPublicationDate(LocalDate.now());

        User user1 = new User();
        user1.setUserId(UUID.randomUUID());

        User user2 = new User();
        user2.setUserId(UUID.randomUUID());

        Article existingArticle = new Article();
        existingArticle.setArticleId(articleId);
        existingArticle.setTitle("Old Article");
        existingArticle.setContent("This is an old article");
        existingArticle.setPublicationDate(LocalDate.now());
        existingArticle.setUser(user1);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(authentication.getPrincipal()).thenReturn(user2);

        assertThrows(UnauthorizedException.class, () -> {
            articleService.updateArticle(articleId, payload, authentication);
        });
    }
    
    @Test
    public void testDeleteArticle() {
        // Chiamare il metodo deleteArticle
        UUID articleId = UUID.randomUUID();

        // Configurare il comportamento del repository mock
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        User authenticatedUser = new User();
        authenticatedUser.setUserId(UUID.randomUUID());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(authenticatedUser);

        // Verificare che il metodo delete del repository non venga chiamato
        assertThrows(NotFoundException.class, () -> {
            articleService.deleteArticle(articleId, authentication);
        });

        verify(articleRepository, never()).delete(any());
    }



    @Test
    public void testDeleteArticleNotFound() {
        UUID articleId = UUID.randomUUID();

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            articleService.deleteArticle(articleId, authentication);
        });
    }

    @Test
    public void testDeleteArticleUnauthorized() {
        UUID articleId = UUID.randomUUID();

        User user1 = new User();
        user1.setUserId(UUID.randomUUID());

        User user2 = new User();
        user2.setUserId(UUID.randomUUID());

        Article existingArticle = new Article();
        existingArticle.setArticleId(articleId);
        existingArticle.setUser(user1);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));
        when(authentication.getPrincipal()).thenReturn(user2);

        assertThrows(UnauthorizedException.class, () -> {
            articleService.deleteArticle(articleId, authentication);
        });
    }

    

}
