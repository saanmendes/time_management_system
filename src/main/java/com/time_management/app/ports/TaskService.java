package com.time_management.app.ports;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.domain.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);
    TaskResponseDTO getTaskById(String id);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO updateTask(String id, TaskUpdateDTO taskUpdateDTO);
    void deleteTask(String id);
}