package AndreaBarocchi.CapstoneProject.google;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import AndreaBarocchi.CapstoneProject.auth.JWTTools;
import AndreaBarocchi.CapstoneProject.entities.User;
import AndreaBarocchi.CapstoneProject.payloads.AuthenticationSuccessfullPayload;
import AndreaBarocchi.CapstoneProject.payloads.UserRegistrationPayload;
import AndreaBarocchi.CapstoneProject.services.UserService;

@RestController
@RequestMapping("/google")
public class GoogleAuthController {
	
	@Autowired
	private GoogleAuthService googleAuthService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/callback")
	public ResponseEntity<AuthenticationSuccessfullPayload> googleCallback(@RequestParam("code") String authorizationCode) throws NotFoundException {
		// using auth code from Google to get the access token
		GoogleAccessTokenResponse accessTokenResponse = googleAuthService.getAccessToken(authorizationCode);
		String accessToken = accessTokenResponse.getAccess_token();

		// using access token to get user info
		GoogleUserInfoResponse userInfoResponse = googleAuthService.getUserInfo(accessToken);
		String email = userInfoResponse.getEmail();
		String username = userInfoResponse.getName();
		String firstname = userInfoResponse.getGiven_name();
		String lastname = userInfoResponse.getFamily_name();
		String profileImgUrl = userInfoResponse.getPicture();
		
		User user = null;
		
		// verify if user already in database
		try {
			user = userService.findUserByEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		if (user == null) {
			// if not create new user
			UserRegistrationPayload newUser = new UserRegistrationPayload();
			newUser.setEmail(email);
			newUser.setUsername(username);
			newUser.setFirstname(firstname);
			newUser.setLastname(lastname);
			newUser.setProfileImgUrl(profileImgUrl);
			
			userService.createUser(newUser);
		}
		
		String token = JWTTools.createToken(user);
		
		return new ResponseEntity<>(new AuthenticationSuccessfullPayload(token), HttpStatus.OK);
	}
	
	//this will return authorization google url
	@GetMapping("/authorization-url")
    public ResponseEntity<String> getGoogleAuthorizationUrl() {
       return new ResponseEntity<>( googleAuthService.getAuthorizationUrl(), HttpStatus.OK);
        
    }
    
}


