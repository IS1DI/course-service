package com.is1di.courses.mapper;

import com.is1di.courses.dto.task.*;
import com.is1di.courses.entity.TaskFull;
import com.is1di.courses.entity.task.template.Note;
import com.is1di.courses.entity.task.template.homework.HomeWork;
import com.is1di.courses.entity.task.template.lecture.Lecture;
import com.is1di.courses.entity.task.template.testTemplate.Question;
import com.is1di.courses.entity.task.template.testTemplate.Test;
import com.is1di.courses.service.tasks.TaskAnsweredService;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mapper(componentModel = "spring")
@Named("taskMapper")
public abstract class TaskMapper {
    @Autowired
    private TaskAnsweredService taskAnsweredService;


    @Mapping(target = "answered", expression = "java(isTaskAnswered(test.getId(),sub))")
    @Mapping(target = "grade", expression = "java(grade(test.getId(),sub))")
    public abstract TestDto.Output.Student toOutputStudent(Test test, String sub);

    public abstract TestDto.Output.StudentStart toOutputStartTest(Test test);

    public abstract TestDto.Output.Teacher toOutputTeacher(Test test);

    public abstract Test toEntity(TestDto.Create dto);

    public abstract Lecture toEntity(LectureDto.Create dto);
    @Mapping(target = "answered", expression = "java(isTaskAnswered(lecture.getId(),sub))")
    @Mapping(target = "grade", expression = "java(grade(lecture.getId(),sub))")
    public abstract LectureDto.Output.Student toOutputStudent(Lecture lecture, String sub);

    public abstract LectureDto.Output.Teacher toOutputTeacher(Lecture lecture);
    @Mapping(target = "answered", expression = "java(isTaskAnswered(note.getId(),sub))")
    @Mapping(target = "grade", expression = "java(grade(note.getId(),sub))")
    public abstract NoteDto.Output.Student toOutputStudent(Note note, String sub);

    public abstract NoteDto.Output.Teacher toOutputTeacher(Note note);
    public abstract Note toEntity(NoteDto.Create dto);
    @Mapping(target = "answered", expression = "java(isTaskAnswered(homeWork.getId(),sub))")
    @Mapping(target = "grade", expression = "java(grade(homeWork.getId(),sub))")
    public abstract HomeWorkDto.Output.Student toOutputStudent(HomeWork homeWork, String sub);

    public abstract HomeWorkDto.Output.Teacher toOutputTeacher(HomeWork homeWork);

    public abstract HomeWork toEntity(HomeWorkDto.Create homework);
    @Mapping(target = "answered", expression = "java(isTaskAnswered(taskFull.getId(),sub))")
    @Mapping(target = "type", source = "taskFull._class")
    @Mapping(target = "grade", expression = "java(grade(taskFull.getId(),sub))")
    public abstract TaskFullDto.Output.Student toOutputStudent(TaskFull taskFull, String sub);

    @Mapping(target = "type", source = "taskFull._class")
    public abstract TaskFullDto.Output.Teacher toOutputTeacher(TaskFull taskFull);

    @Mapping(target = "id", source = "questionId")
    public abstract Question toEntity(QuestionDto.Send qu);
    public abstract Question toEntity(QuestionDto.Create qu);

    public abstract QuestionDto.Output.Teacher toOutputTeacher(Question qu);

    public ObjectId toId(String id) {
        return new ObjectId(id);
    }

    public String toStr(ObjectId id) {
        if (id != null)
            return id.toString();
        return null;
    }
    @Named("isAnswered")
    public boolean isTaskAnswered(ObjectId id, String sub) {
        AtomicBoolean isAnswered = new AtomicBoolean(false);
        taskAnsweredService.getByIdStudent(id,sub)
                .ifPresentOrElse((a) -> isAnswered.set(true),() -> isAnswered.set(false));
        return isAnswered.get();
    }

    public Double grade(ObjectId id, String sub) {
        AtomicReference<Double> grade = new AtomicReference<>(0d);
        taskAnsweredService.getByIdStudent(id,sub)
                .ifPresentOrElse(
                        (a) -> grade.set(a.getGrade()),
                        () -> grade.set(null)
                );
        return grade.get();
    }

}
