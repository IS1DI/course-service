package com.is1di.courses.dto.task;

import com.is1di.courses.dto.TaskDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class HomeWorkDto extends TaskDto {
    @EqualsAndHashCode(callSuper = true)
    @Schema(name = "homeworkCreate")
    @Data
    public static class Create extends TaskDto.Create {
        private String fileUrl;
    }

    public static class Output {
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "hwOutputStudent")
        public static class Student extends TaskDto.Output.Student {
            private String fileUrl;
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "hwOutputTeacher")
        public static class Teacher extends TaskDto.Output.Teacher {
            private String fileUrl;
        }
    }
}
