package com.time_management.app.ports;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);
}