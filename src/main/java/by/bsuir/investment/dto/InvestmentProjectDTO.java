package by.bsuir.investment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestmentProjectDTO {
    private String code;
    private String object;
    private String implementationPeriod;
    private String investments;
    private String currencyCode;
}
