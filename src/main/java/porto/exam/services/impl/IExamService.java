package porto.exam.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import porto.exam.dtos.ExamQuestionDTO;
import porto.exam.dtos.ExamUpsertDTO;
import porto.exam.dtos.detail.ExamQuestionAnswerDTO;
import porto.exam.dtos.detail.ExamUpsertAnswerDTO;
import porto.exam.dtos.detail.ExamUpsertQuestionDTO;
import porto.exam.entities.Answer;
import porto.exam.entities.Exam;
import porto.exam.entities.Question;
import porto.exam.exceptions.BadLogicException;
import porto.exam.repositories.*;
import porto.exam.services.ExamService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IExamService implements ExamService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private StudentAnswerRepository studentAnswerRepository;
    @Autowired
    private StudentExamRepository studentExamRepository;
    @Autowired
    private CourseTeacherRepository courseTeacherRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ExamQuestionDTO> getQuestions(Integer examId) {
        var questions = questionRepository.findByExamId(examId);
        var questionList = new ArrayList<ExamQuestionDTO>();
        for (var question : questions) {
            var dtoQuestion = new ExamQuestionDTO(question.getId(), question.getText());
            var answerList = new ArrayList<ExamQuestionAnswerDTO>();
            for (var answer : answerRepository.findByQuestionId(question.getId())) {
                var dtoAnswer = new ExamQuestionAnswerDTO(answer.getId(), answer.getText());
                answerList.add(dtoAnswer);
            }
            dtoQuestion.setAnswers(answerList);
            questionList.add(dtoQuestion);
        }
        return questionList;
    }

    @Override
    @Transactional
    public void save(ExamUpsertDTO dto) {
        var examEntity = dto.isNew() ? new Exam() : examRepository.findById(dto.getExamId()).orElseThrow();
        examEntity.setType(dto.getType());
        //validation to make sure change isn't made if exam is ongoing/finished
        if (ZonedDateTime.now().isAfter(examEntity.getStartDate())) throw new BadLogicException(400, "Exam has started, unable to make changes");
        examEntity.setStartDate(dto.getStartDate());
        examEntity.setEndDate(dto.getEndDate());
        examEntity.setPassingGrade(dto.getPassingGrade());
        examEntity.setCourseTeacher(courseTeacherRepository.getReferenceById(dto.getCourseTeacherId()));
        examRepository.save(examEntity);
        for (var question : dto.getQuestions()) {
            var questionEntity = question.isNew() ? new Question() : questionRepository.findById(question.getQuestionId()).orElseThrow();
            questionEntity.setText(question.getText());
            questionEntity.setExam(examEntity);
            questionRepository.save(questionEntity);
            // validation to make sure a question can only have one correct answer
            var multipleCorrectAnswer = question.getAnswers().stream()
                    .filter(ExamUpsertAnswerDTO::isCorrect)
                    .limit(2)
                    .count() > 1;
            if (multipleCorrectAnswer) throw new BadLogicException(400, "Multiple correct answers in question: " + question.getText());
            for (var answer : question.getAnswers()) {
                var answerEntity = answer.isNew() ? new Answer() : answerRepository.findById(answer.getAnswerId()).orElseThrow();
                answerEntity.setText(answer.getText());
                answerEntity.setIsCorrect(answer.isCorrect());
                answerEntity.setQuestion(questionEntity);
                answerRepository.save(answerEntity);
            }
        }
    }

    @Override
    public ExamUpsertDTO getUpdateData(Integer examId) {
        var exam = examRepository.findById(examId).orElseThrow();
        var dto = new ExamUpsertDTO(exam.getId(), exam.getCourseTeacher().getId(), exam.getType(), exam.getStartDate(), exam.getEndDate(), exam.getPassingGrade(), false);
        var dtoQuestions = new ArrayList<ExamUpsertQuestionDTO>();
        var questionEntities = questionRepository.findByExamId(examId);
        for (var question : questionEntities) {
            var dtoQuestion = new ExamUpsertQuestionDTO(question.getId(), question.getText(), false);
            var dtoAnswers = new ArrayList<ExamUpsertAnswerDTO>();
            for (var answer : answerRepository.findByQuestionId(question.getId())) {
                var dtoAnswer = new ExamUpsertAnswerDTO(answer.getId(), answer.getText(), answer.getIsCorrect(), false);
                dtoAnswers.add(dtoAnswer);
            }
            dtoQuestion.setAnswers(dtoAnswers);
            dtoQuestions.add(dtoQuestion);
        }
        dto.setQuestions(dtoQuestions);
        return dto;
    }

//    @Override
//    public List<Integer> grade(Integer courseTeacherId, Integer examId) {
//        var studentAnswers = studentAnswerRepository.getByCourseTeacherAndExam(courseTeacherId, examId);
//        var grade = 0;
//        for (var studentAnswer : studentAnswers) {
//            if (studentAnswer.getAnswer().getIsCorrect()) {
//                grade++;
//            }
//        }
//        return List.of();
//    }
}
