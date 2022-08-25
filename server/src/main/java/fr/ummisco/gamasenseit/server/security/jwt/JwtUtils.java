package fr.ummisco.gamasenseit.server.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import fr.ummisco.gamasenseit.server.data.model.user.RefreshToken;
import fr.ummisco.gamasenseit.server.data.model.user.User;
import fr.ummisco.gamasenseit.server.data.repositories.IRefreshTokenRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${gamaSenseIt.accessTokenValidity}")
    private long accessTokenValidity;

    @Value("${gamaSenseIt.refreshTokenValidity}")
    private long refreshTokenValidity;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepo;

    public Optional<RefreshToken> extractRefresh(HttpServletRequest request) {
        return extract(JwtToken.REFRESH_TOKEN_COOKIE_NAME, request).flatMap(this::processRefresh);
    }

    public Optional<JwtToken> extractAccess(HttpServletRequest request) {
        return extract(JwtToken.ACCESS_TOKEN_COOKIE_NAME, request).flatMap(this::processAccess);
    }

    public Optional<RefreshToken> processRefresh(String token) {
        if (token == null) return Optional.empty();
        return refreshTokenRepo.findRefreshTokensByTokenEquals(token);
    }

    public Optional<JwtToken> processAccess(String token) {
        try {
            var jwt = new JwtToken(token, secret);
            return Optional.of(jwt);
        } catch (SignatureException ex) {
            logger.error("Invalid JWT Signature");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, JwtRequestFilter.REFRESH);
        } catch (MalformedJwtException ex) {
            logger.error("Malformed JWT");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, JwtRequestFilter.REFRESH);
        } catch (ExpiredJwtException ex) {
            logger.info("JWT Token has expired");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, JwtRequestFilter.REFRESH);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT exception");
        } catch (IllegalArgumentException ex) {
            logger.info("Unable to get JWT Token");
        }
        return Optional.empty();
    }

    private Optional<String> extract(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null)
            return Optional.empty();
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                logger.info("Extracted " + name);
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    public String generateAccessCookie(User user) {
        String token = Jwts.builder()
            .setClaims(new HashMap<>() {{
                put("roles", List.of(user.getPrivilege().toString()));
            }})
            .setSubject(user.getMail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
        return makeCookie(JwtToken.ACCESS_TOKEN_COOKIE_NAME, token);
    }

    synchronized public String generateRefreshCookie(User user) {
        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (refreshTokenRepo.findRefreshTokensByTokenEquals(token).isPresent());
        var refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setIssuedAt(new Date());
        refreshToken.setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity));
        refreshToken.setUserId(user.getId());
        refreshTokenRepo.save(refreshToken);
        return makeCookie(JwtToken.REFRESH_TOKEN_COOKIE_NAME, token);
    }

    private String makeCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .maxAge(-1)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .secure(true)
                .build().toString();
    }

    public String expiredCookie(String key) {
        return ResponseCookie.from(key, "")
                .maxAge(0)
                .httpOnly(true)
                .path("/")
                .sameSite("Strict")
                .secure(true)
                .build().toString();
    }
}
