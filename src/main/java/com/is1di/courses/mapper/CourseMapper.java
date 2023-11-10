package com.is1di.courses.mapper;

import com.is1di.courses.dto.course.CourseDto;
import com.is1di.courses.entity.courses.Course;
import com.is1di.courses.entity.courses.Section;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {
    @Mapping(target = "sectionIds", source = "sections")
    public abstract CourseDto.Output toOutput(Course course);

    public String toCourse(Section section) {
        return section.getId().toString();
    }

    public ObjectId toId(String id) {
        return new ObjectId(id);
    }

    public String toStr(ObjectId id) {
        if (id != null)
            return id.toString();
        return null;
    }
}
