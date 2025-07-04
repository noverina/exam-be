package porto.exam.configs;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
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
    private final AuthenticationEntryPoint entryPoint;

    public JwtFilter(@Lazy AuthenticationEntryPoint entryPoint) {
        this.entryPoint = entryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, @NonNull HttpServletResponse res, @NonNull FilterChain chain)
            throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        var path = req.getRequestURI();
        if (path.startsWith("/auth") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) {
            chain.doFilter(req, res);
            return;
        }

        var header = req.getHeader("Authorization");

//        if (header == null || !header.startsWith("Bearer ")) {
//            throw new InsufficientAuthenticationException("Bearer token missing");
//        }

        try {
            var token = header.substring(7); // remove "Bearer "
            var claims = jwtService.validateAndGetClaims(token);

            var username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // load full entity from db to populate auth context
                var user = repo.findByEmail(username).orElseThrow();
                var auth = new TokenBasedAuthentication(user);
                SecurityContextHolder.getContext().setAuthentication(auth);
                var teacherOnly = req.getRequestURI().startsWith("/exam") || req.getRequestURI().startsWith("/exam/data") || req.getRequestURI().startsWith("/exam/grade/");
                if (teacherOnly && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
                    throw new AccessDeniedException("Teacher role required");
                }
                var studentOnly = req.getRequestURI().startsWith("/exam/answer");
                if (studentOnly && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
                    throw new AccessDeniedException("Student role required");
                }
            }
        } catch (JwtException | AccessDeniedException e) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }


        chain.doFilter(req, res);
    }
}
