package com.is1di.courses.controller;

import com.is1di.courses.dto.course.SectionDto;
import com.is1di.courses.dto.task.*;
import com.is1di.courses.entity.TaskFull;
import com.is1di.courses.entity.courses.Section;
import com.is1di.courses.entity.task.template.homework.HomeWork;
import com.is1di.courses.entity.task.template.testTemplate.Test;
import com.is1di.courses.mapper.SectionMapper;
import com.is1di.courses.mapper.TaskMapper;
import com.is1di.courses.service.CourseService;
import com.is1di.courses.service.SectionService;
import com.is1di.courses.service.tasks.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RequestMapping("section")
@RestController
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;
    private final TaskService taskService;
    private final SectionMapper sectionMapper;
    private final TaskMapper taskMapper;

    @GetMapping("{sectionId}")
    public SectionDto.Output get(@PathVariable ObjectId sectionId) {
        return sectionMapper.toOutput(sectionService.get(sectionId));
    }

/*    @PutMapping("{sectionId}")
    public SectionDto.Output update(@PathVariable ObjectId sectionId,
                          @RequestBody SectionD section) {
        return sectionMapper.toOutput(sectionService.update(sectionId,section));
    }*/
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("{sectionId}/test")
    public TestDto.Output.Teacher create(@PathVariable ObjectId sectionId,
                       @RequestBody @Valid  TestDto.Create task){
        return taskMapper.toOutputTeacher(taskService.create(sectionId,taskMapper.toEntity(task)));
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("{sectionId}/homework")
    public HomeWorkDto.Output.Teacher create(@PathVariable ObjectId sectionId,
                                             @RequestBody @Valid  HomeWorkDto.Create task){
        return taskMapper.toOutputTeacher(taskService.create(sectionId,taskMapper.toEntity(task)));
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("{sectionId}/lecture")
    public LectureDto.Output.Teacher create(@PathVariable ObjectId sectionId,
                                            @RequestBody @Valid LectureDto.Create task){
        return taskMapper.toOutputTeacher(taskService.create(sectionId,taskMapper.toEntity(task)));
    }
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("{sectionId}/note")
    public NoteDto.Output.Teacher create(@PathVariable ObjectId sectionId,
                                         @RequestBody @Valid  NoteDto.Create task){
        return taskMapper.toOutputTeacher(taskService.create(sectionId,taskMapper.toEntity(task)));
    }
}
