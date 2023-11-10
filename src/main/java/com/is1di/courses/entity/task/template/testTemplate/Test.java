package com.is1di.courses.entity.task.template.testTemplate;

import com.is1di.courses.entity.task.template.Task;
import com.is1di.courses.utils.EntityClassName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = EntityClassName.TASK)
@TypeAlias(EntityClassName.TEST)
@Schema
public class Test extends Task {
    @DocumentReference(lookup = "{'testId' : ?#{#self._id}}")
    private List<Question> questions;
    private Long duration;
    private Boolean isPrivate; //isPrivate = false -> показывать ответы сразу после того как студент ответил
}
