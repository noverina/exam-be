package porto.exam.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import porto.exam.dtos.AuthDto;
import porto.exam.dtos.RegisterDto;
import porto.exam.entities.User;
import porto.exam.enums.Role;
import porto.exam.exceptions.BadLogicException;
import porto.exam.repositories.UserRepository;
import porto.exam.services.JwtService;
import porto.exam.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authManager;
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
    public String auth(AuthDto dto) {
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
        return jwtService.generateToken((UserDetails) auth.getPrincipal());
    }
}
