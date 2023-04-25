package by.bsuir.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesNeedReportViewModel {
    private Integer id;
    private LocalDateTime dateTime;
    private List<ResourcesNeedProjectInfo> projectsInfo;
    private Float ownInvestmentResources;
    private Float totalResourcesNeed;
    private Float externalResourcesNeed;
    private String currencyCode;
}
