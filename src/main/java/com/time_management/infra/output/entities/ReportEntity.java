package com.time_management.infra.output.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "report")
public class ReportEntity {
    @Id
    @UUID
    private String id;
    private String description;
    private LocalDateTime issueDate;
    private Suggestion suggestion;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "report_entity_id")
    private List<TaskEntity> taskEntities =  new ArrayList<>();
}
