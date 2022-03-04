package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ummisco.gamaSenseIt.springServer.security.UserDetailService;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(IRoute.AUTH)
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    private record JwtRequest(String username, String password) {}

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest req) throws Exception {
        String mail = Objects.requireNonNull(req.username);
        String password = Objects.requireNonNull(req.password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLE", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        var user = userDetailService.loadUserByUsername(mail);
        String token = jwtUtils.generateTokenForUser(user);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @RequestMapping(value = IRoute.ME, method = RequestMethod.GET)
    public Map<String, ?> currentUserName(Authentication auth) {
        return new HashMap<>(){{
            if (auth == null) {
                put("auth", false);
                put("name", "");
                put("roles", Collections.emptyList());
            } else {
                put("auth", auth.isAuthenticated());
                put("name", auth.getName());
                put("roles", auth.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
            }
        }};
    }
}
