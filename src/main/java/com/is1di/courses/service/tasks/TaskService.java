package com.is1di.courses.service.tasks;

import com.is1di.courses.entity.TaskFull;
import com.is1di.courses.entity.courses.Section;
import com.is1di.courses.entity.task.template.Task;
import com.is1di.courses.exception.NotFoundException;
import com.is1di.courses.service.MessageService;
import com.is1di.courses.service.SectionService;
import com.is1di.courses.utils.MessageMethod;
import jakarta.validation.constraints.AssertFalse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final MongoOperations mongoOperations;
    private final SectionService sectionService;
    private final MessageService messageService;

    public <T extends Task> T create(ObjectId sectionId,T task) {
        Section section = sectionService.get(sectionId);
        task.setSectionId(sectionId);
        task.setCourseTitle(section.getCourseTitle());
        return mongoOperations.save(task);
    }

    public Task get(ObjectId id) {
        return mongoOperations.findById(id, Task.class);
    }

    public TaskFull getFull(ObjectId id) {
        return Optional.ofNullable(mongoOperations.findOne(Query.query(Criteria.where("_id").is(id)),TaskFull.class))
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.TASK_NOT_FOUND)
                ));
    }
}
