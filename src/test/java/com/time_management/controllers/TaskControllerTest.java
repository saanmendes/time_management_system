package com.time_management.controllers;

import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskResponseDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.app.exceptions.TaskNotFoundException;
import com.time_management.app.ports.TaskService;
import com.time_management.infra.input.controllers.TaskController;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    public TaskControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarTaskComSucesso() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        final var taskResponseDTO = Instancio.create(TaskResponseDTO.class);
        when(taskService.createTask(taskRequestDTO)).thenReturn(taskResponseDTO);

        ResponseEntity<TaskResponseDTO> response = taskController.createTask(taskRequestDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(taskResponseDTO);
        verify(taskService).createTask(taskRequestDTO);
    }

    @Test
    void deveRetornarTaskPorId() {
        final var taskId = "123";
        final var taskResponseDTO = Instancio.create(TaskResponseDTO.class);
        when(taskService.getTaskById(taskId)).thenReturn(taskResponseDTO);

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(taskResponseDTO);
        verify(taskService).getTaskById(taskId);
    }

    @Test
    void deveAtualizarTaskComSucesso() {
        final var taskId = "123";
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);
        final var taskResponseDTO = Instancio.create(TaskResponseDTO.class);
        when(taskService.updateTask(taskId, taskUpdateDTO)).thenReturn(taskResponseDTO);

        ResponseEntity<TaskResponseDTO> response = taskController.updateTask(taskId, taskUpdateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(taskResponseDTO);
        verify(taskService).updateTask(taskId, taskUpdateDTO);
    }

    @Test
    void deveAtualizarStatusPendingDaTask() {
        final var taskId = "123";
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);
        final var taskResponseDTO = Instancio.create(TaskResponseDTO.class);
        when(taskService.updateTaskPendingStatus(taskId, taskUpdateDTO)).thenReturn(taskResponseDTO);

        ResponseEntity<?> response = taskController.updateTaskPending(taskId, taskUpdateDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(taskResponseDTO);
        verify(taskService).updateTaskPendingStatus(taskId, taskUpdateDTO);
    }

    @Test
    void deveDeletarTaskComSucesso() {
        final var taskId = "123";
        doNothing().when(taskService).deleteTask(taskId);

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(taskService).deleteTask(taskId);
    }

    @Test
    void deveLancarExcecaoQuandoTaskNaoForEncontrada() {
        final var taskId = "123";
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).getTaskById(taskId);

        try {
            taskController.getTaskById(taskId);
        } catch (TaskNotFoundException ex) {
            assertThat(ex.getMessage()).isEqualTo("Task not found");
        }

        verify(taskService).getTaskById(taskId);
    }
}
