package by.bsuir.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourcesNeedReportForm {
    private String ownInvestmentResources;
    private String currencyCode;
    private String projectsCodesJson;
}
