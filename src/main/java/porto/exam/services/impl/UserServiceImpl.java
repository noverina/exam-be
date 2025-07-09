package porto.exam.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import porto.exam.dtos.AuthDto;
import porto.exam.dtos.RegisterDto;
import porto.exam.dtos.TokenDto;
import porto.exam.dtos.UserDto;
import porto.exam.entities.Token;
import porto.exam.entities.User;
import porto.exam.enums.Role;
import porto.exam.exceptions.BadLogicException;
import porto.exam.repositories.TokenRepository;
import porto.exam.repositories.UserRepository;
import porto.exam.services.JwtService;
import porto.exam.services.UserService;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private TokenRepository tokenRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authManager;
    @Value("${jwt.access.expiration}")
    private long refreshExpiration;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void register(RegisterDto dto) {
        var entity = new User();
        entity.setEmail(dto.getEmail());
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setName(dto.getName());
        entity.setRole(Role.valueOf(dto.getRole()));
        repo.save(entity);
    }

    @Override
    public TokenDto auth(AuthDto dto) {
        Authentication auth = null;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadLogicException("Invalid credentials");
        }

        if (auth == null) throw new RuntimeException("Unable to authenticate into Spring Security");
        SecurityContextHolder.getContext().setAuthentication(auth);

        var access = jwtService.generateToken(dto.getEmail(), "access");
        var refresh = jwtService.generateToken(dto.getEmail(), "refresh");
        var user = repo.findByEmail(((UserDetails) auth.getPrincipal()).getUsername()).orElseThrow();
        var entity = new Token();
        entity.setUser(user);
        entity.setToken(refresh);
        var expirationUnix = Instant.now().getEpochSecond() + refreshExpiration;
        entity.setExpiresIn(expirationUnix);
        tokenRepo.save(entity);

        return new TokenDto(access, refresh);
    }

    @Override
    public String refresh(String token) {
        var entity = tokenRepo.findByTokenAndExpiresInAfter(token, Instant.now().getEpochSecond())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        return jwtService.generateToken(entity.getUser().getEmail(), "refresh");
    }

    @Override
    public void invalidate(String token) {
        tokenRepo.findByToken(token).ifPresent(tokenRepo::delete);
    }

    @Override
    public UserDto getUserData(String token) {
        var claims = jwtService.validateAndGetClaims(token);
        var entity = repo.findByEmail(claims.getSubject()).orElseThrow();
        return new UserDto(entity.getName(), entity.getUserId(), entity.getRole().name());
    }
}
