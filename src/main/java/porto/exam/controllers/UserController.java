package porto.exam.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.AuthDto;
import porto.exam.dtos.HttpResponseDto;
import porto.exam.dtos.RegisterDto;
import porto.exam.services.UserService;

@RestController
@RequestMapping("/auth")

public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/register")
    public ResponseEntity<HttpResponseDto<?>> register(@RequestBody RegisterDto dto) {
        service.register(dto);
        var httpRes = new HttpResponseDto<>(false, "", null);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpResponseDto<String>> auth(@RequestBody AuthDto dto) {
        var result = service.auth(dto);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}
