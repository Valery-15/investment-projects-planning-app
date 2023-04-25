package by.bsuir.investment.dto;

import by.bsuir.investment.domain.Indicators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorReport {
    private Indicators indicator;
    private String value;
    private String valueAnalysis;

    public IndicatorReport(Indicators indicator, String valueAnalysis) {
        this.indicator = indicator;
        this.valueAnalysis = valueAnalysis;
    }
}
