package by.bsuir.investment.repository;

import by.bsuir.investment.domain.EffectivenessReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EffectivenessReportRepository extends JpaRepository<EffectivenessReport, Integer> {
    @Query("select report from EffectivenessReport report where report.project.code = :projectCode")
    List<EffectivenessReport> findAllByProjectCode(@Param("projectCode") String projectCode);
}
