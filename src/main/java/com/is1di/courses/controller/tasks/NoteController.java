package com.is1di.courses.controller.tasks;

import com.is1di.courses.dto.task.NoteDto;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.NoteService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("task/note")
@RestController
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;
    private final TaskMapper taskMapper;
    private final UserService userService;


    @GetMapping("{taskId}")
    @PreAuthorize("hasRole('STUDENT')")
    public NoteDto.Output.Student getTaskByStudent(@PathVariable ObjectId taskId, JwtAuthenticationToken token) {
        noteService.completeNote(taskId,userService.toStudent(token));
        return taskMapper.toOutputStudent(noteService.findById(taskId),userService.sub(token));
    }
}
