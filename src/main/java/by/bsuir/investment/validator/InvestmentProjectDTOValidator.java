package by.bsuir.investment.validator;

import by.bsuir.investment.dto.InvestmentProjectDTO;
import by.bsuir.investment.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class InvestmentProjectDTOValidator implements Validator {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public InvestmentProjectDTOValidator(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return InvestmentProjectDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        InvestmentProjectDTO projectDTO = (InvestmentProjectDTO) o;

        //валидация кода ИП
        if (projectDTO.getCode() == null || projectDTO.getCode().isEmpty()) {
            errors.reject("empty.code", "отсутствует код проекта");
        } else if (!projectDTO.getCode().matches("^[A-Za-z\\d]{1,32}$")) {
            errors.reject("invalid.code", "код проекта должен содержать от 1 до 32 символов и состоять из цифр и английских букв");
        }

        //валидация объекта инвестирования ИП
        if (projectDTO.getObject() == null || projectDTO.getObject().isEmpty()) {
            errors.reject("empty.object", "отсутствует объект инвестирования");
        } else if (!projectDTO.getObject().matches("^.{3,256}$")) {
            errors.reject("invalid.object.length", "наименование объекта инвестирования должно содержать от 3 до 256 символов");
        }

        //валидация срока реализации проекта
        if (projectDTO.getImplementationPeriod() == null || projectDTO.getImplementationPeriod().isEmpty()) {
            errors.reject("empty.implementation.period", "отсутствует срок реализации проекта");
        } else {
            try {
                if (Integer.parseInt(projectDTO.getImplementationPeriod()) <= 0) {
                    errors.reject("invalid.implementation.period", "срок реализации проекта должен быть не менее 1 года");
                }
            } catch (NumberFormatException e) {
                errors.reject("invalid.implementation.period", "срок реализации проекта должен быть представлен целым числом лет");
            }
        }

        //валидация суммы инвестиций
        if (projectDTO.getInvestments() == null || projectDTO.getInvestments().isEmpty()) {
            errors.reject("empty.investments", "отсутствует сумма инвестируемых средств");
        } else {
            try {
                if (Float.parseFloat(projectDTO.getInvestments()) <= 0) {
                    errors.reject("invalid.investments", "сумма инвестируемых средств должна быть более 0 д.е.");
                }
            } catch (NumberFormatException e) {
                errors.reject("invalid.investments", "сумма инвестируемых средств должна быть представлена в числовом формате (разделитель дробной части - точка)");
            }
        }

        //валидация кода валюты
        if (projectDTO.getCurrencyCode() == null || projectDTO.getCurrencyCode().isEmpty()) {
            errors.reject("empty.currency.code", "отсутствует валюта");
        } else if(!currencyRepository.existsById(projectDTO.getCurrencyCode())){
            errors.reject("invalid.currency.code", "недействительный код валюты");
        }
    }
}
