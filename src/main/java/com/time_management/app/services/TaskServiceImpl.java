package com.time_management.app.services;

import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.app.exceptions.*;
import com.time_management.app.ports.TaskService;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import com.time_management.infra.output.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final StackspotAuthenticator stackspotAuthenticator;
    private final TaskRepository taskRepository;
    private final QuickCommandService quickCommandService;
    private final Report report;

    public TaskServiceImpl(TaskRepository taskRepository, StackspotAuthenticator stackspotAuthenticator, QuickCommandService quickCommandService) {
        this.stackspotAuthenticator = stackspotAuthenticator;
        this.taskRepository = taskRepository;
        this.quickCommandService = quickCommandService;
        this.report = new Report();
    }

    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        try {
            String token = authenticateAndLog();
            String category = executeQuickCommandAndFetchResult(token, taskRequestDTO, true);
            String priority = executeQuickCommandAndFetchResult(token, taskRequestDTO, false);

            TaskEntity taskEntity = TaskMapper.taskRequestToTaskEntity(taskRequestDTO);
            taskEntity.setCategory(category);
            taskEntity.setPriority(priority);

            taskRepository.save(taskEntity);
            Task savedTask = TaskMapper.taskEntityToTask(taskEntity);
            report.getTasks().add(savedTask);

            logger.info("Task created successfully with ID: {}", savedTask.getId());
            return TaskMapper.taskEntityToTaskResponseDTO(taskEntity);

        } catch (DataAccessException exception) {
            logger.error("Error creating task: {}", exception.getMessage(), exception);
            throw new TaskCreationException("Error while saving the task: " + exception.getMessage(), exception);
        }
    }

    @Transactional
    public List<TaskResponseDTO> getAllTasks() {
        try {
            return taskRepository.findAll().stream()
                    .map(TaskMapper::taskEntityToTaskResponseDTO)
                    .toList();
        } catch (DataAccessException exception) {
            logger.error("Error fetching all tasks: {}", exception.getMessage(), exception);
            throw new RuntimeException("Error while fetching all tasks: " + exception.getMessage(), exception);
        }
    }

    @Override
    @Transactional
    public TaskResponseDTO getTaskById(String id) {
        return taskRepository.findById(id)
                .map(TaskMapper::taskEntityToTaskResponseDTO)
                .orElseThrow(() -> {
                    logger.error("Task with ID {} not found", id);
                    return new TaskNotFoundException("Task not found");
                });
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(String taskId, TaskUpdateDTO taskUpdateDTO) {
        return updateTaskEntity(taskId, taskEntity -> {
            taskEntity.setEmail(taskUpdateDTO.getEmail());
            taskEntity.setDescription(taskUpdateDTO.getDescription());
            taskEntity.setRole(taskUpdateDTO.getRole());
            taskEntity.setInitialDate(taskUpdateDTO.getInitialDate());
            taskEntity.setEndDate(taskUpdateDTO.getEndDate());
        });
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskPendingStatus(String taskId, TaskUpdateDTO taskUpdateDTO) {
        return updateTaskEntity(taskId, taskEntity -> taskEntity.setCompleted(taskUpdateDTO.isCompleted()));
    }

    @Override
    @Transactional
    public void deleteTask(String id) {
        try {
            if (!taskRepository.existsById(id)) {
                logger.warn("Attempted to delete non-existent task with ID: {}", id);
                throw new TaskNotFoundException("Task not found with ID: " + id);
            }
            taskRepository.deleteById(id);
        } catch (DataAccessException exception) {
            logger.error("Error deleting task with ID {}: {}", id, exception.getMessage(), exception);
            throw new TaskDeletionException("Error while deleting the task with ID: " + id + ". " + exception.getMessage(), exception);
        }
    }

    private String authenticateAndLog() {
        String token = stackspotAuthenticator.authenticate();
        logger.info("Stackspot authenticated successfully: {}", token);
        return token;
    }

    private String executeQuickCommandAndFetchResult(String token, TaskRequestDTO taskRequestDTO, boolean isCategory) {
        String executionId = isCategory
                ? quickCommandService.executeCategoryQuickCommand(token, taskRequestDTO)
                : quickCommandService.executePriorityQuickCommand(token, taskRequestDTO);

        logger.info("Quick command executed successfully");

        String result;
        do {
            result = isCategory
                    ? quickCommandService.getQuickCommandCategoryCallback(token, executionId)
                    : quickCommandService.getQuickCommandPriorityCallback(token, executionId);

            if (result == null) {
                logger.info("Quick command callback returned null");
            }
        } while (result == null);

        logger.info("Quick command callback returned: {}", result);
        return result;
    }

    private TaskResponseDTO updateTaskEntity(String taskId, TaskUpdater updater) {
        try {
            TaskEntity existingTaskEntity = taskRepository.findById(taskId)
                    .orElseThrow(() -> {
                        logger.warn("Task with ID {} not found", taskId);
                        return new TaskNotFoundException("Task not found with ID: " + taskId);
                    });

            updater.update(existingTaskEntity);

            TaskEntity updatedTaskEntity = taskRepository.save(existingTaskEntity);
            Task updatedTask = TaskMapper.taskEntityToTask(updatedTaskEntity);

            report.getTasks().removeIf(task -> task.getId().equals(taskId));
            report.getTasks().add(updatedTask);

            logger.info("Task updated successfully with ID: {}", updatedTask.getId());
            return TaskMapper.taskToTaskResponseDTO(updatedTask);

        } catch (DataAccessException exception) {
            logger.error("Error updating task with ID {}: {}", taskId, exception.getMessage(), exception);
            throw new TaskUpdateException("Error while updating the task with ID: " + taskId + ". " + exception.getMessage(), exception);
        }
    }

    @FunctionalInterface
    private interface TaskUpdater {
        void update(TaskEntity taskEntity);
    }
}