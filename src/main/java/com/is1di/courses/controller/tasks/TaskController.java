package com.is1di.courses.controller.tasks;

import com.is1di.courses.dto.task.TaskFullDto;
import com.is1di.courses.entity.TaskFull;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskMapper taskMapper;


    @Operation(
            summary = "Получить задание в зависимости от роли возвращается разный ответ",
            description = "Если роль студeнт (смотреть схему внизу страницы taskFullOutputStudent), то вернется задание и его тип и isAnswered - true -  если задание выполнено. Для учителя (смотреть схему внизу страницы taskFullOutputTeacher)- также с типом но без isAnswered "
    )
    @GetMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getFullTask(@PathVariable ObjectId taskId, JwtAuthenticationToken token) {
        if(userService.allowed(token,"TEACHER"))
            return ok( taskMapper.toOutputTeacher(taskService.getFull(taskId)));
        return ok(taskMapper.toOutputStudent(taskService.getFull(taskId), userService.sub(token)));
    }
}
