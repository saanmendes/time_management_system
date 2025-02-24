package com.time_management.infra.output.repositories;

import com.time_management.infra.output.entities.ReportEntity;
import com.time_management.infra.output.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {

}
