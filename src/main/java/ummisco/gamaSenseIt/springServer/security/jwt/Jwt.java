package ummisco.gamaSenseIt.springServer.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class Jwt {

    private final Claims claims;

    public Jwt(String token, String secret){
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

    private boolean canBeRefreshed() {
        return !this.isExpired();
    }

    public boolean validate(UserDetails user) {
        return this.username().equals(user.getUsername()) && !this.isExpired();
    }

}
