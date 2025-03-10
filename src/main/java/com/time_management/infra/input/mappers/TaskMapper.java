package com.time_management.infra.input.mappers;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.TaskEntity;

import java.util.ArrayList;
import java.util.List;

public class TaskMapper {

    public static TaskEntity taskToTaskEntity(Task task) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(task.getId());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setPriority(task.getPriority());
        taskEntity.setCategory(task.getCategory());
        taskEntity.setRole(task.getRole());
        taskEntity.setInitialDate(task.getInitialDate());
        taskEntity.setEndDate(task.getEndDate());
        taskEntity.setEmail(task.getEmail());
        taskEntity.setCompleted(taskEntity.isCompleted());
        return taskEntity;
    }

    public static Task taskEntityToTask(TaskEntity taskEntity) {
        Task task = new Task();
        task.setId(taskEntity.getId());
        task.setDescription(taskEntity.getDescription());
        task.setPriority(taskEntity.getPriority());
        task.setCategory(taskEntity.getCategory());
        task.setRole(taskEntity.getRole());
        task.setInitialDate(taskEntity.getInitialDate());
        task.setEndDate(taskEntity.getEndDate());
        task.setEmail(taskEntity.getEmail());
        task.setCompleted(taskEntity.isCompleted());
        return task;
    }

    public static TaskEntity taskRequestToTaskEntity(TaskRequestDTO taskRequestDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription(taskRequestDTO.getDescription());
        taskEntity.setEmail(taskRequestDTO.getEmail());
        taskEntity.setRole(taskRequestDTO.getRole());
        taskEntity.setInitialDate(taskRequestDTO.getInitialDate());
        taskEntity.setEndDate(taskRequestDTO.getEndDate());
        taskEntity.setCompleted(taskRequestDTO.isCompleted());
        return taskEntity;
    }

    public static TaskResponseDTO taskToTaskResponseDTO(Task task) {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setEmail(task.getEmail());
        taskResponseDTO.setRole(task.getRole());
        taskResponseDTO.setInitialDate(task.getInitialDate());
        taskResponseDTO.setCategory(task.getCategory());
        taskResponseDTO.setCompleted(task.isCompleted());
        taskResponseDTO.setPriority(task.getPriority());
        return taskResponseDTO;
    }

    public static TaskResponseDTO taskEntityToTaskResponseDTO(TaskEntity taskEntity) {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(taskEntity.getId());
        taskResponseDTO.setEmail(taskEntity.getEmail());
        taskResponseDTO.setDescription(taskEntity.getDescription());
        taskResponseDTO.setRole(taskEntity.getRole());
        taskResponseDTO.setInitialDate(taskEntity.getInitialDate());
        taskResponseDTO.setEndDate(taskEntity.getEndDate());
        taskResponseDTO.setPriority(taskEntity.getPriority());
        taskResponseDTO.setCategory(taskEntity.getCategory());
        taskResponseDTO.setCompleted(taskEntity.isCompleted());
        return taskResponseDTO;
    }

    public static TaskEntity taskUpdateToTaskEntity(TaskUpdateDTO taskUpdateDTO) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setEmail(taskUpdateDTO.getEmail());
        taskEntity.setDescription(taskUpdateDTO.getDescription());
        taskEntity.setRole(taskUpdateDTO.getRole());
        taskEntity.setInitialDate(taskUpdateDTO.getInitialDate());
        taskEntity.setEndDate(taskUpdateDTO.getEndDate());
        taskEntity.setCompleted(taskUpdateDTO.isCompleted());
        return taskEntity;
    }

    public static List<Task> taskEntityToTaskList(List<TaskEntity> taskEntityList) {
        List<Task> tasks = new ArrayList<>();
        for (TaskEntity taskEntity : taskEntityList) {
            tasks.add(taskEntityToTask(taskEntity));
        }
        return tasks;
    }

    public static List<TaskEntity> taskToTaskEntityList(List<Task> tasks) {
        List<TaskEntity> taskEntities = new ArrayList<>();
        for (Task task : tasks) {
            taskEntities.add(taskToTaskEntity(task));
        }
        return taskEntities;
    }

}

