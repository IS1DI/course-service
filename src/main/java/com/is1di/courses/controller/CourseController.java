package com.is1di.courses.controller;

import com.is1di.courses.dto.StudentDto;
import com.is1di.courses.dto.course.CourseDto;
import com.is1di.courses.dto.course.SectionDto;
import com.is1di.courses.entity.courses.Course;
import com.is1di.courses.entity.courses.Section;
import com.is1di.courses.exception.AccessDeniedException;
import com.is1di.courses.mapper.CourseMapper;
import com.is1di.courses.mapper.SectionMapper;
import com.is1di.courses.mapper.StudentMapper;
import com.is1di.courses.service.CourseService;
import com.is1di.courses.service.MessageService;
import com.is1di.courses.service.SectionService;
import com.is1di.courses.service.UserService;
import com.is1di.courses.service.tasks.TaskAnsweredService;
import com.is1di.courses.utils.MessageMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final SectionService sectionService;
    private final CourseMapper courseMapper;
    private final UserService userService;
    private final SectionMapper sectionMapper;
    private final MessageService messageService;
    private final TaskAnsweredService taskAnsweredService;
    private final StudentMapper studentMapper;

    @GetMapping("course/{courseTitle}")
    public Course get(@PathVariable String courseTitle) {
        return courseService.get(courseTitle);
    }

    @GetMapping("course")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public Page<CourseDto.Output> page(@RequestParam(required = false, defaultValue = "1") int p,
                                       @RequestParam(required = false, defaultValue = "10") int l,
                                       JwtAuthenticationToken token) {
        if(userService.allowed(token,"TEACHER")) {
            return courseService.getPageTeacher(p,l, userService.sub(token)).map(courseMapper::toOutput);
        } else {
            return courseService.getPageStudent(p,l,userService.direction(token)).map(courseMapper::toOutput);
        }
    }

    @PostMapping("course/{courseTitle}/section")
    @PreAuthorize("hasRole('TEACHER')")
    public SectionDto.Output create(@PathVariable String courseTitle,
                          @RequestBody SectionDto.Create section,
                          JwtAuthenticationToken token) {
        if(courseService.exists(courseTitle,userService.sub(token)))
            return sectionMapper.toOutput(sectionService.create(courseTitle, sectionMapper.toEntity(section)));
        else throw new AccessDeniedException(
                messageService.getMessage(MessageMethod.ACCESS_DENIED)
        );
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("course/{courseTitle}/progress")
    public Double getProgress(@PathVariable String courseTitle, JwtAuthenticationToken token) {
        return taskAnsweredService.progressByCourse(courseTitle, userService.sub(token));
    }

    @GetMapping("course/{courseTitle}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public Page<StudentDto.Output> getStudentsByCourse(@PathVariable String courseTitle,
                                                       @RequestParam(required = false, defaultValue = "1") int p,
                                                       @RequestParam(required = false, defaultValue = "10") int l) {
        return courseService.getPageStudentsByCourse(p,l,courseTitle).map(studentMapper::toOutput);
    }
}
