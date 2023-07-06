package AndreaBarocchi.CapstoneProject.google;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class GoogleAuthService {
    
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    
    private RestTemplate restTemplate;
    
    public GoogleAuthService() {
        this.restTemplate = new RestTemplate();
    }
    
    //create auth url
    public String getAuthorizationUrl() {
        String authorizationUrl = "https://accounts.google.com/o/oauth2/auth";
        authorizationUrl += "?client_id=" + clientId;
        authorizationUrl += "&redirect_uri=" + redirectUri;
        authorizationUrl += "&response_type=code";
        authorizationUrl += "&state=state_parameter_passthrough_value";
        authorizationUrl += "&scope=email%20profile";
        
        return authorizationUrl;
    }
    
    public GoogleAccessTokenResponse getAccessToken(String authorizationCode) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        
        ResponseEntity<GoogleAccessTokenResponse> responseEntity = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            requestEntity,
            GoogleAccessTokenResponse.class
        );
        
        return responseEntity.getBody();
    }
    
    public GoogleUserInfoResponse getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        
        ResponseEntity<GoogleUserInfoResponse> responseEntity = restTemplate.exchange(
            userInfoUrl,
            HttpMethod.GET,
            requestEntity,
            GoogleUserInfoResponse.class
        );
        
        return responseEntity.getBody();
    }
}

