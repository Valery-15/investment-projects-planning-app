package by.bsuir.investment.domain;

public enum Indicators {
    NPV("чистая текущая стоимость (NPV - net present value)", "NPV"),
    PP("период окупаемости (PP - payback period)", "PP"),
    DPP("дисконтированный период окупаемости (DPP - discounted payback period)", "DPP"),
    PI("индекс рентабельности инвестиций (PI - profitability index)","PI"),
    IRR("внутрення норма доходности инвестиций (IRR - internal rate of return)", "IRR");

    public final String description;
    public final String abbreviation;

    private Indicators(String description, String abbreviation) {
        this.description = description;
        this.abbreviation = abbreviation;
    }

    public String getDescription(){
        return description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
