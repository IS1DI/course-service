package com.is1di.courses.controller.tasks;

import com.is1di.courses.dto.task.LectureDto;
import com.is1di.courses.entity.task.template.lecture.Lecture;
import com.is1di.courses.entity.task.testAnswered.TaskAnswered;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.LectureService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@RequestMapping("task/lecture")
@RestController
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;
    private final UserService userService;
    private final TaskMapper taskMapper;

    @GetMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getById(@PathVariable ObjectId taskId, JwtAuthenticationToken token) {
        if(userService.allowed(token,"TEACHER"))
            return ok(taskMapper.toOutputTeacher(lectureService.getLecture(taskId)));
        return ok(taskMapper.toOutputStudent(lectureService.findById(taskId),userService.sub(token)));
    }

    @PostMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT')")
    public TaskAnswered completeLecture(@PathVariable ObjectId taskId, JwtAuthenticationToken token) {
        return lectureService.completeLecture(taskId,userService.toStudent(token));
    }
}
