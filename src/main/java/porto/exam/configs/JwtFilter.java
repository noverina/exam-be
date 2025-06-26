package porto.exam.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import porto.exam.repositories.UserRepository;
import porto.exam.services.JwtService;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService service;
    @Autowired
    private UserRepository repo;

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {
        var header = req.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            var token = header.substring(7); // Remove "Bearer "
            var claims = jwtService.validateAndGetClaims(token);
            var username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // load full entity from db to populate auth context
                var user = repo.findByEmail(username).orElseThrow();
                var auth = new TokenBasedAuthentication(user);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(req, res);
    }
}
