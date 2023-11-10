package com.is1di.courses.service;

import com.is1di.courses.entity.courses.Course;
import com.is1di.courses.entity.courses.Section;
import com.is1di.courses.exception.AlreadyExistsException;
import com.is1di.courses.exception.NotFoundException;
import com.is1di.courses.utils.MessageMethod;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Transactional
@RequiredArgsConstructor
@Service
public class SectionService {
    private final MongoOperations mongoOperations;
    private final MessageService messageService;
    private final CourseService courseService;
    private final TypeService typeService;

    public boolean exists(String courseTitle, ObjectId sectionId) {
        return mongoOperations.exists(Query.query(Criteria.where("title").is(courseTitle).and("sections")
                .elemMatch(Criteria.where("_id").is(sectionId))), Course.class);
    }

    public Section create(String courseTitle, Section section) {
            if(courseService.exists(courseTitle)) {
                section.setCourseTitle(courseTitle);
                return mongoOperations.save(section);
            }
            throw new NotFoundException(
                    messageService.getMessage(MessageMethod.COURSE_NOT_FOUND,courseTitle)
            );
    }

    public Section get(ObjectId id) {
        return Optional.ofNullable(mongoOperations.findById(id,Section.class))
                .orElseThrow(() -> new NotFoundException(
                        messageService.getMessage(MessageMethod.SECTION_NOT_FOUND, id.toString())
                ));
    }
    
    public Section update(ObjectId id, Section sectionUpd) {
        Section section = get(id);
        section.setTitle(sectionUpd.getTitle() != null ? sectionUpd.getTitle() : section.getTitle());
        section.setDescription(sectionUpd.getDescription() != null ? sectionUpd.getDescription() : section.getDescription());
        return mongoOperations.update(Section.class)
                .matching(Criteria.where("_id").is(id))
                .apply(Update.update("title",section.getTitle()).set("description",section.getDescription()))
                .withOptions(FindAndModifyOptions.options().returnNew(true))
                .findAndModifyValue();
    }
}
