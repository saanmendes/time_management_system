package com.time_management.infra.output.repositories;

import com.time_management.domain.models.Task;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, String> {
    @Query("SELECT t FROM TaskEntity t WHERE t.initialDate >= :startDate")
    List<TaskEntity> findByInitialDate(@Param("startDate") LocalDateTime startDate);
}
