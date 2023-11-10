package com.is1di.courses.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

public class QuestionDto {
    @Data
    @Schema(name = "questionCreate")
    public static class Create {
        private String question;
        private List<AnswerDto.Create> answers;
    }

    @Data
    @Schema(name = "questionSend")
    public static class Send {
        private String questionId;
        private List<AnswerDto.Send> answers;
    }
    public static class Output {
        @Data
        @Schema(name = "questionOutputStudent")
        public static class Student {
            private String id;
            private String question;
            private List<AnswerDto.Output.Student> answers;
            private String testId;
        }
        @Data
        @Schema(name = "questionOutputTeacher")
        public static class Teacher {
            private String id;
            private String question;
            private List<AnswerDto.Output.Teacher> answers;
            private String testId;
        }
    }
}
