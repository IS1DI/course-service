package com.is1di.courses.dto.task;

import com.is1di.courses.dto.TaskDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class LectureDto {
    @EqualsAndHashCode(callSuper = true)
    @Schema(name = "lectureCreate")
    @Data
    public static class Create extends TaskDto.Create {
        private String fileUrl;
    }

    public static class Output {
        @EqualsAndHashCode(callSuper = true)
        @Schema(name = "lectureOutputStudent")
        @Data
        public static class Student extends TaskDto.Output.Student {
            private String fileUrl;
        }
        @EqualsAndHashCode(callSuper = true)
        @Schema(name = "lectureOutputTeacher")
        @Data
        public static class Teacher extends TaskDto.Output.Teacher {
            private String fileUrl;
        }
    }
}
