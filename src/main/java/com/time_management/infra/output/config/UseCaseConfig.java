package com.time_management.infra.output.config;

import com.time_management.domain.usecases.ReportUseCase;
import com.time_management.domain.usecases.impl.ReportUseCaseImpl;
import com.time_management.infra.output.repositories.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ReportUseCase reportUseCase(TaskRepository taskRepository) {
        return new ReportUseCaseImpl(taskRepository);
    }

}
