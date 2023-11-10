package com.is1di.courses.controller.tasks;

import com.is1di.courses.dto.task.HomeWorkDto;
import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("task/homework")
public class HomeWorkController {
    private final HomeworkService homeworkService;
    private final UserService userService;
    private final TaskMapper taskMapper;
    @PostMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT')")
    public TaskAnswered uploadHW(@PathVariable ObjectId taskId, @RequestBody String url, JwtAuthenticationToken token) {
        return homeworkService.uploadHomeWork(taskId,userService.toStudent(token),url);
    }

    @PostMapping("{taskId}/student/{studentId}/rate")
    @PreAuthorize("hasRole('TEACHER')")
    public TaskAnswered rateHw(@PathVariable ObjectId taskId,@PathVariable String studentId, @RequestBody Double grade) {
        return homeworkService.rateHomeWork(taskId,studentId,grade);
    }

    @GetMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getHW(@PathVariable ObjectId taskId, JwtAuthenticationToken token) {
        if(userService.allowed(token,"TEACHER"))
            return ok(taskMapper.toOutputTeacher(homeworkService.findById(taskId)));
        return ok(taskMapper.toOutputStudent(homeworkService.findById(taskId), userService.sub(token)));
    }
}
