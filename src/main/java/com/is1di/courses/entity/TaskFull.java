package com.is1di.courses.entity;

import com.is1di.courses.entity.task.template.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TaskFull extends Task {
    private String _class;
    private String fileUrl;
    private Long duration;
}
