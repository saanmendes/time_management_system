package com.time_management.infra.output.repositories;

import com.time_management.app.ports.output.TaskRepository;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TaskJpaRepository implements TaskRepository {
    private final JpaTaskRepository repository;

    @Autowired
    public TaskJpaRepository(JpaTaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public TaskEntity save(TaskEntity taskEntity) {
        return repository.save(taskEntity);
    }

    interface JpaTaskRepository extends JpaRepository <TaskEntity, String> {
    }
}