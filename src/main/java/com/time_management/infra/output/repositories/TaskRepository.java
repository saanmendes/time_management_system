package com.time_management.infra.output.repositories;

import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, String> {

}
