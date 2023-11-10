package com.is1di.courses.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.is1di.courses.dto.TaskDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class TaskFullDto {

    public static class Output  {
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "taskFullOutputTeacher")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Teacher extends TaskDto.Output.Teacher {
            private String fileUrl;
            private String type;
        }
        @EqualsAndHashCode(callSuper = true)
        @Data
        @Schema(name = "taskFullOutputStudent")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Student extends TaskDto.Output.Student {
            private String fileUrl;
            private String type;
        }
    }


}
