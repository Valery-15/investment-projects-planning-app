package by.bsuir.investment.dto;

import by.bsuir.investment.domain.InvestmentProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EffectivenessReportViewModel {
    private Integer id;
    private LocalDateTime dateTime;
    private InvestmentProject project;
    private Map<Integer, Float> cashFlows;
    private Float discountRate;
    private List<IndicatorReport> indicatorsReports;
}
