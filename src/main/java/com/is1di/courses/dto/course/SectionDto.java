package com.is1di.courses.dto.course;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

public class SectionDto {
    @Data
    @Schema(name = "sectionCreate")
    public static final class Create {
        private String title;
        private String description;
    }

    @Data
    @Schema(name = "sectionOutput")
    public static final class Output {
        private String id;
        private String title;
        private String description;
        private List<String> tasks;
    }
}
