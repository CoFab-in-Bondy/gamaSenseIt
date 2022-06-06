package ummisco.gamaSenseIt.springServer.data.controller;

import org.apache.commons.collections.map.SingletonMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.classes.Node;
import ummisco.gamaSenseIt.springServer.data.model.user.RefreshToken;
import ummisco.gamaSenseIt.springServer.data.model.user.User;
import ummisco.gamaSenseIt.springServer.data.repositories.IRefreshTokenRepository;
import ummisco.gamaSenseIt.springServer.data.repositories.IUserRepository;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtToken;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequestMapping(Routes.AUTH)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserRepository userRepository;
    private final IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            IUserRepository userRepository,
            IRefreshTokenRepository refreshTokenRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping(value = Routes.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest req) throws Exception {
        String mail = Objects.requireNonNull(req.username());
        String password = Objects.requireNonNull(req.password());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account as been disabled");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalids credentials");
        }

        var user = userRepository.findByMail(mail)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalids credentials"));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtUtils.generateAccessCookie(user));
        headers.add(HttpHeaders.SET_COOKIE, jwtUtils.generateRefreshCookie(user));
        return ResponseEntity.ok().headers(headers).body(new Node() {{
            put("status", "ok");
            put("role", user.getPrivilege().toString());
        }});
    }

    @PostMapping(value = Routes.REFRESH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = JwtToken.REFRESH_TOKEN_COOKIE_NAME, required = false) String refreshToken
    ) {
        logger.info("Refreshing token");
        // Must have a refresh token for ask for new access token
        var refresh = jwtUtils.processRefresh(refreshToken).filter(RefreshToken::isExpired).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be authenticate");
        });

        // Generate new accessToken
        User user = refresh.getUser();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtUtils.generateAccessCookie(user));
        return ResponseEntity.ok().headers(headers).body(new SingletonMap("status", "ok"));
    }

    @PostMapping(value = Routes.LOGOUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        logger.info("Logout");
        // Delete / Invalidate refresh token
        jwtUtils.extractRefresh(request).ifPresent(refreshTokenRepository::delete);

        // Destroy the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Clear the context and authentication
        SecurityContext context = SecurityContextHolder.getContext();
        SecurityContextHolder.clearContext();
        context.setAuthentication(null);

        // Expire and reset all token
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwtUtils.expiredCookie(JwtToken.ACCESS_TOKEN_COOKIE_NAME));
        headers.add(HttpHeaders.SET_COOKIE, jwtUtils.expiredCookie(JwtToken.REFRESH_TOKEN_COOKIE_NAME));
        return ResponseEntity.ok().headers(headers).body(new SingletonMap("status", "ok"));
    }

    private record LoginRequest(String username, String password) {
    }
}
