package com.is1di.courses.entity.task.testAnswered;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.is1di.courses.utils.EntityClassName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@TypeAlias(EntityClassName.TASK_ANSWERED)
@NoArgsConstructor
@Document(collection = EntityClassName.TASK_ANSWERED)
@Schema(name = "taskAnswered")
public class TaskAnswered {
    private String taskId;
    private String courseTitle;
    @JsonIgnore
    private ObjectId sectionId;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private String url;
    private String studentId;
    private Double grade;
}
