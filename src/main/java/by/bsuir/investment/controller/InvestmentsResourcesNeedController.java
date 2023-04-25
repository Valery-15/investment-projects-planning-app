package by.bsuir.investment.controller;

import by.bsuir.investment.domain.ResourcesNeedReport;
import by.bsuir.investment.dto.ResourcesNeedReportForm;
import by.bsuir.investment.repository.ResourcesNeedReportRepository;
import by.bsuir.investment.service.*;
import by.bsuir.investment.validator.ResourcesNeedReportFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/investment-resources-need")
public class InvestmentsResourcesNeedController {
    private final InvestmentProjectService investmentProjectService;
    private final CurrencyService currencyService;
    private final ResourcesNeedReportFormValidator reportFormValidator;
    private final DTOService dtoService;
    private final ResourcesNeedReportRepository resourcesReportRepository;
    private final InvestmentResourcesNeedService resourcesNeedService;
    private final JsonService jsonService;

    @Autowired
    public InvestmentsResourcesNeedController(InvestmentProjectService investmentProjectService, CurrencyService currencyService, ResourcesNeedReportFormValidator reportFormValidator, DTOService dtoService, ResourcesNeedReportRepository resourcesReportRepository, InvestmentResourcesNeedService resourcesNeedService, JsonService jsonService) {
        this.investmentProjectService = investmentProjectService;
        this.currencyService = currencyService;
        this.reportFormValidator = reportFormValidator;
        this.dtoService = dtoService;
        this.resourcesReportRepository = resourcesReportRepository;
        this.resourcesNeedService = resourcesNeedService;
        this.jsonService = jsonService;
    }

    @InitBinder({"resourcesReportForm"})
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(reportFormValidator);
    }

    @GetMapping("/enter-initial-data")
    public String enterInitialData(Model model){
        model.addAttribute("investmentProjects", investmentProjectService.findAllInvestmentProjects());
        model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
        model.addAttribute("resourcesReportForm", new ResourcesNeedReportForm());
        return "investment-resources-need/initial-data";
    }

    @PostMapping("/calculate-resources-need")
    public String calculateResourcesNeed(@Valid @ModelAttribute("resourcesReportForm") ResourcesNeedReportForm reportForm, BindingResult result, Model model){
        ResourcesNeedReport report = resourcesNeedService.saveReport(reportForm);
        model.addAttribute("report", resourcesNeedService.getReportViewModel(report));
        return "investment-resources-need/report";
    }

    @GetMapping("/reports")
    public String getAllReports(Model model){
        model.addAttribute("resourcesNeedReports", resourcesNeedService.findAllReports());
        return "investment-resources-need/reports";
    }

    @GetMapping("/report/{id}")
    public String getResourcesNeedReport(@PathVariable("id") String reportId, Model model){
        ResourcesNeedReport report = resourcesNeedService.findReportById(Integer.parseInt(reportId));
        model.addAttribute("report", resourcesNeedService.getReportViewModel(report));
        return "investment-resources-need/report";
    }
}
