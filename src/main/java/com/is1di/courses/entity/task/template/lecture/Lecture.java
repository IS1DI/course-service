package com.is1di.courses.entity.task.template.lecture;

import com.is1di.courses.entity.task.template.Task;
import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = EntityClassName.TASK)
@TypeAlias(EntityClassName.LECTURE)
public class Lecture extends Task {
    private String fileUrl;
}
