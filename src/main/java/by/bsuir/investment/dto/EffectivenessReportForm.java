package by.bsuir.investment.dto;

import by.bsuir.investment.domain.InvestmentProject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EffectivenessReportForm {
    private String projectCode;
    private String discountRate;
    private String indicatorsJson;
    private String cashFlowsJson;
}
