package by.bsuir.investment.repository;

import by.bsuir.investment.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    @Query("select currency.code from Currency currency")
    List<String> getAllCurrenciesCodes();

    @Query("select currency.exchangeRate from Currency currency where currency.code = :currencyCode")
    Float getExchangeRateByCurrencyCode(@Param("currencyCode") String currencyCode);
}
