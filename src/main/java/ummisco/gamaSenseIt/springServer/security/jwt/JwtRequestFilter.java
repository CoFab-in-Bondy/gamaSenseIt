package ummisco.gamaSenseIt.springServer.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import ummisco.gamaSenseIt.springServer.security.UserDetailService;
import ummisco.gamaSenseIt.springServer.security.jwt.Jwt;
import ummisco.gamaSenseIt.springServer.security.jwt.JwtUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        Jwt jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                jwt = jwtUtils.parse(authHeader.substring(7));
                jwt.username();
            } catch (IllegalArgumentException e) {
                System.err.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.err.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String for request to " + request.getRequestURI());
        }

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var user = this.userDetailsService.loadUserByUsername(jwt.username());
            if (jwt.validate(user)) {
                System.out.println("AUTHORITIES : " + user.getAuthorities());
                var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }
}
