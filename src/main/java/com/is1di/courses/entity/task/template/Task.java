package com.is1di.courses.entity.task.template;

import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@TypeAlias(EntityClassName.TASK)
@Document(collection = EntityClassName.TASK)
public class Task {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String title;
    private String description;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private ObjectId sectionId;
    private String courseTitle;
}
