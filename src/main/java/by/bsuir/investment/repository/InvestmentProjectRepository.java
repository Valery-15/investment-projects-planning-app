package by.bsuir.investment.repository;

import by.bsuir.investment.domain.InvestmentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentProjectRepository extends JpaRepository<InvestmentProject, String> {
    @Modifying()
    @Query("update InvestmentProject p set p.code = :newCode, p.object = :object, p.implementationPeriod = :implementationPeriod, p.investments = :investments where p.code = :previousCode")
    void updateInvestmentProjectByCode(@Param("newCode") String newCode, @Param("object") String object, @Param("implementationPeriod") Integer implementationPeriod, @Param("investments") Float investments, @Param("previousCode") String previousCode);
}
