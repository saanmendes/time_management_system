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
import org.springframework.dao.DataAccessException;
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
        try {
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
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while saving the task: " + e.getMessage(), e);
        }
    }

    @Transactional
    public List<Task> getAllTasks() {
        try {
            List<TaskEntity> taskEntities = taskRepository.findAll();
            return taskEntities.stream()
                    .map(TaskMapper::taskEntityToTask)
                    .toList();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while fetching all tasks: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Optional<Task> getTaskById(String id) {
        try {
            Optional<TaskEntity> taskEntity = taskRepository.findById(id);
            return taskEntity.map(TaskMapper::taskEntityToTask);
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while fetching the task with ID: " + id + ". " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(String taskId, TaskUpdateDTO taskUpdateDTO) {
        try {
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
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while updating the task with ID: " + taskId + ". " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void deleteTask(String id) {
        try {
            if (taskRepository.existsById(id)) {
                taskRepository.deleteById(id);
            } else {
                throw new RuntimeException("Task not found with ID: " + id);
            }
        } catch (DataAccessException e) {
            throw new RuntimeException("Error while deleting the task with ID: " + id + ". " + e.getMessage(), e);
        }
    }
}