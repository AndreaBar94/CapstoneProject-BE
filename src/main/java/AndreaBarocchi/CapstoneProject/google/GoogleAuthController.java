package AndreaBarocchi.CapstoneProject.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.payloads.UserRegistrationPayload;
import AndreaBarocchi.CapstoneProject.services.UserService;

@RestController
public class GoogleAuthController {
	
	@Autowired
	private GoogleAuthService googleAuthService;
	
	@Autowired
	private UserService userService;
	
	
	//mandatory callback, it has to includes param "code", still not working
	@PostMapping("/google/callback")
	public String googleCallback(@RequestParam("code") String authorizationCode) throws NotFoundException {
		
		// using auth code from Google to get the access token
		GoogleAccessTokenResponse accessTokenResponse = googleAuthService.getAccessToken(authorizationCode);
		String accessToken = accessTokenResponse.getAccess_token();

		// using access token to get user info
		GoogleUserInfoResponse userInfoResponse = googleAuthService.getUserInfo(accessToken);
		String email = userInfoResponse.getEmail();
		String name = userInfoResponse.getName();
		
		// verify if user already in database
		User user = userService.findUserByEmail(email);
		
		if (user == null) {
			// if not create new user
			UserRegistrationPayload newUser = new UserRegistrationPayload();
			newUser.setEmail(email);
			newUser.setFirstname(name);
			
			userService.createUser(newUser);
		}


		// here to do redirect to appropriate page
		return "Callback completed successfully";
	}
	
	//this will return authorization google url
	@GetMapping("/google/authorization-url")
    public String getGoogleAuthorizationUrl() {
       return googleAuthService.getAuthorizationUrl();
        
    }
	
	//same as the method up here, this one will show the url in postman (cool)
//	@GetMapping("/google/authorization-url")
//	public ResponseEntity<String> getGoogleAuthorizationUrl() {
//		String authURL = googleAuthService.getAuthorizationUrl();
//		return ResponseEntity.ok(authURL);
//	}
    
}


