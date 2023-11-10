package com.is1di.courses.mapper;

import com.is1di.courses.dto.course.SectionDto;
import com.is1di.courses.entity.courses.Section;
import com.is1di.courses.entity.task.template.Task;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class SectionMapper {
    public abstract Section toEntity(SectionDto.Create dto);

    public abstract SectionDto.Output toOutput(Section section);

    public String toOut(Task task) {
        return task.getId().toString();
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
