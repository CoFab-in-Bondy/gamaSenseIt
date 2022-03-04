package ummisco.gamaSenseIt.springServer.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.tokenValidity:3600000}")
    private long tokenValidity;

    public Jwt parse(String token) {
        return new Jwt(token, secret);
    }

    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateTokenForUser(UserDetails user) {
        return generateToken(new HashMap<>() {{
            put("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        }}, user.getUsername());
    }
}
