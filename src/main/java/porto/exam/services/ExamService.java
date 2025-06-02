package porto.exam.services;

import porto.exam.dtos.*;

public interface ExamService {
    void upsert(UpsertDto dto);
    UpsertPrefillDto fetchExamUpsertPrefillData(String examId, String timezone);
    void submit (SubmitDto dto);
    ExamListDto fetchExamWithQnA(String examId, String studentId, String timezone);
    void grade(String examId);
    GradeDto fetchGradeData(String courseTeacherId, String examId);
}
