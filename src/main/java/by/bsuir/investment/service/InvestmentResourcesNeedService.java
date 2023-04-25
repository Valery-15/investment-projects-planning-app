package by.bsuir.investment.service;

import by.bsuir.investment.domain.EffectivenessReport;
import by.bsuir.investment.domain.InvestmentProject;
import by.bsuir.investment.domain.ResourcesNeedReport;
import by.bsuir.investment.dto.ResourcesNeedProjectInfo;
import by.bsuir.investment.dto.ResourcesNeedReportForm;
import by.bsuir.investment.dto.ResourcesNeedReportViewModel;
import by.bsuir.investment.repository.CurrencyRepository;
import by.bsuir.investment.repository.InvestmentProjectRepository;
import by.bsuir.investment.repository.ResourcesNeedReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentResourcesNeedService {
    private final InvestmentProjectRepository investmentProjectRepository;
    private final CurrencyRepository currencyRepository;
    private final JsonService jsonService;
    private final ResourcesNeedReportRepository reportRepository;
    private final DTOService dtoService;

    @Autowired
    public InvestmentResourcesNeedService(InvestmentProjectRepository investmentProjectRepository, CurrencyRepository currencyRepository, JsonService jsonService, ResourcesNeedReportRepository reportRepository, DTOService dtoService) {
        this.investmentProjectRepository = investmentProjectRepository;
        this.currencyRepository = currencyRepository;
        this.jsonService = jsonService;
        this.reportRepository = reportRepository;
        this.dtoService = dtoService;
    }
    public Float calculateTotalResourcesNeed(ResourcesNeedReport report) {
        List<ResourcesNeedProjectInfo> projectsInfo = jsonService.parseResourcesNeedProjectsInfoJson(report.getProjectsJson());
        Float totalInvestments = 0f;
        for (ResourcesNeedProjectInfo project : projectsInfo) {
            totalInvestments += project.getInvestments() * currencyRepository.getExchangeRateByCurrencyCode(project.getCurrencyCode());
        }
        totalInvestments /= report.getCurrency().getExchangeRate();
        return totalInvestments;
    }

    public Float calculateExternalResourcesNeed(ResourcesNeedReport report) {
        Float totalResourcesNeed = calculateTotalResourcesNeed(report);
        if(totalResourcesNeed > report.getOwnInvestmentResources()){
            return totalResourcesNeed - report.getOwnInvestmentResources();
        } else {
            return 0f;
        }
    }

    public ResourcesNeedReport saveReport(ResourcesNeedReportForm reportForm){
        ResourcesNeedReport report = dtoService.ToResourcesNeedReport(reportForm);
        reportRepository.save(report);
        return report;
    }

    public ResourcesNeedReportViewModel getReportViewModel(ResourcesNeedReport report){
        return new ResourcesNeedReportViewModel(
                report.getId(),
                report.getDateTime(),
                jsonService.parseResourcesNeedProjectsInfoJson(report.getProjectsJson()),
                report.getOwnInvestmentResources(),
                calculateTotalResourcesNeed(report),
                calculateExternalResourcesNeed(report),
                report.getCurrency().getCode()
        );
    }

    public List<ResourcesNeedReport> findAllReports(){
        return reportRepository.findAll();
    }

    public ResourcesNeedReport findReportById(Integer reportId){
        return reportRepository.findById(reportId).orElse(null);
    }
}
