package by.bsuir.investment.dto;

import by.bsuir.investment.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesNeedProjectInfo {
    private String object;
    private Float investments;
    private String currencyCode;
}
