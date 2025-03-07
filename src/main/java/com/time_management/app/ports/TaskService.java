package com.time_management.app.ports;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO);
    TaskResponseDTO getTaskById(String id);
    List<TaskResponseDTO> getAllTasks();
    TaskResponseDTO updateTask(String id, TaskUpdateDTO taskUpdateDTO);
    void deleteTask(String id);
    TaskResponseDTO updateTaskPendingStatus(String id, TaskUpdateDTO taskUpdateDTO);
}