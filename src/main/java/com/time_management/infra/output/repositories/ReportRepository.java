package com.time_management.infra.output.repositories;

import com.time_management.domain.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, String> {
}
