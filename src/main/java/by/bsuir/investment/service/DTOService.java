package by.bsuir.investment.service;

import by.bsuir.investment.domain.EffectivenessReport;
import by.bsuir.investment.domain.InvestmentProject;
import by.bsuir.investment.domain.ResourcesNeedReport;
import by.bsuir.investment.dto.EffectivenessReportForm;
import by.bsuir.investment.dto.InvestmentProjectDTO;
import by.bsuir.investment.dto.ResourcesNeedProjectInfo;
import by.bsuir.investment.dto.ResourcesNeedReportForm;
import by.bsuir.investment.repository.CurrencyRepository;
import by.bsuir.investment.repository.InvestmentProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DTOService {
    private final CurrencyRepository currencyRepository;
    private final InvestmentProjectRepository projectRepository;
    private final JsonService jsonService;

    @Autowired
    public DTOService(CurrencyRepository currencyRepository, InvestmentProjectRepository projectRepository, JsonService jsonService) {
        this.currencyRepository = currencyRepository;
        this.projectRepository = projectRepository;
        this.jsonService = jsonService;
    }

    public InvestmentProject toInvestmentProject(InvestmentProjectDTO projectDTO){
        InvestmentProject project = new InvestmentProject();
        project.setCode(projectDTO.getCode());
        project.setObject(projectDTO.getObject());
        try{
            project.setImplementationPeriod(Integer.parseInt(projectDTO.getImplementationPeriod()));
            project.setInvestments(Float.parseFloat(projectDTO.getInvestments()));
        } catch (NumberFormatException e){
            return null;
        }
        project.setCurrency(currencyRepository.findById(projectDTO.getCurrencyCode()).orElse(null));
        return project;
    }

    public InvestmentProjectDTO toInvestmentProjectDTO(InvestmentProject project){
        return new InvestmentProjectDTO(
                project.getCode(),
                project.getObject(),
                project.getImplementationPeriod().toString(),
                project.getInvestments().toString(),
                project.getCurrency().getCode()
        );
    }

    public ResourcesNeedReport ToResourcesNeedReport(ResourcesNeedReportForm reportForm){
        List<String> projectsCodes = jsonService.parseProjectsCodesJson(reportForm.getProjectsCodesJson());
        List<ResourcesNeedProjectInfo> projectsInfo = new ArrayList<>();
        for(String code : projectsCodes){
            InvestmentProject project = projectRepository.findById(code).orElse(null);
            if(project != null){
                projectsInfo.add(new ResourcesNeedProjectInfo(
                        project.getObject(),
                        project.getInvestments(),
                        project.getCurrency().getCode()
                ));
            }
        }
        String projectsJson = jsonService.stringifyResourcesNeedProjectsInfo(projectsInfo);
        ResourcesNeedReport report = new ResourcesNeedReport(
                projectsJson,
                Float.parseFloat(reportForm.getOwnInvestmentResources()),
                currencyRepository.findById(reportForm.getCurrencyCode()).get()
        );
        return report;
    }

    public EffectivenessReport toEffectivenessReport(EffectivenessReportForm reportForm){
        return new EffectivenessReport(projectRepository.findById(reportForm.getProjectCode()).orElse(null),
                Float.parseFloat(reportForm.getDiscountRate())/100f,
                reportForm.getCashFlowsJson(),
                reportForm.getIndicatorsJson());
    }
}
