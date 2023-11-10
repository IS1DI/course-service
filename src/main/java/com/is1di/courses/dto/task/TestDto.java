package com.is1di.courses.dto.task;

import com.is1di.courses.dto.TaskDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

public class TestDto extends TaskDto {
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Create extends TaskDto.Create {
        private Long duration;
    }


    public static class Output  {
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "testOutputStudent")
        public static class Student extends TaskDto.Output.Student {
            private Long duration;
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "testStartOutputStudent")
        public static class StudentStart extends TaskDto.Output.Student {
            private Long duration;
            private List<QuestionDto.Output.Student> questions;
        }

        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "testOutputTeacher")
        public static class Teacher extends TaskDto.Output.Teacher {
            private Long duration;
            private List<QuestionDto.Output.Teacher> questions;
        }
    }
}
