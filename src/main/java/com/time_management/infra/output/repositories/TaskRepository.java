package com.time_management.infra.output.repositories;

import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, String> {
    List<TaskEntity> findByInitialDateGreaterThanEqual(LocalDateTime startDate);
}
