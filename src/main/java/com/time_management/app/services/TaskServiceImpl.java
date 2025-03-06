package com.time_management.app.services;

import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.app.exceptions.TaskCreationException;
import com.time_management.app.exceptions.TaskDeletionException;
import com.time_management.app.exceptions.TaskNotFoundException;
import com.time_management.app.exceptions.TaskUpdateException;
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
            String token = stackspotAuthenticator.authenticate();
            logger.info("Stackspot authenticated successfully: {}", token);

            String categoryExecutionId = quickCommandService.executeCategoryQuickCommand(token, taskRequestDTO);
            logger.info("Quick command executed successfully");

            String priorityExecutionId = quickCommandService.executePriorizationQuickCommand(token, taskRequestDTO);

            String category = quickCommandService.getQuickCommandCategoryCallback(token, categoryExecutionId);

            String priority = quickCommandService.getQuickCommandPriorityCallback(token, priorityExecutionId);

            while (category == null) {
                logger.info("Quick command callback returned null");
                category = quickCommandService
                        .getQuickCommandCategoryCallback(token, categoryExecutionId);
            }
            logger.info("Quick command callback returned: {}", category);

            while (priority == null) {
                logger.info("Quick command priority callback returned null");
                priority = quickCommandService
                        .getQuickCommandPriorityCallback(token, priorityExecutionId);
            }
            logger.info("Quick command priority callback returned: {}", priority);

            TaskEntity taskEntity = TaskMapper.taskRequestToTaskEntity(taskRequestDTO);
            taskEntity.setCategory(category);
            taskEntity.setPriority(priority);

            taskRepository.save(taskEntity);

            Task savedTask = TaskMapper.taskEntityToTask(taskEntity);

            report.getTasks().add(savedTask);

            TaskResponseDTO taskResponseDTO = TaskMapper.taskEntityToTaskResponseDTO(taskEntity);

            logger.info("Task created successfully with ID: {}", savedTask.getId());
            return taskResponseDTO;

        } catch (DataAccessException exception) {
            logger.error("Error creating task: " + exception.getMessage(), exception);
            throw new TaskCreationException("Error while saving the task: " + exception.getMessage(), exception);
        }
    }

    @Transactional
    public List<TaskResponseDTO> getAllTasks() {
        try {
            List<TaskEntity> taskEntities = taskRepository.findAll();
            return taskEntities.stream()
                    .map(TaskMapper::taskEntityToTaskResponseDTO)
                    .toList();
        } catch (DataAccessException exception) {
            logger.error("Error fetching all tasks: " + exception.getMessage(), exception);
            throw new RuntimeException("Error while fetching all tasks: " + exception.getMessage(), exception);
        }
    }

    @Override
    @Transactional
    public TaskResponseDTO getTaskById(String id) {
        try {
            TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                    () -> new TaskNotFoundException("Task not found"));
            return TaskMapper.taskEntityToTaskResponseDTO(taskEntity);
        } catch (TaskNotFoundException exception) {
            logger.error("Error fetching task with ID {}: " + exception.getMessage(), id, exception);
            throw new TaskNotFoundException("Task not found");
        }
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(String taskId, TaskUpdateDTO taskUpdateDTO) {
        try {
            TaskEntity existingTaskEntity = taskRepository.findById(taskId)
                    .orElseThrow(() -> {
                        logger.warn("Task with ID {} not found", taskId);
                        return new TaskNotFoundException("Task not found with ID: " + taskId);
                    });

            logger.info("Updating task with ID: {}", taskId);
            existingTaskEntity.setEmail(taskUpdateDTO.getEmail());
            existingTaskEntity.setDescription(taskUpdateDTO.getDescription());
            existingTaskEntity.setRole(taskUpdateDTO.getRole());
            existingTaskEntity.setInitialDate(taskUpdateDTO.getInitialDate());
            existingTaskEntity.setEndDate(taskUpdateDTO.getEndDate());

            logger.info("Saving task with ID: {}", taskId);
            TaskEntity updatedTaskEntity = taskRepository.save(existingTaskEntity);
            Task updatedTask = TaskMapper.taskEntityToTask(updatedTaskEntity);

            report.getTasks().removeIf(task -> task.getId().equals(taskId));
            report.getTasks().add(updatedTask);

            TaskResponseDTO responseDTO = TaskMapper.taskToTaskResponseDTO(updatedTask);

            logger.info("Task updated successfully with ID: {}", updatedTask.getId());
            return responseDTO;

        } catch (DataAccessException exception) {
            logger.error("Error updating task with ID {}: " + exception.getMessage(), taskId, exception);
            throw new TaskUpdateException("Error while updating the task with ID: " + taskId + ". " + exception.getMessage(), exception);
        }
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTaskPendingStatus(String taskId, TaskUpdateDTO taskUpdateDTO) {
        try {
            TaskEntity existingTaskEntity = taskRepository.findById(taskId)
                    .orElseThrow(() -> {
                        logger.warn("Task with ID {} not found", taskId);
                        return new TaskNotFoundException("Task not found with ID: " + taskId);
                    });

            existingTaskEntity.setCompleted(taskUpdateDTO.isCompleted());

            TaskEntity updatedTaskEntity = taskRepository.save(existingTaskEntity);
            Task updatedTask = TaskMapper.taskEntityToTask(updatedTaskEntity);

            report.getTasks().removeIf(task -> task.getId().equals(taskId));
            report.getTasks().add(updatedTask);

            TaskResponseDTO responseDTO = TaskMapper.taskToTaskResponseDTO(updatedTask);

            logger.info("Task updated successfully with ID: {}", updatedTask.getId());
            return responseDTO;
        } catch (DataAccessException exception) {
            logger.error("Error updating task with ID {}: " + exception.getMessage(), taskId, exception);
            throw new TaskUpdateException("Error while updating the task with ID: " + taskId + ". " + exception.getMessage(), exception);
        }
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
            logger.error("Error deleting task with ID {}: " + exception.getMessage(), id, exception);
            throw new TaskDeletionException("Error while deleting the task with ID: " + id + ". " + exception.getMessage(), exception);
        }
    }


}