package com.is1di.courses.entity;


import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.utils.EntityClassName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Document(collection = EntityClassName.STUDENT)
@TypeAlias(EntityClassName.STUDENT)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String sub;
    private String fullName;
    private String direction;
    private String group;
    @DocumentReference(lookup = "{'studentId' : ?#{#self.sub}}")
    private List<TaskAnswered> tasksAnswered;
}
