package com.is1di.courses.utils;

import lombok.Getter;

@Getter
public enum MessageMethod {

    COURSE_NOT_FOUND("course.error.notFound"),

    SECTION_ALREADY_EXISTS("section.error.exists"), //with title

    TASK_NOT_FOUND("task.error.notFound"),
    TIME_MUST_BE_AFTER("time.error.after"),
    SECTION_NOT_FOUND("section.error.notFound"),
    QUESTION_NOT_FOUND("question.error.notFound"),
    ALREADY_STARTED_TEST("test.error.alreadyStarted"),
    TASK_NOT_STARTED("task.error.notStarted"),
    TASK_ACCESS_DENIED("task.error.accessDenied"),
    TASK_ANSWERED_NOT_FOUND("task.error.answered.notFound"),
    ALREADY_FINISHED_TEST("test.error.alreadyFinished"),
    TEST_TIMED_OUT("test.error.timedOut"),
    ZERO_RIGHT_ANSWERS_IN_TEST("test.error.zeroRightAnswers"),
    TASK_TIME_EXPIRED("task.error.time.expired"),
    TASK_ALREADY_FINISHED("task.error.alreadyFinished"),
    ACCESS_DENIED("access.error.denied");

    private final String val;

    MessageMethod(String val) {
        this.val = val;
    }

}
