package com.time_management.app.ports.input;

import com.time_management.domain.dtos.tasks.TaskRequestDTO;
import com.time_management.domain.dtos.tasks.TaskResponseDTO;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);
}