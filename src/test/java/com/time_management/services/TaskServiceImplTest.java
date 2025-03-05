package com.time_management.services;
import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.app.exceptions.*;
import com.time_management.app.services.TaskServiceImpl;
import com.time_management.app.services.stackspot.QuickCommandService;
import com.time_management.app.services.stackspot.StackspotAuthenticator;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import com.time_management.infra.output.repositories.TaskRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private StackspotAuthenticator stackspotAuthenticator;

    @Mock
    private QuickCommandService quickCommandService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void deveCriarTarefaComSucesso() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var token = UUID.randomUUID().toString();
        final var executionId = UUID.randomUUID().toString();

        when(stackspotAuthenticator.authenticate()).thenReturn(token);
        when(quickCommandService.executeCategoryQuickCommand(eq(token), eq(taskRequestDTO))).thenReturn(executionId);
        when(quickCommandService.getQuickCommandCallback(eq(token), eq(executionId))).thenReturn("category");
        when(taskRepository.save(any(TaskEntity.class))).thenAnswer(invocation -> {
            TaskEntity entity = invocation.getArgument(0);
            entity.setId(UUID.randomUUID().toString()); // Simula ID gerado pelo banco
            return entity;
        });

        final var taskResponseDTO = TaskMapper.taskEntityToTaskResponseDTO(taskEntity);
        final var result = taskService.createTask(taskRequestDTO);

        assertThat(result).usingRecursiveComparison().isEqualTo(taskResponseDTO);
    }

    @Test
    void deveLancarExcecaoAoCriarTarefaComErro() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        when(stackspotAuthenticator.authenticate()).thenThrow(new RuntimeException("Erro de autenticação"));

        assertThatThrownBy(() -> taskService.createTask(taskRequestDTO))
                .isInstanceOf(TaskCreationException.class)
                .hasMessageContaining("Error while saving the task");
    }

    @Test
    void deveBuscarTodasAsTarefas() {
        final var taskEntities = List.of(Instancio.create(TaskEntity.class));
        final var taskResponses = taskEntities.stream()
                .map(TaskMapper::taskEntityToTaskResponseDTO)
                .toList();

        when(taskRepository.findAll()).thenReturn(taskEntities);

        final var result = taskService.getAllTasks();

        assertThat(result).containsExactlyElementsOf(taskResponses);
    }

    @Test
    void deveLancarExcecaoAoBuscarTarefaInexistente() {
        final var taskId = UUID.randomUUID().toString();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void deveAtualizarTarefaComSucesso() {
        final var taskId = UUID.randomUUID().toString();
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var taskResponseDTO = TaskMapper.taskEntityToTaskResponseDTO(taskEntity);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        final var result = taskService.updateTask(taskId, taskUpdateDTO);

        assertThat(result).usingRecursiveComparison().isEqualTo(taskResponseDTO);
    }

    @Test
    void deveLancarExcecaoAoAtualizarTarefaInexistente() {
        final var taskId = UUID.randomUUID().toString();
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(taskId, taskUpdateDTO))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }

    @Test
    void deveAtualizarStatusDeTarefaComSucesso() {
        final var taskId = UUID.randomUUID().toString();
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var taskResponseDTO = TaskMapper.taskEntityToTaskResponseDTO(taskEntity);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);

        final var result = taskService.updateTaskPendingStatus(taskId, taskUpdateDTO);

        assertThat(result).usingRecursiveComparison().isEqualTo(taskResponseDTO);
    }

    @Test
    void deveDeletarTarefaComSucesso() {
        final var taskId = UUID.randomUUID().toString();

        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        assertThatCode(() -> taskService.deleteTask(taskId)).doesNotThrowAnyException();
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void deveLancarExcecaoAoDeletarTarefaInexistente() {
        final var taskId = UUID.randomUUID().toString();

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found");
    }
}