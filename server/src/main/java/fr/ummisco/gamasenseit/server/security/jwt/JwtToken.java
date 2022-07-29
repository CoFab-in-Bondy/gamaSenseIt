package fr.ummisco.gamasenseit.server.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtToken {

    private final Claims claims;

    public JwtToken(String token, String secret){
        this.claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String username() {
        return this.claims.getSubject();
    }

    public Date issuedAt() {
        return this.claims.getIssuedAt();
    }

    public Date expiration() {
        return this.claims.getExpiration();
    }

    private boolean isExpired() {
        return this.expiration().before(new Date());
    }

    public boolean validateExpirationAndUser(UserDetails user) {
        return this.username().equals(user.getUsername()) && !this.isExpired();
    }

    public static final String ACCESS_TOKEN_COOKIE_NAME = "AccessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "RefreshToken";
}

