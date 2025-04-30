package porto.exam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.ExamSubmitDTO;
import porto.exam.dtos.ExamUpsertDTO;
import porto.exam.dtos.HttpResponseDTO;
import porto.exam.services.ExamService;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService service;

    @Operation(summary="upsert exam")
    @PostMapping
    public ResponseEntity<HttpResponseDTO<?>> upsert(@RequestBody ExamUpsertDTO dto) {
        service.upsert(dto);
        var httpRes = new HttpResponseDTO<>();
        httpRes.setIsError(false);
        httpRes.setMessage("");
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary="get upsert exam data (for update prefill)")
    @GetMapping("/data")
    public ResponseEntity<HttpResponseDTO<?>> getUpsertExamData(@RequestParam Integer examId) {
        var result = service.getUpsertExamData(examId);
        var httpRes = new HttpResponseDTO<ExamUpsertDTO>();
        httpRes.setIsError(false);
        httpRes.setMessage("");
        httpRes.setData(result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary="submit/save answer of questions in exam")
    @PostMapping("/answer")
    public ResponseEntity<HttpResponseDTO<?>> submitAnswer(@RequestBody ExamSubmitDTO dto) {
        service.submit(dto);
        var httpRes = new HttpResponseDTO<>();
        httpRes.setIsError(false);
        httpRes.setMessage("");
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary="get exam data (for update prefill)")
    @GetMapping("/answer/data")
    public ResponseEntity<HttpResponseDTO<?>> getExamData(@RequestParam Integer examId, @RequestParam Integer studentId) {
        var result = service.getExamData(examId, studentId);
        var httpRes = new HttpResponseDTO<>();
        httpRes.setIsError(false);
        httpRes.setMessage("");
        httpRes.setData(result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

//    @GetMapping("/grade")
//    public ResponseEntity<HttpResponseDTO<?>> grade(@RequestParam Integer examId, @RequestParam Integer courseTeacherId) {
//        try {
//            var result = service.grade(examId, courseTeacherId);
//            var httpRes = new HttpResponseDTO<List<Integer>>();
//            httpRes.setIsError(false);
//            httpRes.setMessage("");
//            httpRes.setData(result);
//            return new ResponseEntity<>(httpRes, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("query failed | cause: {} | msg: {}", e.getCause(), e.getMessage());
//            var httpRes = new HttpResponseDTO<String>();
//            httpRes.setIsError(false);
//            httpRes.setMessage("");
//            httpRes.setData(e.getMessage());
//            return new ResponseEntity<>(httpRes, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}

