package com.time_management.app.services;

import com.time_management.app.ports.TaskService;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import com.time_management.infra.output.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final Report report;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.report = new Report();
    }

    @Override
    @Transactional
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

    public List<Task> getAllTasks() {
        List<TaskEntity> taskEntities = taskRepository.findAll();
        return taskEntities.stream()
                .map(taskEntity -> TaskMapper.taskEntityToTask(taskEntity))
                .toList();
    }

    public Optional<Task> getTaskById(String id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);
        return taskEntity.map(TaskMapper::taskEntityToTask);
    }
}