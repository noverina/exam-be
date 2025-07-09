package porto.exam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.HttpResponseDto;
import porto.exam.services.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @Operation(summary = "for HomeView; fetch course and its corresponding exam")
    @GetMapping
    public ResponseEntity<HttpResponseDto<?>> fetchByUser(@RequestParam String userId, @RequestParam String role, @Parameter(description = "IANA format") @RequestParam String timezone) {
        var result = service.fetchByUser(userId, role, timezone);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}
