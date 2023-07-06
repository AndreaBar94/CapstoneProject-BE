package AndreaBarocchi.CapstoneProject.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.payloads.UserRegistrationPayload;
import AndreaBarocchi.CapstoneProject.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy) {
        Page<User> users = userService.findAllUsers(page, size, sortBy);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRegistrationPayload userPayload) {
        User newUser = userService.createUser(userPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) throws NotFoundException {
        User userDetails = (User) authentication.getPrincipal();
        UUID userId = userDetails.getUserId();
        User currentUser = userService.findUserById(userId);
        return ResponseEntity.ok(currentUser);
    }


    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) throws NotFoundException {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserRegistrationPayload userPayload,
                                            Authentication authentication) throws NotFoundException {
        User updatedUser = userService.updateUser(id, userPayload, authentication);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id, Authentication authentication)
            throws NotFoundException {
        userService.deleteUser(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
