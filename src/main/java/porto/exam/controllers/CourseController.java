package porto.exam.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import porto.exam.dtos.CourseListDTO;
import porto.exam.dtos.HttpResponseDTO;
import porto.exam.services.CourseService;

import java.util.List;

@RestController
@RequestMapping("/course")
@Slf4j
public class CourseController {
    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<HttpResponseDTO<?>> getByStudent( @RequestParam Integer studentId, @RequestParam String timezone) {
        try {
            var result = service.getByStudent(studentId, timezone);
            var httpRes = new HttpResponseDTO<List<CourseListDTO>>();
            httpRes.setIsError(false);
            httpRes.setMessage("");
            httpRes.setData(result);
            return new ResponseEntity<>(httpRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("/ | query failed | cause: {} | msg: {}", e.getCause(), e.getMessage());
            var httpRes = new HttpResponseDTO<String>();
            httpRes.setIsError(true);
            httpRes.setMessage("query failed: " + e.getMessage());
            return new ResponseEntity<>(httpRes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
