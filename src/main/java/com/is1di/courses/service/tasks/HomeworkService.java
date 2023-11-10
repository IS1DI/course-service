package com.is1di.courses.service.tasks;

import com.is1di.courses.entity.Student;
import com.is1di.courses.entity.task.template.homework.HomeWork;
import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.exception.AccessDeniedException;
import com.is1di.courses.exception.AlreadyExistsException;
import com.is1di.courses.exception.NotFoundException;
import com.is1di.courses.exception.TimeException;
import com.is1di.courses.service.CourseService;
import com.is1di.courses.service.MessageService;
import com.is1di.courses.service.StudentService;
import com.is1di.courses.service.TypeService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HomeworkService {
    private final MongoOperations mongoOperations;
    private final StudentService studentService;
    private final CourseService courseService;
    private final TypeService typeService;
    private final MessageService messageService;
    private final TaskAnsweredService taskAnsweredService;


    public TaskAnswered uploadHomeWork(ObjectId taskId, Student student, String url) {
        HomeWork hw = findById(taskId);
        Student st = studentService.findByIdOrCreate(student);
        taskAnsweredService.getByIdStudent(taskId,st.getSub())
                .ifPresent((o) -> {throw new AlreadyExistsException(
                        messageService.getMessage(MessageMethod.TASK_ALREADY_FINISHED)
                );
                });
        if (courseService.get(hw.getCourseTitle()).getAllowedDirection().equals(st.getDirection())) {
            TaskAnswered taskAnswered = new TaskAnswered(taskId.toString(), hw.getCourseTitle(), hw.getSectionId(), LocalDateTime.now(), null,url, st.getSub(), null);
            if (hw.getClosedAt().isBefore(LocalDateTime.now())) {
                if (hw.getOpenedAt().isBefore(LocalDateTime.now())) {
                    return mongoOperations.save(taskAnswered);
                } else throw new TimeException(
                        messageService.getMessage(MessageMethod.TASK_NOT_STARTED)
                );
            } else throw new TimeException(
                    messageService.getMessage(MessageMethod.TASK_TIME_EXPIRED)
            );
        } else throw new AccessDeniedException(
                messageService.getMessage(MessageMethod.TASK_ACCESS_DENIED)
        );
    }

    public TaskAnswered rateHomeWork(ObjectId taskId, String sub, Double grade) {
        return Optional.ofNullable(mongoOperations.update(TaskAnswered.class)
                        .matching(Query.query(Criteria.where("taskId").is(taskId.toString()).and("studentId").is(sub)))
                        .apply(Update.update("finishedAt", LocalDateTime.now()).set("grade", grade))
                        .withOptions(FindAndModifyOptions.options().returnNew(true))
                        .findAndModifyValue())
                .orElseThrow(() -> new NotFoundException(
                                messageService.getMessage(MessageMethod.TASK_ANSWERED_NOT_FOUND)
                        )
                );
    }


    public HomeWork findById(ObjectId id) {
        return typeService.findById(id, EntityClassName.HOMEWORK, HomeWork.class)
                .orElseThrow(() -> new NotFoundException(
                                messageService.getMessage(MessageMethod.TASK_NOT_FOUND)
                        )
                );
    }
}
