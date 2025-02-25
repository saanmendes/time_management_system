package com.time_management.app.services;

import com.time_management.app.dtos.tasks.TaskUpdateDTO;
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
                savedTask.getRole(),
                savedTask.isPending()
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

    @Override
    public TaskResponseDTO updateTask(String taskId, TaskUpdateDTO taskUpdateDTO) {
        TaskEntity existingTaskEntity = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        // Atualiza
        existingTaskEntity.setEmail(taskUpdateDTO.getEmail());
        existingTaskEntity.setDescription(taskUpdateDTO.getDescription());
        existingTaskEntity.setRole(taskUpdateDTO.getRole());
        existingTaskEntity.setInitialDate(taskUpdateDTO.getInitialDate());
        existingTaskEntity.setEndDate(taskUpdateDTO.getEndDate());

        // Salva
        TaskEntity updatedTaskEntity = taskRepository.save(existingTaskEntity);
        Task updatedTask = TaskMapper.taskEntityToTask(updatedTaskEntity);

        // Atualiza no Report
        report.getTasks().removeIf(task -> task.getId().equals(taskId));
        report.getTasks().add(updatedTask);

        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getEmail(),
                updatedTask.getDescription(),
                updatedTask.getInitialDate(),
                updatedTask.getEndTime(),
                updatedTask.getRole(),
                updatedTask.isPending()
        );
    }

    @Override
    public void deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

}