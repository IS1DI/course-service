package com.is1di.courses.entity.task.template.testTemplate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Answer {
    private String answer;
    private boolean right;
}
