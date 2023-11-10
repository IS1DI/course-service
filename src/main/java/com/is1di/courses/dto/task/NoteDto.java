package com.is1di.courses.dto.task;

import com.is1di.courses.dto.TaskDto;
import com.is1di.courses.entity.task.template.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class NoteDto {
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema(name = "noteCreate")
    public static class Create extends TaskDto.Create {
        private String fileUrl;
    }


    public static class Output {
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "noteOutputTeacher")
        public static class Teacher extends TaskDto.Output.Teacher {
            private String fileUrl;
        }
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "noteStudentOutput")
        public static class Student extends TaskDto.Output.Student {
            private String fileUrl;
        }
    }
}
