package com.is1di.courses.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class AnswerDto {
    @Schema(name = "answerCreate")
    @Data
    public static class Create {
        private String answer;
        private Boolean right;
    }
    @Data
    @Schema(name = "answerSend")
    public static class Send {
        private String answer;
    }
    public static class Output {
        @Data
        @Schema(name = "answerOutputStudent")
        public static class Student {
            private String answer;
        }
        @Data
        @Schema(name = "answerOutputTeacher")
        public static class Teacher {
            private String answer;
            private boolean right;
        }
    }
}
