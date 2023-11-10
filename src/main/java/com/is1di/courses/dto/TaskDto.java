package com.is1di.courses.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskDto {
    @Data
    @Schema(name = "taskCreate")
    public static class Create {
        private String title;
        private String description;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime openedAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime closedAt;
    }

    public static class Output {
        @Data
        @Schema(name = "taskOutputTeacher")
        public static class Teacher {
            private String id;
            private String title;
            private String description;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime openedAt;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime closedAt;
            private String sectionId;
            private String courseTitle;

        }
        @Data
        @Schema(name = "taskOutputStudent")
        public static class Student {
            private String id;
            private String title;
            private String description;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime openedAt;
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            private LocalDateTime closedAt;
            private String sectionId;
            private String courseTitle;
            private boolean isAnswered;
            private Double grade;
        }
    }
}
