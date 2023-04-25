package by.bsuir.investment.service;

import by.bsuir.investment.domain.EffectivenessReport;
import by.bsuir.investment.domain.Indicators;
import by.bsuir.investment.domain.InvestmentProject;
import by.bsuir.investment.domain.ResourcesNeedReport;
import by.bsuir.investment.dto.*;
import by.bsuir.investment.repository.EffectivenessReportRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class InvestmentProjectEffectivenessService {
    private final DTOService dtoService;
    private final EffectivenessReportRepository reportRepository;
    private final JsonService jsonService;

    public InvestmentProjectEffectivenessService(DTOService dtoService, EffectivenessReportRepository reportRepository, JsonService jsonService) {
        this.dtoService = dtoService;
        this.reportRepository = reportRepository;
        this.jsonService = jsonService;
    }

    public List<EffectivenessReport> findReportsByProjectCode(String projectCode) {
        return reportRepository.findAllByProjectCode(projectCode);
    }

    public EffectivenessReport saveReport(EffectivenessReportForm reportForm) {
        EffectivenessReport report = dtoService.toEffectivenessReport(reportForm);
        reportRepository.save(report);
        return report;
    }

    public EffectivenessReport findReportById(Integer reportId) {
        return reportRepository.findById(reportId).orElse(null);
    }

    public EffectivenessReportViewModel getReportViewModel(EffectivenessReport report) {
        List<Indicators> indicators = jsonService.parseIndicatorsJson(report.getIndicatorsJson());
        Map<Integer, Float> cashFlows = jsonService.parseCashFlowsJson(report.getCashFlowsJson());
        Map<Indicators, Double> calculatedIndicators = calculateIndicators(report.getProject().getInvestments(), cashFlows, report.getDiscountRate(), indicators);
        List<IndicatorReport> indicatorsReports = makeIndicatorsReports(calculatedIndicators, report.getDiscountRate());
        return new EffectivenessReportViewModel(
                report.getId(),
                report.getDateTime(),
                report.getProject(),
                cashFlows,
                report.getDiscountRate(),
                indicatorsReports
        );
    }

    public Map<Indicators, Double> calculateIndicators(Float investments, Map<Integer, Float> cashFlows, Float discountRate, List<Indicators> indicators) {
        Map<Indicators, Double> calculatedIndicators = new HashMap<>();
        for (Indicators indicator : indicators) {
            calculatedIndicators.put(indicator,
                    switch (indicator) {
                        case NPV -> calculateNetPresentValue(investments, cashFlows, discountRate);
                        case PP -> calculatePaybackPeriod(investments, cashFlows);
                        case DPP -> calculateDiscountedPaybackPeriod(investments, cashFlows, discountRate);
                        case PI -> calculateProfitabilityIndex(investments, cashFlows, discountRate);
                        case IRR -> calculateInternalRateOfReturn(investments, cashFlows, discountRate);
                    });
        }
        return calculatedIndicators;
    }

    public Double calculateNetPresentValue(Float investments, Map<Integer, Float> cashFlows, Float discountRate) {
        return calculateCashFlowsPresentValue(cashFlows, discountRate) - investments.doubleValue();
    }

    public Double calculateCashFlowsPresentValue(Map<Integer, Float> cashFlows, Float discountRate) {
        Double cfpv = 0d;
        for (Map.Entry<Integer, Float> cashFlow : cashFlows.entrySet()) {
            cfpv = cfpv + cashFlow.getValue() / Math.pow((1 + discountRate), cashFlow.getKey());
        }
        return cfpv;
    }

    public Double calculateProfitabilityIndex(Float investments, Map<Integer, Float> cashFlows, Float discountRate) {
        return calculateCashFlowsPresentValue(cashFlows, discountRate) / investments;
    }

    public Double calculateDiscountedPaybackPeriod(Float investments, Map<Integer, Float> cashFlows, Float discountRate) {
        Map<Integer, Float> cashFlowsOrderedByYear = new TreeMap<>(cashFlows);
        Double accumulatedCashFlow = 0d;
        Double previousYearAccumulatedCashFlow;
        for (Map.Entry<Integer, Float> cashFlow : cashFlowsOrderedByYear.entrySet()) {
            previousYearAccumulatedCashFlow = accumulatedCashFlow;
            accumulatedCashFlow += discountCashFlow(cashFlow, discountRate);
            if (accumulatedCashFlow >= investments) {
                return cashFlow.getKey() - 1 + ((investments - previousYearAccumulatedCashFlow) / discountCashFlow(cashFlow, discountRate));
            }
        }
        return null;
    }

    public Double discountCashFlow(Map.Entry<Integer, Float> cashFlow, Float discountRate) {
        return cashFlow.getValue() / Math.pow((1 + discountRate), cashFlow.getKey());
    }

    public Double calculatePaybackPeriod(Float investments, Map<Integer, Float> cashFlows) {
        Map<Integer, Float> cashFlowsOrderedByYear = new TreeMap<>(cashFlows);
        Double accumulatedCashFlow = 0d;
        Double previousYearAccumulatedCashFlow;
        for (Map.Entry<Integer, Float> cashFlow : cashFlowsOrderedByYear.entrySet()) {
            previousYearAccumulatedCashFlow = accumulatedCashFlow;
            accumulatedCashFlow += cashFlow.getValue();
            if (accumulatedCashFlow >= investments) {
                return cashFlow.getKey() - 1 + ((investments - previousYearAccumulatedCashFlow) / cashFlow.getValue());
            }
        }
        return null;
    }

    public Double calculateInternalRateOfReturn(Float investments, Map<Integer, Float> cashFlows, Float discountRate) {
        discountRate = Float.parseFloat(new BigDecimal(Float.toString(discountRate)).setScale(2, RoundingMode.HALF_UP).toString());
        Double npv = calculateNetPresentValue(investments, cashFlows, discountRate);
        if (npv == 0d) {
            return discountRate.doubleValue();
        }
        float d1, d2;
        double npv1, npv2;
        if (npv > 0) {
            while (npv >= 0) {
                discountRate = discountRate + 0.01f;
                npv = calculateNetPresentValue(investments, cashFlows, discountRate);
            }
            d2 = discountRate;
            d1 = d2 - 0.01f;
        } else {
            while (npv <= 0) {
                discountRate = discountRate - 0.01f;
                npv = calculateNetPresentValue(investments, cashFlows, discountRate);
            }
            d1 = discountRate;
            d2 = d1 + 0.01f;
        }
        npv1 = calculateNetPresentValue(investments, cashFlows, d1);
        npv2 = calculateNetPresentValue(investments, cashFlows, d2);
        Double irr = d1 + ((d2 - d1) * npv1 / (npv1 - npv2));
        return irr;
    }

    public List<IndicatorReport> makeIndicatorsReports(Map<Indicators, Double> indicatorsValues, Float discountRate) {
        List<IndicatorReport> indicatorReports = new ArrayList<>();
        for (Map.Entry<Indicators, Double> indicator : indicatorsValues.entrySet()) {
            IndicatorReport indicatorReport = new IndicatorReport(indicator.getKey(),
                    switch (indicator.getKey()) {
                        case NPV -> {
                            Double npv = indicator.getValue();
                            if (npv > 0) {
                                yield "NPV > 0, реализация проекта позволит инвестору увеличить капитал, проект эффективен по критерию NPV";
                            } else if (npv == 0) {
                                yield "NPV = 0, инвестиции окупаются, однако не приносят инвестору доход";
                            } else {
                                yield "NPV < 0, реализация проекта не приносит инвестору доход на вложенный капитал, проект неэффективен по критерию NPV";
                            }
                        }
                        case PP -> {
                            Double pp = indicator.getValue();
                            if (pp == null) {
                                yield "инвестиционный проект не окупается";
                            } else {
                                yield "инвестиционный проект окупится за " + pp + " года";
                            }
                        }
                        case DPP -> {
                            Double dpp = indicator.getValue();
                            if (dpp == null) {
                                yield "инвестиционный проект не окупается (по показателю дисконтированного срока окупаемости)";
                            } else {
                                yield "инвестиционный проект окупится за " + dpp + " года (по показателю дисконтированного срока окупаемости)";
                            }
                        }
                        case PI -> {
                            Double pi = indicator.getValue();
                            if (pi > 1) {
                                yield "PI > 1, инвестиционный проект является прибыльным";
                            } else if (pi == 1) {
                                yield "PI = 1, инвестиционный проект не является ни прибыльным, ни убыточным";
                            } else {
                                yield "PI < 1, инвестиционный проект является убыточным";
                            }
                        }
                        case IRR -> {
                            Double irr = indicator.getValue();
                            if (irr > discountRate) {
                                yield "IRR > d, инвестиционный проект эффективен";
                            } else if (irr == discountRate.doubleValue()) {
                                yield "IRR = d, инвестиционный проект не является ни прибыльным, ни убыточным";
                            } else {
                                yield "IRR < d, инвестиционный проект неэффективен";
                            }
                        }
                    });
            if(indicator.getValue() == null){
                indicatorReport.setValue("-");
            } else {
                indicatorReport.setValue(new BigDecimal(indicator.getValue().toString()).setScale(3, RoundingMode.HALF_UP).toString());
            }
            indicatorReports.add(indicatorReport);
        }
        return indicatorReports;
    }
}
