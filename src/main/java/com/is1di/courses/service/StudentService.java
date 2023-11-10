package com.is1di.courses.service;

import com.is1di.courses.entity.Student;
import com.is1di.courses.utils.EntityClassName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class StudentService {
    private final MongoOperations mongoOperations;
    private final TypeService typeService;

    public boolean exists(String sub) {
        return typeService.exists(Query.query(Criteria.where("sub").is(sub)), EntityClassName.STUDENT, Student.class);
    }

    public Student findByIdOrCreate( Student student) {
        if(exists(student.getSub())) {
            return mongoOperations.findOne(Query.query(Criteria.where("sub").is(student.getSub())), Student.class,EntityClassName.STUDENT);
        } else {
            return create(student);
        }
    }

    public Student create(Student student) {
        return mongoOperations.save(student);
    }

}
