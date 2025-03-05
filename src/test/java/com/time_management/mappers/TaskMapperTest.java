package com.time_management.mappers;


import com.time_management.app.dtos.tasks.TaskRequestDTO;
import com.time_management.app.dtos.tasks.TaskUpdateDTO;
import com.time_management.domain.models.Task;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.TaskEntity;
import org.instancio.Instancio;
import org.instancio.TypeToken;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    @Test
    void deveConverterTaskParaTaskEntity() {
        final var task = Instancio.create(Task.class);
        final var result = TaskMapper.taskToTaskEntity(task);

        System.out.println("Task.isCompleted: " + task.isCompleted());
        System.out.println("TaskEntity.isCompleted: " + result.isCompleted());

        assertThat(result.getId()).isEqualTo(task.getId());
        assertThat(result.getDescription()).isEqualTo(task.getDescription());
        assertThat(result.getPriority()).isEqualTo(task.getPriority());
        assertThat(result.getCategory()).isEqualTo(task.getCategory());
        assertThat(result.getRole()).isEqualTo(task.getRole());
        assertThat(result.getInitialDate()).isEqualTo(task.getInitialDate());
        assertThat(result.getEndDate()).isEqualTo(task.getEndTime());
        assertThat(result.getEmail()).isEqualTo(task.getEmail());
        assertThat(result.isCompleted()).isEqualTo(task.isCompleted());
    }

    @Test
    void deveConverterTaskEntityParaTask() {
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var result = TaskMapper.taskEntityToTask(taskEntity);
        assertThat(result.getId()).isEqualTo(taskEntity.getId());
        assertThat(result.getDescription()).isEqualTo(taskEntity.getDescription());
        assertThat(result.getPriority()).isEqualTo(taskEntity.getPriority());
        assertThat(result.getCategory()).isEqualTo(taskEntity.getCategory());
        assertThat(result.getRole()).isEqualTo(taskEntity.getRole());
        assertThat(result.getInitialDate()).isEqualTo(taskEntity.getInitialDate());
        assertThat(result.getEndTime()).isEqualTo(taskEntity.getEndDate());
        assertThat(result.getEmail()).isEqualTo(taskEntity.getEmail());
        assertThat(result.isCompleted()).isEqualTo(taskEntity.isCompleted());
    }

    @Test
    void deveConverterTaskRequestParaTaskEntity() {
        final var taskRequestDTO = Instancio.create(TaskRequestDTO.class);
        final var result = TaskMapper.taskRequestToTaskEntity(taskRequestDTO);
        assertThat(result.getDescription()).isEqualTo(taskRequestDTO.getDescription());
        assertThat(result.getEmail()).isEqualTo(taskRequestDTO.getEmail());
        assertThat(result.getRole()).isEqualTo(taskRequestDTO.getRole());
        assertThat(result.getInitialDate()).isEqualTo(taskRequestDTO.getInitialDate());
        assertThat(result.getEndDate()).isEqualTo(taskRequestDTO.getEndDate());
        assertThat(result.isCompleted()).isEqualTo(taskRequestDTO.isCompleted());
    }

    @Test
    void deveConverterTaskParaTaskResponseDTO() {
        final var task = Instancio.create(Task.class);
        final var result = TaskMapper.taskToTaskResponseDTO(task);
        assertThat(result.getId()).isEqualTo(task.getId());
        assertThat(result.getDescription()).isEqualTo(task.getDescription());
        assertThat(result.getEmail()).isEqualTo(task.getEmail());
        assertThat(result.getRole()).isEqualTo(task.getRole());
        assertThat(result.getInitialDate()).isEqualTo(task.getInitialDate());
        assertThat(result.getCategory()).isEqualTo(task.getCategory());
        assertThat(result.isCompleted()).isEqualTo(task.isCompleted());
        assertThat(result.getPriority()).isEqualTo(task.getPriority());
    }

    @Test
    void deveConverterTaskEntityParaTaskResponseDTO() {
        final var taskEntity = Instancio.create(TaskEntity.class);
        final var result = TaskMapper.taskEntityToTaskResponseDTO(taskEntity);
        assertThat(result.getId()).isEqualTo(taskEntity.getId());
        assertThat(result.getEmail()).isEqualTo(taskEntity.getEmail());
        assertThat(result.getDescription()).isEqualTo(taskEntity.getDescription());
        assertThat(result.getRole()).isEqualTo(taskEntity.getRole());
        assertThat(result.getInitialDate()).isEqualTo(taskEntity.getInitialDate());
        assertThat(result.getEndDate()).isEqualTo(taskEntity.getEndDate());
        assertThat(result.getPriority()).isEqualTo(taskEntity.getPriority());
        assertThat(result.getCategory()).isEqualTo(taskEntity.getCategory());
        assertThat(result.isCompleted()).isEqualTo(taskEntity.isCompleted());
    }

    @Test
    void deveConverterTaskUpdateParaTaskEntity() {
        final var taskUpdateDTO = Instancio.create(TaskUpdateDTO.class);
        final var result = TaskMapper.taskUpdateToTaskEntity(taskUpdateDTO);
        assertThat(result.getEmail()).isEqualTo(taskUpdateDTO.getEmail());
        assertThat(result.getDescription()).isEqualTo(taskUpdateDTO.getDescription());
        assertThat(result.getRole()).isEqualTo(taskUpdateDTO.getRole());
        assertThat(result.getInitialDate()).isEqualTo(taskUpdateDTO.getInitialDate());
        assertThat(result.getEndDate()).isEqualTo(taskUpdateDTO.getEndDate());
        assertThat(result.isCompleted()).isEqualTo(taskUpdateDTO.isCompleted());
    }

    @Test
    void deveConverterListaDeTaskEntityParaListaDeTask() {
        final var taskEntityList = Instancio.create(new TypeToken<List<TaskEntity>>() {});
        final var result = TaskMapper.taskEntityToTaskList(taskEntityList);
        assertThat(result).hasSameSizeAs(taskEntityList);
        for (int i = 0; i < taskEntityList.size(); i++) {
            assertThat(result.get(i).getId()).isEqualTo(taskEntityList.get(i).getId());
        }
    }

    @Test
    void deveConverterListaDeTaskParaListaDeTaskEntity() {
        final var taskList = Instancio.create(new TypeToken<List<Task>>() {});
        final var result = TaskMapper.taskToTaskEntityList(taskList);
        assertThat(result).hasSameSizeAs(taskList);
        for (int i = 0; i < taskList.size(); i++) {
            assertThat(result.get(i).getId()).isEqualTo(taskList.get(i).getId());
        }
    }
}
