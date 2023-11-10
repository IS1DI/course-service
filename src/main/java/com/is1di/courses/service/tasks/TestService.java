package com.is1di.courses.service.tasks;

import com.is1di.courses.entity.Student;
import com.is1di.courses.entity.task.template.testTemplate.Answer;
import com.is1di.courses.entity.task.template.testTemplate.Question;
import com.is1di.courses.entity.task.template.testTemplate.Test;
import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.exception.*;
import com.is1di.courses.service.*;
import com.is1di.courses.utils.EntityClassName;
import com.is1di.courses.utils.MessageMethod;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class TestService {
    private final MongoOperations mongoOperations;
    private final MessageService messageService;
    private final TypeService typeService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TaskAnsweredService taskAnsweredService;


    public Test update(ObjectId id, Test test) {
        Test t = Optional.ofNullable(mongoOperations.findById(id, Test.class))
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_NOT_FOUND, id.toString()))
                );
        if (test.getOpenedAt().isAfter(LocalDateTime.now()) && test.getClosedAt().isAfter(test.getOpenedAt()) && test.getDuration() < test.getClosedAt().getMinute() - (test.getOpenedAt().getMinute())
                && test.getDuration() > 5) {
            t.setTitle(test.getTitle() != null ? test.getTitle() : t.getTitle());
            t.setDescription(test.getDescription() != null ? test.getDescription() : t.getDescription());
            t.setOpenedAt(test.getOpenedAt() != null ? test.getOpenedAt() : t.getOpenedAt());
            t.setClosedAt(test.getClosedAt() != null ? test.getClosedAt() : t.getClosedAt());
            t.setDuration(test.getDuration() != null ? test.getDuration() : t.getDuration());
            t.setIsPrivate(test.getIsPrivate() != null ? test.getIsPrivate() : t.getIsPrivate());
            return t;
        } else throw new TimeException(
                messageService.getMessage(MessageMethod.TIME_MUST_BE_AFTER)
        );

    }

    public Test findById(ObjectId id) {
        return typeService.findById(id, EntityClassName.TEST, Test.class)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_NOT_FOUND, id.toString())
                ));
    }

    public boolean exists(ObjectId id) {
        return typeService.existsById(id, EntityClassName.TEST, Test.class);
    }

    public Test startTest(ObjectId id, Student student) {
        Test test = findById(id);
        Student stud = studentService.findByIdOrCreate(student);
        if (courseService.get(test.getCourseTitle()).getAllowedDirection().equals(stud.getDirection())) {
            stud.getTasksAnswered().stream().filter(t -> new ObjectId(t.getTaskId()).equals(id)).findFirst()
                    .ifPresent(
                            (started) -> {
                                throw new AlreadyExistsException(
                                        messageService.getMessage(
                                                MessageMethod.ALREADY_STARTED_TEST, test.getTitle()
                                        )
                                );
                            });
            mongoOperations.save(new TaskAnswered(test.getId().toString(),
                    test.getCourseTitle(),
                    test.getSectionId(),
                    LocalDateTime.now(),
                    null,
                    null,
                    student.getSub(),
                    null));

            return test;
        } else throw new AccessDeniedException(
                messageService.getMessage(MessageMethod.TASK_ACCESS_DENIED)
        );
    }

    public Question addQuestion(ObjectId id, Question question) {
        if (exists(id)) {
            question.setTestId(id);
            return mongoOperations.save(question);
        } else throw new NotFoundException(
                messageService.getMessage(MessageMethod.TASK_NOT_FOUND, id.toString())
        );
    }


    public Question findQuestionById(ObjectId id, ObjectId qId) {
        return findById(id).getQuestions().stream().filter(it -> it.getId().equals(qId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.QUESTION_NOT_FOUND)
                ));
    }

    public TaskAnswered finishTest(ObjectId id, List<Question> questions, String sub) {
        Test test = findById(id);
        TaskAnswered taskAnswered = taskAnsweredService.getByIdStudentSafely(id, sub);
        if (taskAnswered.getFinishedAt() != null) {
            throw new AccessDeniedException(
                    messageService.getMessage(
                            MessageMethod.ALREADY_FINISHED_TEST
                    )
            );
        }
        if (taskAnswered.getStartedAt().plusMinutes(test.getDuration()).isAfter(LocalDateTime.now()))
            throw new TimeException(
                    messageService.getMessage(MessageMethod.TEST_TIMED_OUT)
            );
        double grade = 0d;
        int maxGrade = test.getQuestions().size();
        if(maxGrade == 0)
            throw new ZeroException(
                    messageService.getMessage(MessageMethod.ZERO_RIGHT_ANSWERS_IN_TEST)
            );
        for (Question origQuestion : test.getQuestions()) {

            AtomicInteger rightAnswered = new AtomicInteger();
            AtomicInteger countAnswersIsRight = new AtomicInteger();
            questions.stream().filter(answeredQ -> answeredQ.getId().equals(origQuestion.getId()))
                    .findFirst()
                    .ifPresent((question) -> {

                        origQuestion.getAnswers().stream().filter(Answer::isRight).forEach(answ -> {
                            question.getAnswers().stream().filter(answeredAnswer -> answeredAnswer.getAnswer().equals(answ.getAnswer()))
                                    .findFirst().ifPresentOrElse(a -> {
                                                rightAnswered.addAndGet(1);
                                                countAnswersIsRight.addAndGet(1);
                                            },
                                            () -> countAnswersIsRight.addAndGet(1));
                        });
                    });
            if(countAnswersIsRight.get() != 0)
                grade += (double) rightAnswered.get() / countAnswersIsRight.get();
        }
        return mongoOperations.update(TaskAnswered.class)
                .matching(Query.query(Criteria.where("taskId").is(taskAnswered.getTaskId()).and("studentId").is(sub)))
                .apply(Update.update("finishedAt",LocalDateTime.now()).set("grade",grade/maxGrade))
                .withOptions(FindAndModifyOptions.options().returnNew(true))
                .findAndModifyValue();
    }

    public void removeQuestion(ObjectId id, ObjectId questionId) {
        if (mongoOperations.remove(Question.class)
                .matching(Query.query(Criteria.where("_id").is(questionId).and("testId").is(id)))
                .one()
                .getDeletedCount() > 0)
            return;
        throw new NotFoundException(
                messageService.getMessage(MessageMethod.QUESTION_NOT_FOUND)
        );
    }
}
