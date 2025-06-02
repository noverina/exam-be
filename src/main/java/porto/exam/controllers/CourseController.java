package porto.exam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import porto.exam.dtos.HttpResponseDto;
import porto.exam.services.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @Operation(summary="for HomeView; fetch course and its corresponding exam")
    @GetMapping
    public ResponseEntity<HttpResponseDto<?>> fetchByStudent(@RequestParam String studentId, @Parameter(description = "IANA format") @RequestParam String timezone) {
        var result = service.fetchByStudent(studentId, timezone);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}
