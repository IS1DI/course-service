package com.is1di.courses.controller.tasks;

import com.is1di.courses.dto.task.QuestionDto;
import com.is1di.courses.dto.task.TestDto;
import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.TestService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("task/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @Operation(
            summary = "Начать тест (только для студента)",
            description = "возвращается сам тест"
    )
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("{taskId}")
    public TestDto.Output.StudentStart startTest(@PathVariable ObjectId taskId,
                                            JwtAuthenticationToken token) {
        return taskMapper.toOutputStartTest(testService.startTest(taskId,userService.toStudent(token)));
    }

    @Operation(
            summary = "Завершить тест (только для студента)",
            description = "возвращается результат по тесту, в боди нужно отправить список id вопросов в котором есть список ответов (ответы в виде текста т.к. у ответов нет id) "
    )
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("{taskId}")
    public TaskAnswered finishTest(@PathVariable ObjectId taskId,
                                   @RequestBody List<QuestionDto.Send> questions,
                                   JwtAuthenticationToken token) {
        return testService.finishTest(taskId,questions.stream().map(taskMapper::toEntity).collect(Collectors.toList()), userService.sub(token));
    }

    @Operation(
            summary = "Добавить вопрос к тесту (только для учителя)",
            description = "возвращается весь тест. В боди сам вопрос со списком ответов (у правильных ответов должно быть указано в поле right - true. У неправильных это можно не указывать) "
    )
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("{taskId}/question")
    public QuestionDto.Output.Teacher addQuestion(@PathVariable ObjectId taskId, @RequestBody QuestionDto.Create questionDto) {
        return taskMapper.toOutputTeacher(testService.addQuestion(taskId,taskMapper.toEntity(questionDto)));
    }

    @Operation(
            summary = "Удалить вопрос из теста (только для учителя)",
            description = "Ничего не возвращается (только статус NO_CONTENT)"
    )
    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("{taskId}/question/{questionId}")
    public ResponseEntity<?> removeQuestion(@PathVariable ObjectId taskId, @PathVariable ObjectId questionId) {
        testService.removeQuestion(taskId,questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Получить тест со списком правильных ответов (только для учителя)",
            description = "возвращается тест со списком вопросов, в каждом вопросе список ответов (у каждого ответа указано в поле isRight - true - если ответ правильный, false - если не правильный соответственно)"
    )
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("{taskId}")
    public TestDto.Output.Teacher getTestForTeacher(@PathVariable ObjectId taskId) {
        return taskMapper.toOutputTeacher(testService.findById(taskId));
    }
}
