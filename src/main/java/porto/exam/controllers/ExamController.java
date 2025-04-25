package porto.exam.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.ExamQuestionDTO;
import porto.exam.dtos.ExamUpsertDTO;
import porto.exam.dtos.HttpResponseDTO;
import porto.exam.services.ExamService;

import java.util.List;

@RestController
@RequestMapping("/exam")
@Slf4j
public class ExamController {
    @Autowired
    private ExamService service;

    @GetMapping("/question")
    public ResponseEntity<HttpResponseDTO<?>> getQuestions(@RequestParam Integer examId) {
        try {
            var result = service.getQuestions(examId);
            var httpRes = new HttpResponseDTO<List<ExamQuestionDTO>>();
            httpRes.setIsError(false);
            httpRes.setMessage("");
            httpRes.setData(result);
            return new ResponseEntity<>(httpRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("/question | query failed | cause: {} | msg: {}", e.getCause(), e.getMessage());
            var httpRes = new HttpResponseDTO<String>();
            httpRes.setIsError(true);
            httpRes.setMessage("query failed: " + e.getMessage());
            return new ResponseEntity<>(httpRes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upsert")
    public ResponseEntity<HttpResponseDTO<?>> save(@RequestBody ExamUpsertDTO dto) {
        try {
            service.save(dto);
            var httpRes = new HttpResponseDTO<>();
            httpRes.setIsError(false);
            httpRes.setMessage("");
            return new ResponseEntity<>(httpRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("/upsert | save failed | cause: {} | msg: {}", e.getCause(), e.getMessage());
            var httpRes = new HttpResponseDTO<String>();
            httpRes.setIsError(true);
            httpRes.setMessage("save failed: " + e.getMessage());
            return new ResponseEntity<>(httpRes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upsert/data")
    public ResponseEntity<HttpResponseDTO<?>> getUpdateData(@RequestParam Integer examId) {
        try {
            var result = service.getUpdateData(examId);
            var httpRes = new HttpResponseDTO<ExamUpsertDTO>();
            httpRes.setIsError(false);
            httpRes.setMessage("");
            httpRes.setData(result);
            return new ResponseEntity<>(httpRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error("/upsert/data | query failed | cause: {} | msg: {}", e.getCause(), e.getMessage());
            var httpRes = new HttpResponseDTO<String>();
            httpRes.setIsError(true);
            httpRes.setMessage("query failed: " + e.getMessage());
            return new ResponseEntity<>(httpRes, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

