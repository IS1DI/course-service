package com.is1di.courses.entity.task.template.testTemplate;

import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@TypeAlias(EntityClassName.QUESTION)
@Document(collection = EntityClassName.QUESTION)
public class Question {
    private ObjectId id;
    private String question;
    private ObjectId testId;
    private List<Answer> answers = new ArrayList<>();
}
