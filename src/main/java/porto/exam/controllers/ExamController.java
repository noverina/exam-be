package porto.exam.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import porto.exam.dtos.HttpResponseDto;
import porto.exam.dtos.SubmitDto;
import porto.exam.dtos.UpsertDto;
import porto.exam.services.ExamService;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService service;

    @Operation(summary = "for UpsertView; upsert exam")
    @PostMapping
    public ResponseEntity<HttpResponseDto<?>> upsert(@RequestBody UpsertDto dto) {
        service.upsert(dto);
        var httpRes = new HttpResponseDto<>(false, "", null);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary = "for UpsertView; get prefill (update) data for form")
    @GetMapping("/data")
    public ResponseEntity<HttpResponseDto<?>> getUpsertData(@RequestParam String examId, @Parameter(description = "IANA format") @RequestParam String timezone) {
        var result = service.fetchExamUpsertPrefillData(examId, timezone);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary = "for ExamView; submit (final)/save (temp) selected answer of question")
    @PostMapping("/answer")
    public ResponseEntity<HttpResponseDto<?>> submitAnswer(@RequestBody SubmitDto dto) {
        service.submit(dto);
        var httpRes = new HttpResponseDto<>(false, "", null);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary = "for ExamView; get prefill (update) data for form")
    @GetMapping("/answer/data")
    public ResponseEntity<HttpResponseDto<?>> getExamData(@RequestParam String examId, @RequestParam String studentId, @Parameter(description = "IANA format") @RequestParam String timezone) {
        var result = service.fetchExamWithQnA(examId, studentId, timezone);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary = "for HomeView; grade exam")
    @GetMapping("/grade")
    public ResponseEntity<HttpResponseDto<?>> grade(@RequestParam String examId) {
        service.grade(examId);
        var httpRes = new HttpResponseDto<>(false, "", null);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }

    @Operation(summary = "for GradeView; fetch exam grade detail")
    @GetMapping("/grade/data")
    public ResponseEntity<HttpResponseDto<?>> fetchGrade(@RequestParam String courseTeacherId, @RequestParam String examId) {
        service.grade(examId);
        var result = service.fetchGradeData(courseTeacherId, examId);
        var httpRes = new HttpResponseDto<>(false, "", result);
        return new ResponseEntity<>(httpRes, HttpStatus.OK);
    }
}

