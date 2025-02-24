package com.time_management.infra.output.repositories;

import com.time_management.domain.models.Report;
import com.time_management.infra.output.entities.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, String> {
}
