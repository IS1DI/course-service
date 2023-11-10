package com.is1di.courses.controller.tasks;

import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.tasks.TaskAnsweredService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
@RequiredArgsConstructor
public class TaskAnsweredController {
    private final TaskAnsweredService taskAnsweredService;

    @Operation(
            summary = "Получить все ответы на задания по студенту (только для учителя)",
            description = "Возвращает все ответы "
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("{studentId}")
    public Page<TaskAnswered> taskAnsweredPageByStudent(@PathVariable String studentId,
                                                        @RequestParam(required = false, defaultValue = "1") int p,
                                                        @RequestParam(required = false, defaultValue = "10") int l) {
        return taskAnsweredService.getPageByStudent(p, l, studentId);
    }
}
