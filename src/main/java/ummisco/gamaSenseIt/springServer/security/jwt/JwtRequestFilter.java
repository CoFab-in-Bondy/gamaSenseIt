package ummisco.gamaSenseIt.springServer.security.jwt;

import io.micrometer.core.lang.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import ummisco.gamaSenseIt.springServer.data.controller.Routes;
import ummisco.gamaSenseIt.springServer.data.model.APIError;
import ummisco.gamaSenseIt.springServer.security.UserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);


    public static final String REFRESH = "Refresh";

    private final JwtUtils jwtUtils;
    private final UserDetailService userDetailsService;

    @Autowired
    public JwtRequestFilter(JwtUtils jwtUtils, UserDetailService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @Nullable HttpServletRequest request, @Nullable HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String url = request == null ? "" : request.getRequestURL().toString();
        try {
            jwtUtils.extractAccess(request).ifPresent(jwt -> {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    var user = this.userDetailsService.loadUserByUsername(jwt.username());
                    if (jwt.validateExpirationAndUser(user)) {
                        logger.info("Request of " + user.getUsername() + " as " + user.getAuthorities() + " to " + url);
                        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, REFRESH);
                    }
                }
            });
            filterChain.doFilter(request, response);
        } catch (ResponseStatusException ex) {
            var apiError = new APIError(ex.getStatus(), ex.getReason());
            logger.info("Return error from filter at " + url);
            apiError.intoResponse(response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (Routes.isEndpoint(path))
            return (Routes.AUTH + Routes.LOGIN).equals(path)
                    || (Routes.AUTH + Routes.LOGOUT).equals(path)
                    || (Routes.AUTH + Routes.REFRESH).equals(path);
        return true;
    }
}
