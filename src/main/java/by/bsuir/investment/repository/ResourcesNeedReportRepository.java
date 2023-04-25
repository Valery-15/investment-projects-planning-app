package by.bsuir.investment.repository;

import by.bsuir.investment.domain.ResourcesNeedReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesNeedReportRepository extends JpaRepository<ResourcesNeedReport, Integer> {
}
