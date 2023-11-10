package com.is1di.courses.service;

import com.is1di.courses.entity.Student;
import com.is1di.courses.entity.courses.Course;
import com.is1di.courses.exception.NotFoundException;
import com.is1di.courses.utils.EntityClassName;
import com.is1di.courses.utils.MessageMethod;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final MongoOperations mongoOperations;
    private final TypeService typeService;
    private final MessageService messageService;
    public Course create(Course course) {
        return mongoOperations.save(course);
    }

    public Course get(String courseTitle) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("title").is(courseTitle)), Course.class))
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.COURSE_NOT_FOUND)
                ));
    }
    public boolean exists(String courseTitle) {
        return mongoOperations.exists(Query.query(Criteria.where("title").is(courseTitle)),Course.class);
    }

    public boolean exists(String courseTitle,String teacherId) {
        return mongoOperations.exists(Query.query(Criteria.where("title").is(courseTitle).and("teacherId").is(teacherId)),Course.class);
    }

    public Page<Course> getPageStudent(int p, int l,String direction) {
        return typeService.getPage(p,l, Query.query(Criteria.where("allowedDirection").is(direction)), EntityClassName.COURSE, Course.class);
    }

    public Page<Course> getPageTeacher(int p, int l, String sub) {
        Page<Course> pa =  typeService.getPage(p,l,Query.query(Criteria.where("teacherId").is(sub)),EntityClassName.COURSE,Course.class);
        return pa;
    }

    public Page<Student> getPageStudentsByCourse(int p, int l, String courseTitle) {
        return typeService.getPage(p,l,Query.query(Criteria.where("direction").is(get(courseTitle).getAllowedDirection())),EntityClassName.STUDENT, Student.class);
    }
}
