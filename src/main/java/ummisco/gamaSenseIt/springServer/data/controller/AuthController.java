package ummisco.gamaSenseIt.springServer.data.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(IRoute.AUTH)
public class AuthController {

    @CrossOrigin
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
