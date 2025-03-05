package com.time_management.mappers;

import com.time_management.domain.models.Report;
import com.time_management.domain.models.Task;
import com.time_management.infra.input.mappers.ReportMapper;
import com.time_management.infra.input.mappers.TaskMapper;
import com.time_management.infra.output.entities.ReportEntity;
import org.instancio.Instancio;
import org.instancio.TypeToken;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReportMapperTest {

    @Test
    void deveConverterReportEntityParaReportResponseDTO() {
        final var reportEntity = Instancio.create(ReportEntity.class);
        try (MockedStatic<TaskMapper> taskMapperMock = Mockito.mockStatic(TaskMapper.class)) {
            final var tasks = Instancio.create(new TypeToken<List<Task>>() {});
            taskMapperMock.when(() -> TaskMapper.taskEntityToTaskList(reportEntity.getTaskEntities()))
                    .thenReturn(tasks);

            final var result = ReportMapper.reportEntityToReportResponseDTO(reportEntity);

            assertThat(result.getId()).isEqualTo(reportEntity.getId());
            assertThat(result.getIssueDate()).isEqualTo(reportEntity.getIssueDate());
            assertThat(result.getDescription()).isEqualTo(reportEntity.getDescription());
            assertThat(result.getTasks()).isSameAs(tasks);
        }
    }

    @Test
    void deveConverterReportParaReportEntity() {
        final var report = Instancio.create(Report.class);
        try (MockedStatic<TaskMapper> taskMapperMock = Mockito.mockStatic(TaskMapper.class)) {
            final var taskEntities = Instancio.create(List.class);
            taskMapperMock.when(() -> TaskMapper.taskToTaskEntityList(report.getTasks()))
                    .thenReturn(taskEntities);

            final var result = ReportMapper.reportToReportEntity(report);

            assertThat(result.getId()).isEqualTo(report.getId());
            assertThat(result.getIssueDate()).isEqualTo(report.getIssueDate());
            assertThat(result.getDescription()).isEqualTo(report.getDescription());
            assertThat(result.getTaskEntities()).isSameAs(taskEntities);
            assertThat(result.getSuggestion()).isEqualTo(report.getSuggestion());
        }
    }

    @Test
    void deveConverterReportEntityParaReport() {
        final var reportEntity = Instancio.create(ReportEntity.class);

        final var result = ReportMapper.reportEntityToReport(reportEntity);

        assertThat(result.getId()).isEqualTo(reportEntity.getId());
        assertThat(result.getIssueDate()).isEqualTo(reportEntity.getIssueDate());
        assertThat(result.getDescription()).isEqualTo(reportEntity.getDescription());
        assertThat(result.getSuggestion()).isEqualTo(reportEntity.getSuggestion());
    }

    @Test
    void deveConverterReportParaReportResponseDTO() {
        final var report = Instancio.create(Report.class);

        final var result = ReportMapper.reportToReportResponseDTO(report);

        assertThat(result.getId()).isEqualTo(report.getId());
        assertThat(result.getIssueDate()).isEqualTo(report.getIssueDate());
        assertThat(result.getDescription()).isEqualTo(report.getDescription());
        assertThat(result.getTasks()).isEqualTo(report.getTasks());
        assertThat(result.getSuggestion()).isEqualTo(report.getSuggestion());
    }
}
