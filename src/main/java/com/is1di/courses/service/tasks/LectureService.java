package com.is1di.courses.service.tasks;

import com.is1di.courses.entity.Student;
import com.is1di.courses.entity.task.template.lecture.Lecture;
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
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {
    private final MongoOperations mongoOperations;
    private final MessageService messageService;
    private final TypeService typeService;
    private final StudentService studentService;
    private final TaskAnsweredService taskAnsweredService;
    private final CourseService courseService;


    public Lecture findById(ObjectId id) {
        return typeService.findById(id, EntityClassName.LECTURE, Lecture.class)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_NOT_FOUND)
                ));
    }

    public TaskAnswered completeLecture(ObjectId id, Student student) {
        Lecture l = findById(id);
        Student st = studentService.findByIdOrCreate(student);
        if (courseService.get(l.getCourseTitle()).getAllowedDirection().equals(st.getDirection())) {
            taskAnsweredService.getByIdStudent(id, st.getSub())
                    .ifPresent((o) -> {
                        throw new AlreadyExistsException(
                                messageService.getMessage(MessageMethod.TASK_ALREADY_FINISHED)
                        );
                    });
            if (l.getOpenedAt().isBefore(LocalDateTime.now())) {
                if (l.getClosedAt().isAfter(LocalDateTime.now())) {
                    return mongoOperations.save(new TaskAnswered(l.getId().toString(), l.getCourseTitle(), l.getSectionId(), LocalDateTime.now(), LocalDateTime.now(), null, st.getSub(), 100d));
                } else throw new TimeException(
                        messageService.getMessage(MessageMethod.TASK_ALREADY_FINISHED)
                );
            } else throw new TimeException(
                    messageService.getMessage(MessageMethod.TASK_NOT_STARTED)
            );
        }else throw new AccessDeniedException(
                messageService.getMessage(MessageMethod.TASK_ACCESS_DENIED)
        );
    }

    public Lecture getLecture(ObjectId id) {
        return typeService.findById(id, EntityClassName.LECTURE, Lecture.class)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_NOT_FOUND)
                ));
    }
}
