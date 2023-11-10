package com.is1di.courses.service.tasks;

import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.exception.NotFoundException;
import com.is1di.courses.service.MessageService;
import com.is1di.courses.service.TypeService;
import com.is1di.courses.utils.EntityClassName;
import com.is1di.courses.utils.MessageMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskAnsweredService {
    private final MongoOperations mongoOperations;
    private final MessageService messageService;
    private final TypeService typeService;

    public TaskAnswered getByIdStudentSafely(ObjectId taskId, String sub) {
        return getByIdStudent(taskId, sub)
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_ANSWERED_NOT_FOUND)
                ));
    }

    public Optional<TaskAnswered> getByIdStudent(ObjectId taskId, String sub) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("taskId").is(taskId.toString()).and("studentId").is(sub)), TaskAnswered.class));
    }

    public Page<TaskAnswered> getPageByStudent(int p, int l, String sub) {
        return typeService.getPage(p, l, Query.query(Criteria.where("studentId").is(sub)), EntityClassName.TASK_ANSWERED, TaskAnswered.class);
    }

    public double progressByTask(ObjectId taskId, String sub) {
        MatchOperation m = Aggregation.match(Criteria.where("taskId").is(taskId.toString()).and("studentId").is(sub));
        CountOperation c = Aggregation.count().as("count");
        GroupOperation g = Aggregation.group("sub").sum("grade").as("grade");
        return Optional.ofNullable(mongoOperations.aggregate(Aggregation.newAggregation(TaskAnswered.class,m,c,g),EntityClassName.TASK_ANSWERED, AggregateOutput.class)
                .getUniqueMappedResult())
                .orElse(new AggregateOutput(0L,0d)).calculate();
    }

    public double progressByCourse(String courseTile, String sub) {
        long all = mongoOperations.count(Query.query(Criteria.where("courseTitle").is(courseTile)),EntityClassName.COURSE);
        long answers = mongoOperations.count(Query.query(Criteria.where("courseTitle").is(courseTile).and("studentId").is(sub)),EntityClassName.TASK_ANSWERED);
        if(all!= 0) {
            return 100 * (double) answers / all;
        }
        return 0d;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static final class AggregateOutput {
        private Long count;
        private Double grade;
        public Double calculate() {
            if(count != null && count != 0) {
                return grade/count;
            }else return 0d;
        }
    }
}
