package porto.exam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import porto.exam.dtos.CourseListDTO;
import porto.exam.dtos.HttpResponseDTO;
import porto.exam.exceptions.BadLogicException;
import porto.exam.services.CourseService;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService service;

    @Operation(summary="get course + exam")
    @GetMapping
    public ResponseEntity<HttpResponseDTO<?>> getByStudent( @RequestParam Integer studentId, @RequestParam String timezone) {
        var result = service.getByStudent(studentId, timezone);
        var httpRes = new HttpResponseDTO<List<CourseListDTO>>();
        httpRes.setIsError(false);
        httpRes.setMessage("");
        httpRes.setData(result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    //TODO delete later this is for testing error handling
    @GetMapping("/error")
    public ResponseEntity<HttpResponseDTO<?>> errorTest() throws Exception {
        throw new BadLogicException("test bad logic error");
    }
}
