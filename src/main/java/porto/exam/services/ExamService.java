package porto.exam.services;

import porto.exam.dtos.ExamQuestionDTO;
import porto.exam.dtos.ExamUpsertDTO;

import java.util.List;

public interface ExamService {
    List<ExamQuestionDTO> getQuestions (Integer examId);
    void save(ExamUpsertDTO dto);
    ExamUpsertDTO getUpdateData(Integer examId);
//    List<Integer> grade(Integer examId, Integer teacherId);
}
