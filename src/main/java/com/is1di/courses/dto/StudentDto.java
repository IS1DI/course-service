package com.is1di.courses.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

public class StudentDto {
    @Data
    @Schema(name = "studentOutput")
    public static final class Output {
        private String sub;
        private String fullName;
        private String direction;
        private String group;
    }
}
