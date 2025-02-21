package com.time_management.app.ports.output;

import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.TaskEntity;

public interface TaskRepository {
    TaskEntity save(TaskEntity taskEntity);
}