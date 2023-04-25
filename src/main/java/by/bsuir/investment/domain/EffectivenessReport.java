package by.bsuir.investment.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
create table effectiveness_reports(
	id int primary key auto_increment,
	investment_project_code varchar(32) not null references investment_projects(code) on delete cascade on update cascade,
	date_time datetime not null,
    discount_rate float not null,
    cash_flows varchar(1024) not null,
    indicators varchar(64) not null
);
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "effectiveness_reports")
public class EffectivenessReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    private InvestmentProject project;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "discount_rate", nullable = false)
    private Float discountRate;

    @Column(name = "cash_flows", nullable = false, length = 1024)
    private String cashFlowsJson;

    @Column(name = "indicators", nullable = false, length = 64)
    private String indicatorsJson;

    public EffectivenessReport(InvestmentProject project, Float discountRate, String cashFlowsJson, String indicatorsJson) {
        this.project = project;
        this.discountRate = discountRate;
        this.cashFlowsJson = cashFlowsJson;
        this.indicatorsJson = indicatorsJson;
        this.dateTime = LocalDateTime.now();
    }
}
