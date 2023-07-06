package AndreaBarocchi.CapstoneProject.google;

import lombok.Data;

@Data
public class GoogleAccessTokenResponse {
    private String access_token;
    private String expires_in;
    private String token_type;
    private String refresh_token;
    // Altri campi opzionali
    
    // Getter e setter
}
