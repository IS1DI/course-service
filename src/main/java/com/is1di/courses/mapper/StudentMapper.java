package com.is1di.courses.mapper;

import com.is1di.courses.dto.StudentDto;
import com.is1di.courses.entity.Student;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StudentMapper {

    public abstract StudentDto.Output toOutput(Student student);
    public ObjectId toId(String id) {
        return new ObjectId(id);
    }

    public String toStr(ObjectId id) {
        if (id != null)
            return id.toString();
        return null;
    }
}
