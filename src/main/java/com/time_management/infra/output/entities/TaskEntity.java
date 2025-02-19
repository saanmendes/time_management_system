package com.time_management.infra.output.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "task")
public class TaskEntity {
    @Id
    @UUID
    private String id;
    private String email;
    private String description;
    private LocalDateTime initialDate;
    private LocalDateTime endDate;
    private String role;
    private String priority;
    private String category;
}
