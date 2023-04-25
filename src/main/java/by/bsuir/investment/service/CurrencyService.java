package by.bsuir.investment.service;

import by.bsuir.investment.domain.Currency;
import by.bsuir.investment.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<String> getAllCurrenciesCodes(){
        return currencyRepository.getAllCurrenciesCodes();
    }
}
