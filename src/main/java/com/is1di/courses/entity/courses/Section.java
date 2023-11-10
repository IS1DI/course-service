package com.is1di.courses.entity.courses;

import com.is1di.courses.entity.task.template.Task;
import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = EntityClassName.SECTION)
public class Section {
    @Id
    private ObjectId id;
    private String title;
    private String description;
    @DocumentReference(lookup = "{'sectionId' : ?#{#self._id}}")
    private List<Task> tasks;
    private String courseTitle;
}
