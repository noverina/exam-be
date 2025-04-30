package porto.exam.services;

import porto.exam.dtos.ExamDataDTO;
import porto.exam.dtos.ExamSubmitDTO;
import porto.exam.dtos.ExamUpsertDTO;

public interface ExamService {
    void upsert(ExamUpsertDTO dto);
    ExamUpsertDTO getUpsertExamData(Integer examId);
    void submit (ExamSubmitDTO dto);
    ExamDataDTO getExamData(Integer examId, Integer studentId);
//    List<Integer> grade(Integer examId, Integer teacherId);
}
