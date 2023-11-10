package com.is1di.courses.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CourseDto {
    @Data
    @Schema(name = "courseOutput")
    public static class Output {
        private String title;
        private String allowedDirection;
        private String teacherId;
        private String teacherFullName;
        private String description;
        private LocalDate startAt;
        private LocalDate endsAt;
        List<String> sectionIds;
    }
}
