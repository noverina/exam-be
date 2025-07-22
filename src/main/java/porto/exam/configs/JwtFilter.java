package porto.exam.configs;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
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
    protected boolean shouldNotFilter(HttpServletRequest req) {
        var path = req.getRequestURI();
        return req.getMethod().equalsIgnoreCase("OPTIONS")
                || path.startsWith("/auth")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {
        var headerToken = req.getHeader("Authorization");
        if (headerToken == null || !headerToken.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String token = "";
        if (headerToken.startsWith("Bearer ")) token = headerToken.substring(7);
        try {
            if (!token.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                var auth = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException e) {
            SecurityContextHolder.clearContext();
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (AccessDeniedException ae) {
            SecurityContextHolder.clearContext();
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(req, res);
    }

}
