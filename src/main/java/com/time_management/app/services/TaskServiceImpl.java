package com.time_management.app.services;

import com.time_management.app.ports.TaskService;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import com.time_management.infra.output.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final Report report;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.report = new Report();
    }

    @Override
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        TaskEntity savedTaskEntity = taskRepository.save(TaskMapper.taskRequestToTaskEntity(taskRequestDTO));

        Task savedTask = TaskMapper.taskEntityToTask(savedTaskEntity);

        report.getTasks().add(savedTask);

        return new TaskResponseDTO(
                savedTask.getId(),
                savedTask.getEmail(),
                savedTask.getDescription(),
                savedTask.getInitialDate(),
                savedTask.getEndTime(),
                savedTask.getRole()
        );
    }
}