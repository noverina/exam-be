package porto.exam.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import porto.exam.dtos.ExamDataDTO;
import porto.exam.dtos.ExamSubmitDTO;
import porto.exam.dtos.ExamUpsertDTO;
import porto.exam.dtos.detail.ExamDataAnswerDTO;
import porto.exam.dtos.detail.ExamDataQuestionDTO;
import porto.exam.dtos.detail.ExamUpsertAnswerDTO;
import porto.exam.dtos.detail.ExamUpsertQuestionDTO;
import porto.exam.entities.*;
import porto.exam.enums.DeleteType;
import porto.exam.exceptions.BadLogicException;
import porto.exam.repositories.*;
import porto.exam.services.ExamService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

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
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void upsert(ExamUpsertDTO dto) {
        var examEntity = dto.isNew() ? new Exam() : examRepository.findById(dto.getExamId()).orElseThrow();
        examEntity.setType(dto.getType());

        if (ZonedDateTime.now(ZoneId.of("UTC")).isAfter(dto.getStartDate())) throw new BadLogicException("Exam has started, unable to make changes");

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

            var multipleCorrectAnswer = question.getAnswers().stream()
                    .filter(ExamUpsertAnswerDTO::isCorrect)
                    .limit(2)
                    .count() > 1;
            if (multipleCorrectAnswer) throw new BadLogicException("Multiple correct answers in question: " + question.getText());

            for (var answer : question.getAnswers()) {
                var answerEntity = answer.isNew() ? new Answer() : answerRepository.findById(answer.getAnswerId()).orElseThrow();
                answerEntity.setText(answer.getText());
                answerEntity.setIsCorrect(answer.isCorrect());
                answerEntity.setQuestion(questionEntity);
                answerRepository.save(answerEntity);
            }
        }

        for (var entityToDelete : dto.getFormDelete()) {
            if (entityToDelete.getType() == DeleteType.QUESTION) {
                questionRepository.deleteById(entityToDelete.getId());
                answerRepository.findByQuestionId(entityToDelete.getId()).ifPresent(
                        answers -> answerRepository.deleteAllById(
                                answers.stream()
                                .map(Answer::getId)
                                .toList()));
            }
            if (entityToDelete.getType() == DeleteType.ANSWER) answerRepository.deleteById(entityToDelete.getId());
        }
    }

    @Override
    public ExamUpsertDTO getUpsertExamData(Integer examId) {
        var exam = examRepository.findById(examId).orElseThrow();
        var dto = new ExamUpsertDTO(exam.getId(), exam.getCourseTeacher().getId(), exam.getType(), exam.getStartDate(), exam.getEndDate(), exam.getPassingGrade(), false);
        var dtoQuestions = new ArrayList<ExamUpsertQuestionDTO>();
        for (var question : questionRepository.findByExamId(examId).orElseThrow()) {
            var dtoQuestion = new ExamUpsertQuestionDTO(question.getId(), question.getText(), false);
            var dtoAnswers = new ArrayList<ExamUpsertAnswerDTO>();
            for (var answer : answerRepository.findByQuestionId(question.getId()).orElseThrow()) {
                var dtoAnswer = new ExamUpsertAnswerDTO(answer.getId(), answer.getText(), answer.getIsCorrect(), false);
                dtoAnswers.add(dtoAnswer);
            }
            dtoQuestion.setAnswers(dtoAnswers);
            dtoQuestions.add(dtoQuestion);
        }
        dto.setQuestions(dtoQuestions);
        return dto;
    }

    @Override
    @Transactional
    public void submit(ExamSubmitDTO dto) {
        var entity = studentExamRepository.findOneByStudentIdAndExamId(dto.getStudentId(), dto.getExamId()).orElse(new StudentExam());
        if (dto.isFinal()) entity.setSubmitDate(ZonedDateTime.now(ZoneId.of("UTC")));
        if (entity.getStudent() == null) entity.setStudent(userRepository.getReferenceById(dto.getStudentId()));
        if (entity.getExam() == null) entity.setExam(examRepository.getReferenceById(dto.getExamId()));
        studentExamRepository.save(entity);

        for (var selectedAnswer : dto.getFormSubmitSelected()) {
            var studentAnswerEntity = studentAnswerRepository.findOneByQuestionIdAndStudentExamId(selectedAnswer.getQuestionId(), entity.getId()).orElse(new StudentAnswer());
            studentAnswerEntity.setAnswer(answerRepository.getReferenceById(selectedAnswer.getSelectedAnswerId()));
            studentAnswerEntity.setQuestion(questionRepository.getReferenceById(selectedAnswer.getQuestionId()));
            studentAnswerEntity.setStudentExam(studentExamRepository.getReferenceById(entity.getId()));
            studentAnswerRepository.save(studentAnswerEntity);
        }
    }

    @Override
    public ExamDataDTO getExamData(Integer examId, Integer studentId) {
        var dto = studentExamRepository.getByExamAndStudent(examId, studentId).orElseThrow();
        var questions = questionRepository.findByExamId(examId);
        var questionsDTO = new ArrayList<ExamDataQuestionDTO>();

        if (questions.isPresent()) {
            for (var question: questions.get()) {
                var questionDTO = new ExamDataQuestionDTO(question.getId(), question.getText());
                studentAnswerRepository.findOneByQuestionIdAndStudentExamId(question.getId(), dto.getStudentExamId()).ifPresent(answer -> questionDTO.setSelectedAnswerId(answer.getAnswer().getId()));
                var answers = answerRepository.findByQuestionId(question.getId());
                var answersDTO = new ArrayList<ExamDataAnswerDTO>();

                if (answers.isPresent()) {
                    for (var answer : answers.get()) {
                        var answerDTO = ZonedDateTime.now(ZoneId.of("UTC")).isAfter(dto.getEndDate()) ? new ExamDataAnswerDTO(answer.getId(), answer.getText(), answer.getIsCorrect()) : new ExamDataAnswerDTO(answer.getId(), answer.getText());
                        answersDTO.add(answerDTO);
                    }

                    questionDTO.setAnswers(answersDTO);
                }

                questionsDTO.add(questionDTO);
            }
        }
        dto.setQuestions(questionsDTO);
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
