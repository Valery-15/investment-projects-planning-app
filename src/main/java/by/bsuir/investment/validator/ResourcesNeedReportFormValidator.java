package by.bsuir.investment.validator;

import by.bsuir.investment.dto.ResourcesNeedReportForm;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ResourcesNeedReportFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return ResourcesNeedReportForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
