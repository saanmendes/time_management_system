package com.time_management.services;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.app.exceptions.*;
import com.time_management.app.services.TaskServiceImpl;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.domain.models.Report;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import com.time_management.infra.output.repositories.TaskRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private Report report;

    @Mock
    private StackspotAuthenticator stackspotAuthenticator;

    @Mock
    private QuickCommandService quickCommandService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var token = UUID.randomUUID().toString();
        final var executionId = UUID.randomUUID().toString();
        final var category = "TestCategory";

        when(stackspotAuthenticator.authenticate()).thenReturn(token);
        when(quickCommandService.executeCategoryQuickCommand(eq(token), eq(taskRequestDTO))).thenReturn(executionId);
        when(quickCommandService.getQuickCommandCallback(eq(token), eq(executionId))).thenReturn(category);
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID().toString());
            return entity;
        });
        final var result = taskService.createTask(taskRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(category);
        verify(stackspotAuthenticator, times(1)).authenticate();
        verify(quickCommandService, times(1)).executeCategoryQuickCommand(eq(token), eq(taskRequestDTO));
        verify(quickCommandService, atLeastOnce()).getQuickCommandCallback(eq(token), eq(executionId));
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenSavingTaskFails() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        final var token = UUID.randomUUID().toString();
        final var executionId = UUID.randomUUID().toString();
        final var category = "TestCategory";

        when(stackspotAuthenticator.authenticate()).thenReturn(token);
        when(quickCommandService.executeCategoryQuickCommand(eq(token), eq(taskRequestDTO))).thenReturn(executionId);
        when(quickCommandService.getQuickCommandCallback(eq(token), eq(executionId))).thenReturn(category);
        when(taskRepository.save(any(TaskEntity.class))).thenThrow(new DataAccessException("Database error") {});

        assertThatThrownBy(() -> taskService.createTask(taskRequestDTO))
                .isInstanceOf(TaskCreationException.class)
                .hasMessageContaining("Error while saving the task");

        verify(stackspotAuthenticator, times(1)).authenticate();
        verify(quickCommandService, times(1)).executeCategoryQuickCommand(eq(token), eq(taskRequestDTO));
        verify(quickCommandService, atLeastOnce()).getQuickCommandCallback(eq(token), eq(executionId));
        verify(taskRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void shouldFetchAllTasks() {
        final var taskEntities = List.of(Instancio.create(TaskEntity.class));
        final var taskResponses = taskEntities.stream()
                .map(TaskMapper::taskEntityToTaskResponseDTO)
                .toList();

        when(taskRepository.findAll()).thenReturn(taskEntities);

        final var result = taskService.getAllTasks();

        assertThat(result)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyElementsOf(taskResponses);
    }

    @Test
    void shouldThrowExceptionWhenFetchingNonExistentTask() {
        final var taskId = UUID.randomUUID().toString();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        final var taskId = UUID.randomUUID().toString();
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(taskId, taskUpdateDTO))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        final var taskId = UUID.randomUUID().toString();

        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        assertThatCode(() -> taskService.deleteTask(taskId)).doesNotThrowAnyException();
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        final var taskId = UUID.randomUUID().toString();

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }
}