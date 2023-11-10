package com.is1di.courses.entity.task.template;

import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@TypeAlias(EntityClassName.NOTE)
@Document(collection = EntityClassName.TASK)
public class Note extends Task {
    private String fileUrl;
}
