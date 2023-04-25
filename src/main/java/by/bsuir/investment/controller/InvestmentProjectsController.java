package by.bsuir.investment.controller;

import by.bsuir.investment.domain.InvestmentProject;
import by.bsuir.investment.dto.InvestmentProjectDTO;
import by.bsuir.investment.service.CurrencyService;
import by.bsuir.investment.service.DTOService;
import by.bsuir.investment.service.InvestmentProjectEffectivenessService;
import by.bsuir.investment.service.InvestmentProjectService;
import by.bsuir.investment.validator.InvestmentProjectDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/investment-projects")
public class InvestmentProjectsController {
    private final InvestmentProjectService investmentProjectService;
    private final InvestmentProjectDTOValidator projectValidator;
    private final DTOService dtoService;
    private final CurrencyService currencyService;
    private final InvestmentProjectEffectivenessService effectivenessService;

    @Autowired
    public InvestmentProjectsController(InvestmentProjectService investmentProjectService, InvestmentProjectDTOValidator projectValidator, DTOService dtoService, CurrencyService currencyService, InvestmentProjectEffectivenessService effectivenessService) {
        this.investmentProjectService = investmentProjectService;
        this.projectValidator = projectValidator;
        this.dtoService = dtoService;
        this.currencyService = currencyService;
        this.effectivenessService = effectivenessService;
    }

    @InitBinder({"projectDTO", "projectDTOToEdit"})
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(projectValidator);
    }

    @GetMapping()
    public String showAllInvestmentProjects(Model model) {
        model.addAttribute("investmentProjects", investmentProjectService.findAllInvestmentProjects());
        return "/investment-projects/list";
    }

    @GetMapping("/new")
    public String newInvestmentProject(Model model) {
        model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
        model.addAttribute("projectDTO", new InvestmentProjectDTO());
        return "/investment-projects/new";
    }

    @PostMapping()
    public String saveInvestmentProject(Model model, @Valid @ModelAttribute("projectDTO") InvestmentProjectDTO projectDTO, BindingResult result) {
//        if (result.hasErrors()) {
//            model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
//            return "/investment-projects/new";
//        }
//        List<String> errors = investmentProjectService.saveProject(projectDTO);
//        if (errors == null || errors.isEmpty()) {
//            return "redirect:/investment-projects";
//        } else {
//            for (String error : errors) {
//                result.addError(new ObjectError("project", error));
//            }
//            model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
//            return "/investment-projects/new";
//        }
        List<String> errors = null;
        if (!result.hasErrors()) {
            errors = investmentProjectService.saveProject(projectDTO);
        }
        if (errors == null || errors.isEmpty()) {
            return "redirect:/investment-projects";
        } else {
            for (String error : errors) {
                result.addError(new ObjectError("project", error));
            }
            model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
            return "/investment-projects/new";
        }
    }

    @GetMapping("/{id}")
    public String showInvestmentProjectInfo(@PathVariable(name = "id") String projectCode, Model model) {
        model.addAttribute("project", investmentProjectService.findProjectByCode(projectCode));
        model.addAttribute("effectivenessReports", effectivenessService.findReportsByProjectCode(projectCode));
        model.addAttribute("dateTimeFormatter", DateTimeFormatter.ofPattern("HH:mm dd.MM.yy"));
        return "investment-projects/project";
    }

    @GetMapping("/{id}/edit")
    public String editProject(@PathVariable("id") String projectCode, Model model) {
        InvestmentProject projectToEdit = investmentProjectService.findProjectByCode(projectCode);
        if (projectToEdit == null) {
            return "redirect:/investment-projects";
        }
        model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
        model.addAttribute("projectDTOToEdit", dtoService.toInvestmentProjectDTO(projectToEdit));
        model.addAttribute("projectToEditCode", projectToEdit.getCode());
        return "investment-projects/edit";
    }

    @DeleteMapping("/{id}")
    public String deleteInvestmentProject(@PathVariable("id") String projectCode) {
        investmentProjectService.deleteProjectByCode(projectCode);
        return "redirect:/investment-projects";
    }

    @PutMapping("/{id}")
    public String updateProject(@PathVariable("id") String previousCode, @Valid @ModelAttribute("projectDTOToEdit") InvestmentProjectDTO projectDTOToEdit, BindingResult result, Model model) {
        String newCode = projectDTOToEdit.getCode();
        if(newCode != null && !newCode.isEmpty() && !investmentProjectService.mayProjectCodeBeUpdated(previousCode, newCode)){
            result.addError(new ObjectError("project", "инвестиционный проект с кодом " + newCode + " уже существует"));
        }
        if (result.hasErrors()) {
            model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
            model.addAttribute("projectToEditCode", previousCode);
            return "investment-projects/edit";
        }
        investmentProjectService.updateProjectByCode(previousCode, projectDTOToEdit);
        return "redirect:/investment-projects/" + newCode;
//        InvestmentProject changedProject = dtoService.toInvestmentProject(projectDTOToEdit);
//        if (!investmentProjectService.mayProjectCodeBeUpdated(previousCode, changedProject.getCode())) {
//            result.addError(new ObjectError("project", "инвестиционный проект с кодом " + changedProject.getCode() + " уже существует"));
//            model.addAttribute("currenciesCodes", currencyService.getAllCurrenciesCodes());
//            model.addAttribute("projectToEditCode", previousCode);
//            return "investment-projects/edit";
//        }
    }
}
