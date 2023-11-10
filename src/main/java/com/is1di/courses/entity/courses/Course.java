package com.is1di.courses.entity.courses;

import com.is1di.courses.entity.Student;
import com.is1di.courses.utils.EntityClassName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Document(collection = EntityClassName.COURSE)
@TypeAlias(EntityClassName.COURSE)
@Data
@NoArgsConstructor
public class Course {
    @Indexed(unique = true)
    private String title;
    private String allowedDirection;
    private String teacherId;
    private String teacherFullName;
    private String description;
    private LocalDate startAt;
    private LocalDate endsAt;
    @DocumentReference(lookup = "{'courseTitle' : ?#{#self.title}}", collection = EntityClassName.SECTION)
    private List<Section> sections;
    @DocumentReference(lookup = "{'direction' : ?#{#self.allowedDirection}}")
    private List<Student> students;
    public Course(String title,String allowedDirection,String teacherId,String teacherFullName, String description, String startAt,String endsAt) {
        this.title = title;
        this.allowedDirection = title;
        this.teacherId = teacherId;
        this.teacherFullName = teacherFullName;
        this.description = description;
        this.startAt = LocalDate.parse(startAt,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.endsAt = LocalDate.parse(endsAt, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }
}
