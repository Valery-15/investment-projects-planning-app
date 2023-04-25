package by.bsuir.investment.service;

import by.bsuir.investment.domain.InvestmentProject;
import by.bsuir.investment.dto.InvestmentProjectDTO;
import by.bsuir.investment.repository.InvestmentProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class InvestmentProjectService {
    private final InvestmentProjectRepository investmentProjectRepository;
    private final DTOService dtoService;

    @Autowired
    public InvestmentProjectService(InvestmentProjectRepository investmentProjectRepository, DTOService dtoService) {
        this.investmentProjectRepository = investmentProjectRepository;
        this.dtoService = dtoService;
    }

    public List<InvestmentProject> findAllInvestmentProjects() {
        return investmentProjectRepository.findAll();
    }

    public List<String> saveProject(InvestmentProjectDTO projectDTO) {
        List<String> errors = new ArrayList<>();
        InvestmentProject project = dtoService.toInvestmentProject(projectDTO);
        if (investmentProjectRepository.existsById(project.getCode())) {
            errors.add("инвестиционный проект с кодом " + project.getCode() + " уже существует");
        } else {
            investmentProjectRepository.save(project);
        }
        return errors;
    }

    public InvestmentProject findProjectByCode(String code) {
        return investmentProjectRepository.findById(code).orElse(null);
    }

    public void deleteProjectByCode(String code) {
        if (investmentProjectRepository.existsById(code)) {
            investmentProjectRepository.deleteById(code);
        }
    }

    public boolean mayProjectCodeBeUpdated(String previousCode, String newCode) {
        if (newCode != null && previousCode != null) {
            if(!previousCode.equals(newCode)){
                if(investmentProjectRepository.existsById(newCode)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void updateProjectByCode(String code, InvestmentProjectDTO projectDTO) {
        InvestmentProject project = dtoService.toInvestmentProject(projectDTO);
        investmentProjectRepository.updateInvestmentProjectByCode(project.getCode(), project.getObject(), project.getImplementationPeriod(), project.getInvestments(), code);
    }



//    public boolean existsByCode(String code){
//        return investmentProjectRepository.existsById(code);
//    }


//    public boolean mayProjectBeSaved(InvestmentProject project) {
//        if (project != null && project.getCode() != null) {
//            if (!investmentProjectRepository.existsById(project.getCode())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void saveProject(InvestmentProject project) {
//        if (mayProjectBeSaved(project)) {
//            investmentProjectRepository.save(project);
//        }
//    }
}
