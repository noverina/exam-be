package porto.exam.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.AuthDto;
import porto.exam.dtos.HttpResponseDto;
import porto.exam.dtos.RegisterDto;
import porto.exam.dtos.UserDto;
import porto.exam.services.UserService;

@RestController
@RequestMapping("/auth")

public class UserController {
    @Autowired
    private UserService service;
    @Value("${jwt.refresh.expiration}")
    private long expiration;

    @PostMapping("/register")
    public ResponseEntity<HttpResponseDto<?>> register(@RequestBody RegisterDto dto) {
        service.register(dto);
        var httpRes = new HttpResponseDto<>(false, "", null);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpResponseDto<String>> auth(@RequestBody AuthDto dto, HttpServletResponse res) {
        var token = service.auth(dto);
        var cookie = ResponseCookie.from("token", token.getRefresh())
                .httpOnly(true)
                //.secure(true)
                .path("/auth")
                .maxAge(expiration)
                .sameSite("Lax")
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        var httpRes = new HttpResponseDto<>(false, "", token.getAccess());
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<HttpResponseDto<String>> refresh(HttpServletRequest req, @CookieValue(name = "token", required = false) String token) {
        if (token == null) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var accessToken = service.refresh(token);
        var httpRes = new HttpResponseDto<>(false, "", accessToken);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<HttpResponseDto<UserDto>> getUserData(HttpServletRequest req) {
        var token = req.getHeader("Authorization");
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        var output = service.getUserData(token.substring(7));
        var httpRes = new HttpResponseDto<>(false, "", output);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @GetMapping("/invalidate")
    public ResponseEntity<Void> logout(@CookieValue(name = "token", required = false) String token, HttpServletResponse res) {
        if (token == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        service.invalidate(token);
        var cleared = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/auth")
                .maxAge(0)
                .build();
        res.addHeader(HttpHeaders.SET_COOKIE, cleared.toString());
        return ResponseEntity.noContent().build();
    }
}
