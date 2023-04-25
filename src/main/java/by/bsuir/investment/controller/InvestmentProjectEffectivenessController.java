package by.bsuir.investment.controller;

import by.bsuir.investment.domain.EffectivenessReport;
import by.bsuir.investment.dto.EffectivenessReportForm;
import by.bsuir.investment.service.InvestmentProjectEffectivenessService;
import by.bsuir.investment.service.InvestmentProjectService;
import by.bsuir.investment.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/investment-project-effectiveness")
public class InvestmentProjectEffectivenessController {
    private final InvestmentProjectService investmentProjectService;
    private final InvestmentProjectEffectivenessService effectivenessService;
    private final JsonService jsonService;

    @Autowired
    public InvestmentProjectEffectivenessController(InvestmentProjectService investmentProjectService, InvestmentProjectEffectivenessService effectivenessService, JsonService jsonService) {
        this.investmentProjectService = investmentProjectService;
        this.effectivenessService = effectivenessService;
        this.jsonService = jsonService;
    }

    @GetMapping("/{id}/enter-initial-data")
    public String enterInitialData(@PathVariable(name = "id") String projectCode, Model model) {
        model.addAttribute("project", investmentProjectService.findProjectByCode(projectCode));
        model.addAttribute("report", new EffectivenessReportForm());
        return "investment-project-effectiveness/initial-data";
    }

    @PostMapping("/{id}/calculate-indicators")
    public String calculateIndicators(@PathVariable(name = "id") String projectCode,
                                      @ModelAttribute("report") EffectivenessReportForm reportForm,
                                      Model model) {
        reportForm.setProjectCode(projectCode);
        EffectivenessReport report = effectivenessService.saveReport(reportForm);
        return "redirect:/investment-project-effectiveness/report/" + report.getId();
    }

    @GetMapping("/report/{id}")
    public String getEffectivenessReport(@PathVariable("id") String reportId, Model model){
        EffectivenessReport report = effectivenessService.findReportById(Integer.parseInt(reportId));
        model.addAttribute("report", effectivenessService.getReportViewModel(report));
        return "investment-project-effectiveness/report";
    }





    @GetMapping("/choose-project")
    public String chooseProject(Model model) {
        model.addAttribute("investmentProjects", investmentProjectService.findAllInvestmentProjects());
        return "investment-project-effectiveness/choose-project";
    }




}
