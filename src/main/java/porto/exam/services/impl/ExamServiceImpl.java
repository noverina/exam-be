package porto.exam.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import porto.exam.dtos.*;
import porto.exam.dtos.detail.*;
import porto.exam.dtos.flat.FlatExamQnADto;
import porto.exam.dtos.flat.FlatGradeDto;
import porto.exam.dtos.flat.FlatUpsertPrefillDto;
import porto.exam.entities.*;
import porto.exam.enums.DeleteType;
import porto.exam.exceptions.BadLogicException;
import porto.exam.repositories.*;
import porto.exam.services.ExamService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExamServiceImpl implements ExamService {
    // region repo definition
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private CourseTeacherRepository courseTeacherRepository;
    @Autowired
    private StudentAnswerRepository studentAnswerRepository;
    @Autowired
    private StudentExamRepository studentExamRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ObjectMapper mapper;
    // endregion

    @Override
    @Transactional
    public void upsert(UpsertDto dto) {
        if (ZonedDateTime.now(ZoneId.of("UTC")).isAfter(dto.getStartDate())) throw new BadLogicException("Exam has started, unable to make changes");

        var examEntity = dto.isNew() ? new Exam(null, dto.getType(), dto.getPassingGrade(), dto.getStartDate(), dto.getEndDate(), false, courseTeacherRepository.getReferenceById(dto.getCourseTeacherId())) : examRepository.findById(dto.getExamId()).orElseThrow(EntityNotFoundException::new);
        examRepository.save(examEntity);

        for (var question : dto.getQuestions()) {
            var questionEntity = question.isNew() ? new Question(null, question.getText(), examEntity) : questionRepository.findById(question.getQuestionId()).orElseThrow(EntityNotFoundException::new);
            questionRepository.save(questionEntity);

            var multipleCorrectAnswer = question.getAnswers().stream()
                    .filter(UpsertAnswerDto::isCorrect)
                    .limit(2)
                    .count() > 1;
            if (multipleCorrectAnswer) throw new BadLogicException("Multiple correct answers in question: " + question.getText());

            for (var answer : question.getAnswers()) {
                var answerEntity = answer.isNew() ? new Answer(null, answer.getText(), answer.isCorrect(), questionEntity) : answerRepository.findById(answer.getAnswerId()).orElseThrow();
                answerRepository.save(answerEntity);
            }
        }

        // delete entity queued for deletion
        for (var entityToDelete : dto.getFormDelete()) {
            // cascade delete answer
            if (entityToDelete.getType() == DeleteType.QUESTION) {
                questionRepository.deleteById(entityToDelete.getEntityId());
                var answers = answerRepository.findByQuestionQuestionId(entityToDelete.getEntityId());
                answerRepository.deleteAllById(answers.stream().map(Answer::getAnswerId).toList());
            }
            if (entityToDelete.getType() == DeleteType.ANSWER) answerRepository.deleteById(entityToDelete.getEntityId());
        }
    }

    @Override
    public UpsertPrefillDto fetchExamUpsertPrefillData(String examId, String timezone) {
        var zone = ZoneId.of(timezone);
        var output = new UpsertPrefillDto();
        var groupedExams = examRepository.fetchUpsertPrefill(examId)
                .stream().collect(Collectors.groupingBy(FlatUpsertPrefillDto::getExamId, LinkedHashMap::new, Collectors.toList()));
        for (var groupedExam : groupedExams.entrySet()) {
            var type = groupedExam.getValue().getFirst().getType();
            var startDate = groupedExam.getValue().getFirst().getStartDate().withZoneSameInstant(zone);
            var endDate = groupedExam.getValue().getFirst().getEndDate().withZoneSameInstant(zone);
            var passingGrade = groupedExam.getValue().getFirst().getPassingGrade();

            var groupedQuestions = groupedExam.getValue().stream().collect(Collectors.groupingBy(FlatUpsertPrefillDto::getQuestionId, LinkedHashMap::new, Collectors.toList()));
            var questions = new ArrayList<UpsertPrefillQuestionDto>();
            for (var groupedQuestion : groupedQuestions.entrySet()) {
                var questionId = groupedQuestion.getValue().getFirst().getQuestionId();
                var text = groupedQuestion.getValue().getFirst().getQuestionText();

                var answers = groupedQuestion.getValue().stream().map(answer -> new UpsertPrefillAnswerDto(answer.getAnswerId(), answer.getAnswerText(), answer.isCorrect())).toList();

                questions.add(new UpsertPrefillQuestionDto(questionId, text, answers));
            }
            output = new UpsertPrefillDto(type, startDate, endDate, passingGrade, questions);
        }
        return output;
    }

    @Override
    @Transactional
    public void submit(SubmitDto dto) {
        var entity = studentExamRepository.findByStudentAndExam(dto.getStudentId(), dto.getExamId()).orElse(new StudentExam());
        if (entity.getSubmitDate() != null || entity.getGrade() != null) throw new BadLogicException("Finalized; can't make changes");
        if (dto.isFinal()) entity.setSubmitDate(ZonedDateTime.now(ZoneId.of("UTC")));
        entity.setStudent(userRepository.findById(dto.getStudentId()).orElseThrow());
        entity.setExam(examRepository.findById(dto.getExamId()).orElseThrow());
        studentExamRepository.save(entity);

        for (var choice : dto.getChoices()) {
            if (choice.getAnswerId() != null) {
                var studentAnswerEntity = studentAnswerRepository.findByQuestionAndStudentAndExam(choice.getQuestionId(), dto.getStudentId(), dto.getExamId()).orElse(new StudentAnswer());
                studentAnswerEntity.setAnswer(answerRepository.findById(choice.getAnswerId()).orElseThrow());
                studentAnswerEntity.setQuestion(questionRepository.findById(choice.getQuestionId()).orElseThrow());
                studentAnswerEntity.setStudent(userRepository.findById(dto.getStudentId()).orElseThrow());
                studentAnswerRepository.save(studentAnswerEntity);
            }
        }
    }

    @Override
    public ExamListDto fetchExamWithQnA(String examId, String studentId, String timezone) {
        var zone = ZoneId.of(timezone);
        var output = new ExamListDto();
        var groupedQuestions = examRepository.fetchExamWithQnA(examId, studentId).stream().collect(Collectors.groupingBy(FlatExamQnADto::getExamId, LinkedHashMap::new, Collectors.toList()));
        for (var groupedExam : groupedQuestions.entrySet()) {
            var examType = groupedExam.getValue().getFirst().getExamType();
            var courseName = groupedExam.getValue().getFirst().getCourseName();
            var endDate = groupedExam.getValue().getFirst().getEndDate().withZoneSameInstant(zone);
            var canShowCorrect = groupedExam.getValue().getFirst().getEndDate().isBefore(ZonedDateTime.now());
            var grade = groupedExam.getValue().getFirst().getGrade();
            var isFinal = groupedExam.getValue().getFirst().getSubmitDate() != null || groupedExam.getValue().getFirst().getGrade() != null;


            var groupedAnswers = groupedExam.getValue().stream().collect(Collectors.groupingBy(FlatExamQnADto::getQuestionId, LinkedHashMap::new, Collectors.toList()));
            var questions = new ArrayList<QuestionDto>();
            for (var groupedQuestion : groupedAnswers.entrySet()) {
                var questionId = groupedQuestion.getValue().getFirst().getQuestionId();
                var text = groupedQuestion.getValue().getFirst().getQuestionText();
                var selected = answerRepository.findIdByStudentAndQuestion(studentId, groupedQuestion.getValue().getFirst().getQuestionId());

                var answers = groupedQuestion.getValue().stream().map(answer -> new AnswerDto(answer.getAnswerId(), answer.getAnswerText(), canShowCorrect ? answer.getIsCorrect() : null)).toList();

                questions.add(new QuestionDto(questionId, selected, text, answers));
            }

            output = new ExamListDto(isFinal, examId, examType, courseName, endDate, grade, questions);
        }
        return output;
    }

    @Override
    @Transactional
    public void grade(String examId) {
        if (examRepository.findById(examId).orElseThrow().getEndDate().isAfter(ZonedDateTime.now())) throw new BadLogicException("Exam has not ended");

        var studentExams = studentExamRepository.findByExam(examId).orElseThrow(() -> new BadLogicException("No data to grade"));
        for (var studentExam : studentExams) {
            var answers = studentAnswerRepository.findByStudentAndExam(studentExam.getStudent().getUserId(), examId);
            var correctAnswers = answerRepository.findExamCorrectAnswers(examId);
            var correct = 0;

            for (var answer : answers.orElse(Collections.emptyList())) {
                if (correctAnswers.contains(answer.getAnswer())) correct++;
            }

            var grade = (int) Math.ceil(((float) correct / correctAnswers.size()) * 100);
            studentExam.setGrade(grade);
            studentExamRepository.save(studentExam);
        }
    }

    @Override
    public GradeDto fetchGradeData(String courseTeacherId, String examId) {
        var output = new GradeDto();
        var groupedStudents = examRepository.fetchGrade(examId, courseTeacherId).stream().collect(Collectors.groupingBy(FlatGradeDto::getExamId, LinkedHashMap::new, Collectors.toList()));
        for (var groupedStudent : groupedStudents.entrySet()) {
            var id = groupedStudent.getValue().getFirst().getExamId();
            var examType = groupedStudent.getValue().getFirst().getExamType();
            var courseName = groupedStudent.getValue().getFirst().getCourseName();
            var passingGrade = groupedStudent.getValue().getFirst().getPassingGrade();


            var passAmt = new AtomicInteger();
            var total = new AtomicInteger();
            var studentWithGrade = new AtomicInteger();
            var students = groupedStudent.getValue().stream()
                    .map(student -> {
                        Integer grade = student.getGrade();
                        if (grade != null) {
                            total.getAndAdd(student.getGrade());
                            studentWithGrade.getAndIncrement();
                        }

                        boolean passed = (grade != null && grade >= passingGrade);

                        if (passed) passAmt.getAndIncrement();

                        return new GradeStudentDto(
                                student.getStudentId(),
                                student.getStudentName(),
                                grade,
                                passed
                        );
                    })
                    .toList();
            var passRate =  (!students.isEmpty())
                    ? (Integer) passAmt.get() / students.size()
                    : 0;
            var avg =  (!students.isEmpty())
                    ? (Integer) total.get() / studentWithGrade.get()
                    : 0;

            output = new GradeDto(id, examType, courseName, passingGrade, passRate, passAmt.get(), avg, students);
        }
        return output;
    }
}
